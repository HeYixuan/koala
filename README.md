# koala
koala是基于spring cloud 开发的spring cloud数据权限框架



授权码模式自动授权：
<br/>
第一步：http://localhost:9001/oauth/authorize?client_id=test&response_type=code&redirect_uri=https://www.baidu.com/
<br/>跳转登录页 输入用户名：admin 123456 
登录成功拿到授权码输入(注意：redirect_uri地址和数据库的地址要一致，不然会导致url不匹配)：

第二步：http://localhost:9001/oauth/token?grant_type=authorization_code&code=HklLOx&client_id=test&client_secret=test&redirect_uri=https://www.baidu.com/

校验token:
客户端ID：test 密钥:test (Base64加密)<br/>
请求头：Authorization:Basic dGVzdDp0ZXN0 (Base64加密)
http://localhost:9001/oauth/check_token?token=b78a15ce-0faa-4ee8-95d5-433e121b5a2a

手动授权 用户名：admin 123456
<br/>
http://localhost:9001/oauth/authorize?client_id=test2&response_type=code&redirect_uri=https://www.baidu.com/


1.使用了Feign增强,不需要手动写Feign实现,只需要写Controller的Mapping映射和Feign的映射一致
2.网关
3.登陆密码需要传入AES加密过的密码,因为会走网关密码拦截器拦截。<br/>
3.1：AES加密链接:http://tool.chacuo.net/cryptaes <br/>
加密模式：CBC  
填充：ZERO_PADDING  <br/>
密码：0oYL0SBdNjXm8DkQ  <br/>
偏移量：0oYL0SBdNjXm8DkQ <br/>
输出:Base64  <br/>
加密明文：123456  <br/>
加密后密文：Y3rzvgYsYvBBLGjOCl4jTg==  <br/>
密文urlEncode后字符串是：Y3rzvgYsYvBBLGjOCl4jTg%3D%3D  <br/>


通过网关访问登录
http://localhost:9999/oauth/oauth/token?username=admin2&password=Y3rzvgYsYvBBLGjOCl4jTg%3D%3D&grant_type=password&scope=all
<br/>携带请求头
Authorization: Basic dGVzdDI6dGVzdDI=
<br/>
TENANT-ID: 000000
注：TENANT-ID 默认000000

获取到token后请求
http://localhost:9999/system/systemUser/getList
<br/>携带请求头
Authorization: Bearer 6265b580-f096-4826-ab2d-b61b45cb19a6

