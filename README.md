Consul Starter for your services
================================
consul registration  and discovery java client using spring cloud commons, spring boot and Consul client from OrbitzWorldwide to run on your enterprise servers. Starter auto register your service with consul,running locally or remotely as specified in the application.properties.

TODO: Retrieve the server port and application name from the server(Tomcat) directly , instead of application.properties

Required Frameworks
===================
Spring boot
Jersey Jax-RS
Consul-Client

Installation
=============
https://www.consul.io/intro/getting-started/install.html

Building
==========
Java Version: 1.8

Basic Compile and Test
======================
$ mvn clean install

Configuration
=============
Please add the consul-starter jar to your api/service application, update the application properties with url , appname , server port and context path.

consul.host=localhost  
consul.port: 8500  
or
consul.url : http://localhost:8500
spring.application.name=mygreat-api-service
server.port=8200
server.context-path=/apis





