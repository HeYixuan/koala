package org.igetwell.union.service.impl;

import org.igetwell.common.uitls.CharacterUtils;
import org.igetwell.union.service.impl.unionpay.acp.sdk.SDKConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 证书工具类，主要用于对证书的加载和使用
 * 声明：以下代码只是为了方便接入方测试而提供的样例代码，
 * 商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障
 */
public class CertUtils {

    protected static final Logger logger = LoggerFactory.getLogger(CertUtils.class);
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

    static {
        init();
    }

    @PostConstruct
    private static void init() {
        try {
            addProvider();//向系统添加BC provider
            initSignCert("D:/DevelopTools/acp_test_sign.pfx");//初始化签名私钥证书
            initMiddleCert("D:/DevelopTools/acp_test_middle.cer");//初始化验签证书的中级证书
            initRootCert("D:/DevelopTools/acp_test_root.cer");//初始化验签证书的根证书
            initEncryptCert("D:/DevelopTools/acp_test_enc.cer");//初始化加密公钥
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
            logger.info("[证书工具类]-加载验签根证书成功.");
        } else {
            logger.info("[证书工具类]-加载验签根证书失败.证书为空");
        }
    }

    /**
     * 加载根证书
     *
     * @param path 证书文件路径
     */
    public static void initRootCert(String path) {
        if (CharacterUtils.isBlank(path)) {
            rootCert = initCert(path);
            logger.info("[证书工具类]-加载验签根证书成功.");
        } else {
            logger.info("[证书工具类]-加载验签根证书失败.证书路径为空");
        }
    }

    /**
     * 加载根证书
     *
     * @param inputStream 证书文件流
     */
    public static void initSignCert(InputStream inputStream) {
        if (null != keyStore) {
            keyStore = null;
        }
        keyStore = getKeyInfo(inputStream, "000000", "PKCS12");
        logger.info("[证书工具类]-加载验签根证书成功.");


    }

    /**
     * 加载签名证书
     *
     * @param path 证书文件路径
     */
    private static void initSignCert(String path) {
        if (null != keyStore) {
            keyStore = null;
        }
        keyStore = getKeyInfo(path, "000000", "PKCS12");
        logger.info("[证书工具类]-加载验签根证书成功.");
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
        if (CharacterUtils.isBlank(path)) {
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
        if (CharacterUtils.isBlank(path)) {
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
            if (logger.isDebugEnabled()) {
                logger.warn("Load RSA CertPath,Pwd=[" + keyPwd + "],type=[" + type + "]");
            }
            char[] password = null;
            password = null == keyPwd || "".equals(keyPwd.trim()) ? null : keyPwd.toCharArray();
            if (null != ks) {
                ks.load(inputStream, password);
            }
            return ks;
        } catch (Exception e) {
            logger.error("获取key信息异常", e);
            return null;
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("获取key信息异常", e);
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
            if (logger.isDebugEnabled()) {
                logger.warn("Load RSA CertPath,Pwd=[" + keyPwd + "],type=[" + type + "]");
            }
            char[] password = null;
            password = null == keyPwd || "".equals(keyPwd.trim()) ? null : keyPwd.toCharArray();
            if (null != ks) {
                ks.load(inputStream, password);
            }
            return ks;
        } catch (Exception e) {
            logger.error("获取key信息异常", e);
            return null;
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("获取key信息异常", e);
                }
            }
        }
    }


}
