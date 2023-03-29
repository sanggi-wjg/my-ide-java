# MY IDE (version. JAVA)

[![✅ Build and Test](https://github.com/sanggi-wjg/my-ide-java/actions/workflows/build-test-main.yml/badge.svg?branch=main)](https://github.com/sanggi-wjg/my-ide-java/actions/workflows/build-test-main.yml)
[![CodeFactor](https://www.codefactor.io/repository/github/sanggi-wjg/my-ide-java/badge)](https://www.codefactor.io/repository/github/sanggi-wjg/my-ide-java)
[![codecov](https://codecov.io/gh/sanggi-wjg/my-ide-java/branch/main/graph/badge.svg?token=8NSYRJXPMS)](https://codecov.io/gh/sanggi-wjg/my-ide-java)

### Codecov coverage
<img src="https://codecov.io/gh/sanggi-wjg/my-ide-java/branch/main/graphs/tree.svg?token=8NSYRJXPMS" alt="">


## Development Environment
![Java](https://img.shields.io/badge/Java-34495E.svg?style=for-the-badge&logo=Java&logoColor=white)
![SpringBoot](https://img.shields.io/badge/SpringBoot-76b44d.svg?style=for-the-badge&logo=SpringBoot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-206188.svg?style=for-the-badge&logo=MySQL&logoColor=white)

```shell
Java 17 (corretto)
Spring boot 3.0.2
JPA Hibernate 6.1.6
```


## TODO
* [ ] Kafka 연동 추가 작업 및 리팩토링
* [ ] Redis는 할까? 말까? 할까? 말까?
* [ ] Gradle 멀티 모듈 변경


## Install & Start
* To begin, Install Docker and Docker Compose on your server or local machine.
* Once installed successfully, run the below docker-compose command.
```shell
docker-compose --env-file .env.docker up -d
```
* After Gradle build, execute MainApplication(MyIdeJavaApplication) with the `local` profile as Spring Boot active profiles.


## Concept Description 
I wanted to explore different ways of executing actual code in Docker containers.  
So I proceeded with three ways.


### Python 3.8
Implement using an HTTP request with container.

```mermaid
sequenceDiagram
  box Client
  participant SpringBoot
  participant DockerClient
  end
  
  box Docker Container
  participant Container
  end
  
  SpringBoot->>DockerClient: Transfer Code 
  DockerClient->>Container: HTTP Post Request to Flask route
  Container->>DockerClient: Run a Code then respond result whatever success or failure as HTTP Response
  DockerClient->>SpringBoot: Transfer result
```


### Python 2.7
Run a file, that is already implemented with a queue, on container.

```mermaid
sequenceDiagram
  box Client
  participant SpringBoot
  participant DockerClient
  end
  
  box Docker Container
  participant Container
  end
  
  SpringBoot->>DockerClient: Transfer Code 
  DockerClient->>Container: Container file로 전달 받은 Code를 arguments로 하여 파일 실행, Read Queue에 전달
  Container->>DockerClient: Code를 실행하고 성공 or 실패한 출력 결과 Writer Queue가 출력
  DockerClient->>SpringBoot: Transfer result
```


### PHP 8.2, PHP 7.4, GCC 4.9
After create temp file, transfer the file to container.  
Then, run a file on container.

```mermaid
sequenceDiagram
  box Client
  participant SpringBoot
  participant DockerClient
  end
  
  box Docker Container
  participant Container
  end
  
  SpringBoot->>DockerClient: Transfer Code 
  DockerClient->>Container: 임시 파일을 생성하고 Container로 생성한 파일 전달
  Container->>DockerClient: 전달 받은 파일을 실행하고 성공 or 실패한 출력 결과 출력
  DockerClient->>SpringBoot: Transfer result
```



## Usage
### Swagger
* http://localhost:9000/swagger-ui/index.html

![](.README_images/1241e6dc.png)


### Kafka UI
* http://localhost:8081/

![](.README_images/bbb374b1.png)
![](.README_images/a70674d0.png)


### Jacoco
* gradle > Tasks > verification > test `(For Result of Test)`
  * build > reports > tests > index.html
* gradle > Tasks > verification > jacocoTestReport `(For Coverage)`
  * build > jacoco > test > html  > index.html

### Codecov
* https://app.codecov.io/gh/sanggi-wjg/my-ide-java
  * ref
    * https://about.codecov.io/blog/setting-up-codecov-with-java-and-gradle/
    * https://jane514.tistory.com/12


### Local Test view
* Test viewfile for API Test on local
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
