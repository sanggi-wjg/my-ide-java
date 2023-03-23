## doDocker build test

```shell
docker build -t gcc-4.9 .
docker run -d --name gcc-4.9 gcc-4.9
docker stop gcc-4.9 && docker rm gcc-4.9
```


## Docker container test code execution

```shell
docker cp test.c gcc-4.9:/app
docker exec gcc-4.9 gcc -o /app/Test /app/test.c
docker exec gcc-4.9 /app/Test
```

