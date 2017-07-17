The following Spring projects are used in this sample app:
Spring Boot Version : 1.5.3.RELEASE
* http://projects.spring.io/spring-boot/[Spring Boot]
* http://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html[Spring MVC]
* http://projects.spring.io/spring-security/[Spring Security]
* http://projects.spring.io/spring-security-oauth/[Spring Security OAuth]
* http://projects.spring.io/spring-data-jpa/[Spring Data JPA]

###module: memory
#### 该工程的认证使用的是内存数据(只是demo使用)
通过mvn将工程引入IDE,
修改maven的conf.xml,加入阿里云maven仓,这样下载工程所需的jar就会快很多:
```xml
<mirrors>
<!--阿里云仓库 -->
    <mirror>
        <id>alimaven</id>
        <mirrorOf>central</mirrorOf>
        <name>aliyun maven</name>
        <url>http://maven.aliyun.com/nexus/content/repositories/central/</url>
    </mirror>	
  </mirrors>
```
application.yml文件可以激活不同的环境：
```yaml
spring:
  profiles:
    active: dev #dev #prod
```
filter配置，非常重要，在spring boot 版本1.5.3.RELEASE的时候，不加这个配置的话,只能获取到token，
其他访问带入token也不能正常访问：
```yaml
security:
    oauth2:
        resource:
            filter-order: 3
```
application-dev.yml,application-prod.yml分别设置不同的端口，以此测试application.yml是否可以激活不同的环境：
```yaml
server:
  port: 8082
```
运行项目
```sh
mvn springboot:run
```
访问
```sh
curl http://localhost:8082/greeting
```
该请求需要认证，所以会得到：
```json
{
  "error": "unauthorized",
  "error_description": "An Authentication object was not found in the SecurityContext"
}
```
下面的请求无需认证，可以得到结果:
```sh
curl http://localhost:8082/hello
```
```json
{"id":1,"content":"Hello, hello!"}
```
获取授权码,可以通过curl命令，(password=memorypassword&username=memory对应WebSecurityConfig设置的认证)
,(memoryclient:123456对应OAuth2Configuration的客户端认证)
```sh
curl -X POST -vu memoryclient:123456 http://localhost:8082/oauth/token -H "Accept: application/json" -d "password=memorypassword&username=memory&grant_type=password&scope=read%20write&client_secret=123456&client_id=memoryclient"
```
得到json
```json
{
  "access_token": "6f453ba0-a8cf-4581-aabb-a9b0a29da04f",
  "token_type": "bearer",
  "refresh_token": "eeda9c5b-4cc1-495b-b355-0a42a2e4d02e",
  "expires_in": 43199,
  "scope": "read write"
}
```
然后根据token 去访问需要认证的请求:
```sh 
curl http://localhost:8082/greeting -H "Authorization: Bearer 6f453ba0-a8cf-4581-aabb-a9b0a29da04f"
```
得到json
```json
{"id":2,"content":"Hello, greeting!"}
```


###module: store
在上面的例子memory中，所有的token信息都是保存在内存中的，这显然无法在生产环境中使用(进程结束后所有token丢失, 用户需要重新授权)，因此我们需要将这些信息进行持久化操作。 
把授权服务器中的数据存储到数据库中并不难，因为 Spring Security OAuth 已经为我们设计好了一套Schema和对应的DAO对象。
框架为我们提前设计好了schema, 在gitHub上：https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/resources/schema.sql

在使用这套表结构之前要注意的是，对于MySQL来说，默认建表语句中主键是varchar(255)类型，在mysql中执行会报错，原因是mysql对varchar主键长度有限制。
如果不修改，则不能讲Id设置为主键,这里改成128就好了。其次，语句中会有某些字段为LONGVARBINARY类型，修改为MySql对应的blob类型.
下面这套sql是在上面的基础上修改的:
* 查看工程目录下的建表语句: resources/oauth2.sql;
* 根据如下SQL：sys.sql,建立一张用户表进行测试使用.

Spring Security OAuth相关接口说明:
DefaultTokenServices  token生成、过期等 OAuth2 标准规定的业务逻辑,通过TokenStore接口完成对生成数据的持久化。
ClientDetailsService 接口负责从存储仓库中读取数据.
要想使用数据库存储，只需要提供这些接口的实现类即可。框架已经为我们写好JDBC实现JdbcTokenStore和JdbcClientDetailsService。

配置JdbcTokenStore:
```java
@Component
public class Auth2JdbcTokenStore extends JdbcTokenStore {
    @Autowired//构造类注入
    public Auth2JdbcTokenStore(DataSource dataSource) {
        super(dataSource);
    }
}
```
配置 JdbcClientDetailsService:
```java
@Component
public class Auth2JdbcClientDetailsService extends JdbcClientDetailsService {
    @Autowired
    public Auth2JdbcClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }
}
```
