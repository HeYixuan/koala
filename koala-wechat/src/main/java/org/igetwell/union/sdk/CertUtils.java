package org.igetwell.union.sdk;

import org.igetwell.common.uitls.CharacterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 证书工具类，主要用于对证书的加载和使用
 * 声明：以下代码只是为了方便接入方测试而提供的样例代码，
 * 商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障
 */
@Component
public class CertUtils {

    protected static final Logger logger = LoggerFactory.getLogger(CertUtils.class);

    public static final String UNION_PAY_NAME = "中国银联股份有限公司";
    public static final String UNION_PAY_NAME_SIGN = "00040000:SIGN";


    private static String SIGN_PATH;
    private static String ROOT_PATH;
    private static String MIDDLE_PATH;
    private static String ENCRYPT_PATH;
    private static String KEY_PWD;
    private static boolean isValidateCNName;


    @Value("${union.signCert}")
    public void setSignPath(String signPath) {
        this.SIGN_PATH = signPath;
    }

    @Value("${union.rootCert}")
    public void setRootPath(String rootPath) {
        this.ROOT_PATH = rootPath;
    }

    @Value("${union.middleCert}")
    public void setMiddlePath(String middlePath) {
        this.MIDDLE_PATH = middlePath;
    }

    @Value("${union.encryptCert}")
    public void setEncryptPath(String encryptPath) {
        this.ENCRYPT_PATH = encryptPath;
    }

    @Value("${union.keyPwd}")
    public void setKeyPwd(String keyPwd) {
        this.KEY_PWD = keyPwd;
    }

    @Value("${union.isValidateCNName}")
    public void setIsValidateCNName(boolean isValidateCNName) {
        this.isValidateCNName = isValidateCNName;
    }

    /**
     * 证书容器，存储对商户请求报文签名私钥证书.
     */
    private static KeyStore keyStore = null;
    /**
     * 敏感信息加密公钥证书
     */
    private static X509Certificate encryptCert = null;
    /**
     * 磁道加密公钥
     */
    private static PublicKey encryptTrackKey = null;
    /**
     * 验证银联返回报文签名证书.
     */
    private static X509Certificate validateCert = null;
    /**
     * 验签中级证书
     */
    private static X509Certificate middleCert = null;
    /**
     * 验签根证书
     */
    private static X509Certificate rootCert = null;
    /**
     * 验证银联返回报文签名的公钥证书存储Map.
     */
    private static Map<String, X509Certificate> certMap = new HashMap<String, X509Certificate>();
    /**
     * 商户私钥存储Map
     */
    private final static Map<String, KeyStore> keyStoreMap = new ConcurrentHashMap<String, KeyStore>();


    @PostConstruct
    private void init() {
        try {
            //向系统添加BC provider
            addProvider();
            initSignCert(SIGN_PATH, KEY_PWD);//初始化签名私钥证书
            initMiddleCert(MIDDLE_PATH);//初始化验签证书的中级证书
            initRootCert(ROOT_PATH);//初始化验签证书的根证书
            initEncryptCert(ENCRYPT_PATH);//初始化加密公钥
        } catch (Exception e) {
            logger.error("init失败。（如果是用对称密钥签名的可无视此异常。）", e);
        }
    }

    private static void addProvider() {
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        } else {
            Security.removeProvider("BC"); //解决eclipse调试时tomcat自动重新加载时，BC存在不明原因异常的问题。
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        }
    }


    /**
     * 加载根证书
     *
     * @param inputStream 证书文件流
     */
    public static void initRootCert(InputStream inputStream) {
        if (null != inputStream) {
            rootCert = initCert(inputStream);
            logger.info("[证书工具类]-加载根证书成功.");
        } else {
            logger.info("[证书工具类]-加载根证书失败.证书为空");
        }
    }

    /**
     * 加载根证书
     *
     * @param path 证书文件路径
     */
    public static void initRootCert(String path) {
        if (CharacterUtils.isNotBlank(path)) {
            rootCert = initCert(path);
            logger.info("[证书工具类]-加载根证书成功.");
        } else {
            logger.info("[证书工具类]-加载根证书失败.证书路径为空");
        }
    }

    /**
     * 加载根证书
     *
     * @param inputStream 证书文件流
     */
    public static void initSignCert(InputStream inputStream, String keyPwd) {
        keyStore = getKeyInfo(inputStream, keyPwd, "PKCS12");
        if (null != keyStore) {
            logger.info("[证书工具类]-加载验签证书成功.");
        } else {
            logger.info("[证书工具类]-加载验签证书失败");
        }

    }

    /**
     * 加载签名证书
     *
     * @param path 证书文件路径
     */
    private static void initSignCert(String path, String keyPwd) {
        keyStore = getKeyInfo(path, keyPwd, "PKCS12");
        if (null != keyStore) {
            logger.info("[证书工具类]-加载验签证书成功.");
        } else {
            logger.info("[证书工具类]-加载验签证书失败.");
        }
    }

    /**
     * 加载中级证书
     *
     * @param inputStream 证书文件流
     */
    public static void initMiddleCert(InputStream inputStream) {
        if (null != inputStream) {
            middleCert = initCert(inputStream);
            logger.info("[证书工具类]-加载中级证书成功.");
        } else {
            logger.info("[证书工具类]-加载中级证书失败.证书为空");
        }
    }

    /**
     * 加载中级证书
     *
     * @param path 证书文件路径
     */
    public static void initMiddleCert(String path) {
        if (CharacterUtils.isNotBlank(path)) {
            middleCert = initCert(path);
            logger.info("[证书工具类]-加载中级证书成功.");
        } else {
            logger.info("[证书工具类]-加载中级证书失败.证书路径为空");
        }
    }

    /**
     * 加载敏感信息加密证书
     *
     * @param inputStream 证书文件流
     */
    private static void initEncryptCert(InputStream inputStream) {
        if (null != inputStream) {
            encryptCert = initCert(inputStream);
            logger.info("[证书工具类]-加载敏感信息加密证书成功.");
        } else {
            logger.info("[证书工具类]-加载敏感信息加密证书失败.证书为空");
        }
    }

    /**
     * 加载敏感信息加密证书
     *
     * @param path 证书文件路径
     */
    private static void initEncryptCert(String path) {
        if (CharacterUtils.isNotBlank(path)) {
            encryptCert = initCert(path);
            logger.info("[证书工具类]-加载敏感信息加密证书成功.");
        } else {
            logger.info("[证书工具类]-加载敏感信息加密证书失败.证书路径为空");
        }
    }


    /**
     * 通过证书路径初始化为公钥证书
     *
     * @param inputStream 证书流
     * @return X509 证书
     */
    private static X509Certificate initCert(InputStream inputStream) {
        X509Certificate cert = null;
        CertificateFactory certFactory = null;
        try {
            certFactory = CertificateFactory.getInstance("X.509", "BC");
            cert = (X509Certificate) certFactory.generateCertificate(inputStream);
            // 打印证书加载信息,供测试阶段调试
            if (logger.isDebugEnabled()) {
                logger.debug("[证书编号：{}]", cert.getSerialNumber());
            }
        } catch (Exception e) {
            logger.error("[证书工具类]-加载证书异常.", e);
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("[证书工具类]-加载证书关闭流异常.");
                }
            }
        }
        return cert;
    }

    /**
     * 通过证书路径初始化为公钥证书
     *
     * @param path 证书路径
     * @return X509 证书
     */
    private static X509Certificate initCert(String path) {
        X509Certificate cert = null;
        CertificateFactory certFactory = null;
        FileInputStream in = null;
        try {
            certFactory = CertificateFactory.getInstance("X.509", "BC");
            in = new FileInputStream(path);
            cert = (X509Certificate) certFactory.generateCertificate(in);
            // 打印证书加载信息,供测试阶段调试
            if (logger.isDebugEnabled()) {
                logger.debug("[证书编号：{}]", cert.getSerialNumber());
            }
        } catch (Exception e) {
            logger.error("[证书工具类]-加载证书异常.", e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("[证书工具类]-加载证书关闭流异常.");
                }
            }
        }
        return cert;
    }

    /**
     * 从配置文件acp_sdk.properties中获取验签公钥使用的中级证书
     * @return
     */
    public static X509Certificate getMiddleCert() {
        if (null == middleCert) {
            initMiddleCert(MIDDLE_PATH);
        }
        return middleCert;
    }

    /**
     * 从配置文件acp_sdk.properties中获取验签公钥使用的根证书
     * @return
     */
    public static X509Certificate getRootCert() {
        if (null == rootCert) {
            initRootCert(ROOT_PATH);
        }
        return rootCert;
    }


    /**
     * 通过keyStore 获取私钥签名证书PrivateKey对象
     *
     * @return
     */
    public static PrivateKey getPrivateKey() {
        try {
            Enumeration<String> aliasenum = keyStore.aliases();
            String keyAlias = null;
            if (aliasenum.hasMoreElements()) {
                keyAlias = aliasenum.nextElement();
            }
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias, KEY_PWD.toCharArray());
            return privateKey;
        } catch (Exception e) {
            logger.error("通过keyStore获取私钥签名证书对象异常");
            return null;
        }
    }


    /**
     * 通过keyStore 获取私钥签名证书PrivateKey对象
     *
     * @return
     */
    public static PrivateKey getPrivateKey(String password) {
        try {
            Enumeration<String> aliasenum = keyStore.aliases();
            String keyAlias = null;
            if (aliasenum.hasMoreElements()) {
                keyAlias = aliasenum.nextElement();
            }
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias, password.toCharArray());
            return privateKey;
        } catch (Exception e) {
            logger.error("通过keyStore获取私钥签名证书对象异常");
            return null;
        }
    }

    /**
     * 获取配置文件acp_sdk.properties中配置的签名私钥证书certId
     * 通过keystore获取私钥证书的certId值
     *
     * @return 证书的物理编号
     */
    public static String getCertId() {
        try {
            Enumeration<String> aliasenum = keyStore.aliases();
            String keyAlias = null;
            if (aliasenum.hasMoreElements()) {
                keyAlias = aliasenum.nextElement();
            }
            X509Certificate cert = (X509Certificate) keyStore.getCertificate(keyAlias);
            return cert.getSerialNumber().toString();
        } catch (Exception e) {
            logger.error("[证书工具类]-通过keyStore获取私钥证书的certId值异常.");
            return null;
        }
    }

    /**
     * 将签名私钥证书文件读取为证书存储对象
     *
     * @param path   证书文件
     * @param keyPwd 证书密码
     * @param type   证书类型
     * @return 证书对象
     */
    public static KeyStore getKeyInfo(String path, String keyPwd, String type) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path);
            KeyStore ks = KeyStore.getInstance(type);
            char[] password = null == keyPwd || "".equals(keyPwd.trim()) ? null : keyPwd.toCharArray();
            if (null != ks) {
                ks.load(inputStream, password);
            }
            return ks;
        } catch (Exception e) {
            logger.error("[证书工具类]-签名私钥证书文件读取为证书存储对象异常", e);
            return null;
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("[证书工具类]-签名私钥证书文件读取为证书存储对象关闭流异常", e);
                }
            }
        }
    }

    /**
     * 将签名私钥证书文件读取为证书存储对象
     *
     * @param inputStream 证书文件
     * @param keyPwd      证书密码
     * @param type        证书类型
     * @return 证书对象
     */
    public static KeyStore getKeyInfo(InputStream inputStream, String keyPwd, String type) {
        try {
            KeyStore ks = KeyStore.getInstance(type);
            char[] password = null == keyPwd || "".equals(keyPwd.trim()) ? null : keyPwd.toCharArray();
            if (null != ks) {
                ks.load(inputStream, password);
            }
            return ks;
        } catch (Exception e) {
            logger.error("[证书工具类]-签名私钥证书文件读取为证书存储对象异常", e);
            return null;
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("获取key信息异常", e);
                    logger.error("[证书工具类]-签名私钥证书文件读取为证书存储对象关闭流异常", e);
                }
            }
        }
    }


    /**
     * 将字符串转换为X509Certificate对象.
     *
     * @param x509CertString
     * @return
     */
    public static X509Certificate getCertificate(String x509CertString) {
        X509Certificate x509Cert = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
            InputStream tIn = new ByteArrayInputStream(x509CertString.getBytes("ISO-8859-1"));
            x509Cert = (X509Certificate) cf.generateCertificate(tIn);
        } catch (Exception e) {
            logger.error("[证书工具类]-获取X509Certificate证书对象异常", e);
        }
        return x509Cert;
    }

    /**
     * 获取证书的CN
     * @param certificate
     * @return
     */
    private static String getIdentitiesFromCertficate(X509Certificate certificate) {
        String dn = certificate.getSubjectDN().toString();
        if ((dn != null)) {
            String split[] = dn.substring(dn.indexOf("CN=")).split("@");
            if (split != null && split.length > 2 && split[2] != null)
                return split[2];
        }
        return null;
    }

    /**
     * 检查证书链
     * @param cert 待验证的证书
     */
    public static boolean verifyCertificate(X509Certificate cert) {
        if(!verifyCertificateChain(cert)){
            return false;
        }
        // 验证公钥是否属于银联
        String cnName = CertUtils.getIdentitiesFromCertficate(cert);
        if(isValidateCNName){
            if(!UNION_PAY_NAME.equals(cnName)) {
                logger.error("CER OWNER IS NOT CHINA UNION PAY:", cnName);
                return false;
            }
        }

        if(!UNION_PAY_NAME.equals(cnName) && !UNION_PAY_NAME_SIGN.equals(cnName)) {
            logger.error("CER OWNER IS NOT CHINA UNION PAY:", cnName);
            return false;
        }
        return true;
    }

    /**
     * 验证书链。
     * @param cert
     * @return
     */
    private static boolean verifyCertificateChain(X509Certificate cert){
        if (null == cert) {
            logger.error("证书不能为空");
            return false;
        }
        X509Certificate middleCert = CertUtils.getMiddleCert();
        if (null == middleCert) {
            logger.error("[证书工具类]-中级证书不能为空.");
            return false;
        }

        X509Certificate rootCert = CertUtils.getRootCert();
        if (null == rootCert) {
            logger.error("[证书工具类]-根证书不能为空.");
            return false;
        }

        try {
            cert.checkValidity();//验证有效期
            X509CertSelector selector = new X509CertSelector();
            selector.setCertificate(cert);

            Set<TrustAnchor> trustAnchors = new HashSet<TrustAnchor>();
            trustAnchors.add(new TrustAnchor(rootCert, null));
            PKIXBuilderParameters pkixParams = new PKIXBuilderParameters(trustAnchors, selector);

            Set<X509Certificate> intermediateCerts = new HashSet<X509Certificate>();
            intermediateCerts.add(rootCert);
            intermediateCerts.add(middleCert);
            intermediateCerts.add(cert);

            pkixParams.setRevocationEnabled(false);

            CertStore intermediateCertStore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(intermediateCerts), "BC");
            pkixParams.addCertStore(intermediateCertStore);

            CertPathBuilder builder = CertPathBuilder.getInstance("PKIX", "BC");

            @SuppressWarnings("unused")
            PKIXCertPathBuilderResult result = (PKIXCertPathBuilderResult) builder
                    .build(pkixParams);
            logger.info("验证证书链成功.");
            return true;
        } catch (Exception e) {
            logger.error("验证证书链失败.", e);
        }
        return false;
    }


}
