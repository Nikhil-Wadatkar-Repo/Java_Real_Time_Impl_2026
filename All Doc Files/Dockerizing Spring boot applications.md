1. create or pull existing MYSQL image from registry. 
2. create the database & table
(Step 1 and 2 given below)

3. dockerize the spring boot applications or Rest API
    create a docker file named by Dockerfile in porject folder for each files and add below content. Check service names

FROM eclipse-temurin:17-jdk-jammy
ADD target/couponservice-0.0.1-SNAPSHOT.jar couponservice-0.0.1-SNAPSHOT.jar
ENTRYPOINT [ "java","-jar","couponservice-0.0.1-SNAPSHOT.jar" ]

    
4. build docker images (run below command by going to folder of where docker file is present)
    dockr build -f Dockerfile -t product_app .
    dockr build -f Dockerfile -t coupon_app .

5. Running the containers
Here first run mysql container because other services are dependent on it. 
docker run mysql

5.1) we have to start coupon service and link it to mysql.
docker run -t --name=coupon-app --link docker-mysql:mysql -p 10555:9091 coupon_app
5.2)start roducr service by linking it to mysql
docker run -t --link docker-mysql:mysql -p 10666:9090 product_app
docker run -t --link docker-mysql:mysql --link coupon-app:coupon_app -p 10666:9090 product_app

6) Testing:
http://localhost:10555/couponapiapi
http://localhost:10666/productapi

7) Pushing images to Central docker repository (Docker Hub)
Go to hub.docker.com -> Sign In

First give or assign a tag name to the images and then push them to Docker Hub
Creating tag name
docker tag product_app bharath19/prodctservice
docker tag coupon_app bharath19/couponservice

pushing to docker hub
docker push bharath19/prodctservice
docker push bharath19/couponservice

===================================================================
===================================================================
1. create or pull existing MYSQL image from registry.
-----------------------------------------------------------    
#pull mysql image first
docker pull mysql

#see whether image is pulled or not
docker images

#to start mysql in docker, you can use the following command:
docker run -d -p 6666:3306 --name=docker-mysql --env="MYSQL_ROOT_PASSWORD=test1234" --env="MYSQL_DATABASE=mydb" mysql

#to execute mysql in a docker itself
docker exec -it docker-mysql bash

mysql -uroot -p
give password as => test1234

Now you are in container's DB 
mysql> show databases;
mysql> show tables;


2. create the database & table
-----------------------------------------------
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



