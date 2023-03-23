## Docker build test

```shell
docker build -t php-7.4 .
docker run -d --name php-7.4 php-7.4
docker stop php-7.4 && docker rm php-7.4
```


## Docker container test code execution

```shell
docker cp test.php php-7.4:/app
docker exec php-7.4 php /app/test.php
```

