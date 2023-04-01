## doDocker build test

```shell
docker build -t container-jdk-11 .
docker run -d --name container-jdk-11 container-jdk-11
docker stop container-jdk-11 && docker rm container-jdk-11
```


## Docker container test code execution

```shell
docker cp Test.java container-jdk-11:/app
docker exec container-jdk-11 javac /app/Test.java
docker exec container-jdk-11 java Test
```

