# Dockerizing Spring Boot Applications with MySQL

## 1. Create or Pull Existing MySQL Image from Registry

```bash
# Pull mysql image first
docker pull mysql

# See whether image is pulled or not
docker images

# To start mysql in docker, use the following command:
docker run -d -p 6666:3306 --name=docker-mysql --env="MYSQL_ROOT_PASSWORD=test1234" --env="MYSQL_DATABASE=mydb" mysql

# To execute mysql in the docker container itself
docker exec -it docker-mysql bash

mysql -uroot -p
# give password as => test1234
```

Now you are in the container's DB:

```sql
show databases;
show tables;
```

## 2. Create the Database & Table

```sql
use mydb;

create table product(
    id int AUTO_INCREMENT PRIMARY KEY,
    name varchar(20),
    description varchar(100),
    price decimal(8,3)
);

create table coupon(
    id int AUTO_INCREMENT PRIMARY KEY,
    code varchar(20) UNIQUE,
    discount decimal(8,3),
    exp_date varchar(100)
);

select * from product;
select * from coupon;
```

## 3. Dockerize the Spring Boot Applications / REST APIs

Create a `Dockerfile` in the project folder for each service and add the content below.
**Check service names** before building.

```dockerfile
FROM eclipse-temurin:17-jdk-jammy
ADD target/couponservice-0.0.1-SNAPSHOT.jar couponservice-0.0.1-SNAPSHOT.jar
ENTRYPOINT [ "java","-jar","couponservice-0.0.1-SNAPSHOT.jar" ]
```

## 4. Build Docker Images

Run the below commands from the folder where the Dockerfile is present:

```bash
docker build -f Dockerfile -t product_app .
docker build -f Dockerfile -t coupon_app .
```

## 5. Running the Containers

Run the MySQL container first, since the other services depend on it.

```bash
docker run mysql
```

### 5.1 Start the Coupon Service and Link it to MySQL

```bash
docker run -t --name=coupon-app --link docker-mysql:mysql -p 10555:9091 coupon_app
```

### 5.2 Start the Product Service and Link it to MySQL

```bash
docker run -t --link docker-mysql:mysql -p 10666:9090 product_app

docker run -t --link docker-mysql:mysql --link coupon-app:coupon_app -p 10666:9090 product_app
```

## 6. Testing

- Coupon API: [http://localhost:10555/couponapiapi](http://localhost:10555/couponapiapi)
- Product API: [http://localhost:10666/productapi](http://localhost:10666/productapi)

## 7. Pushing Images to Central Docker Repository (Docker Hub)

1. Go to [hub.docker.com](https://hub.docker.com) and sign in.
2. Assign a tag name to the images, then push them to Docker Hub.

### Creating a Tag Name

```bash
docker tag product_app bharath19/prodctservice
docker tag coupon_app bharath19/couponservice
```

### Pushing to Docker Hub

```bash
docker push bharath19/prodctservice
docker push bharath19/couponservice
```
