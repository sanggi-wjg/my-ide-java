# MY IDE (version. JAVA)


## Development Environment
* Java 17
* Spring boot 3.0.2
* JPA Hibernate 6.1.6


## Install
* First of all, Install docker and docker-compose
```shell
# Create Symbolic link `docker.sock` to root share directory
cd ~ && mkdir share/
ln -s /var/run/docker.sock ~/share/docker.sock

./gradlew build

docker build -t my-ide .
docker run -d -p 9000:9000 --name my-ide  my-ide -v ~/share:/var/run

# remove
docker rm my-ide && docker rmi my-ide
```


## Swagger
```shell
http://localhost:9000/swagger-ui/index.html
```


## Local Test
* local 환경에서 코드 실행 API 테스트
```shell
test/resources/test_view.html
```


### Ref
* JWT 구현 참조
  * https://medium.com/geekculture/implementing-json-web-token-jwt-authentication-using-spring-security-detailed-walkthrough-1ac480a8d970