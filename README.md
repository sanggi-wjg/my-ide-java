# MY IDE (version. JAVA)


## Development Environment
* Java 17
* Spring boot 3.0.2
* JPA Hibernate 6.1.6


## Install & Start
* First of all, Install docker and docker-compose on your server or local.
* Execute docker-compose command. 
```shell
docker-compose -f src/resources/container/docker-compose.yml up -d
```
* After Gradle build, Execute MainApplication(MyIdeJavaApplication) with `local` as active profile.


## Usage
### Swagger
* http://localhost:9000/swagger-ui/index.html

![](.README_images/1241e6dc.png)

### Local Test view
* local 환경에서 코드 실행 API 테스트
  * test/resources/test_view.html

![](.README_images/faf642de.png)
![](.README_images/a4616146.png)


### GitHub action script test using act
* https://github.com/nektos/act
```shell
act -l
act --container-architecture linux/amd64
```

### Ref
* JWT 구현 참조
  * https://medium.com/geekculture/implementing-json-web-token-jwt-authentication-using-spring-security-detailed-walkthrough-1ac480a8d970