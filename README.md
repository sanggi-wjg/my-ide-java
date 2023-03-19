# MY IDE (version. JAVA)


## Development Environment
* Java 17
* Spring boot 3.0.2
* JPA Hibernate 6.1.6


## Install
### First of all, Install docker and docker-compose.

### Execute docker-compose command. 
```shell
docker-compose -f src/resources/container/docker-compose.yml up -d
```

### Then, start project


## Usage
### Swagger
* http://localhost:9000/swagger-ui/index.html

### Local Test view
* local 환경에서 코드 실행 API 테스트
  * test/resources/test_view.html

### GitHub action script test using act
* https://github.com/nektos/act
```shell
act -l
act --container-architecture linux/amd64
```

### Ref
* JWT 구현 참조
  * https://medium.com/geekculture/implementing-json-web-token-jwt-authentication-using-spring-security-detailed-walkthrough-1ac480a8d970