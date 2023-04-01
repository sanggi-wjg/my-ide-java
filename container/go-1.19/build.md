## doDocker build test

```shell
docker build -t container-go-1.19 .
docker run -d --name container-go-1.19 container-go-1.19
docker stop container-go-1.19 && docker rm container-go-1.19
```


## Docker container test code execution

```shell
docker cp test.go container-go-1.19:/app
docker exec container-go-1.19 go run /app/test.go
```

