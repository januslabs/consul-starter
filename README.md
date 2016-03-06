<a href='https://bintray.com/nandhusriram/consul-starter/consul-starter/_latestVersion'><img src='https://api.bintray.com/packages/nandhusriram/consul-starter/consul-starter/images/download.svg'></a>


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

The easiest way to get started with this starter project is to fork, clone or download from the link below.

git clone https://github.com/januslabs/consul-starter.git 

You will also need to install Consul. This starter project assumes that you have a basic understanding of Consul's operations.

https://www.consul.io/intro/getting-started/install.html

Bintray:

Grab the latest binary (0.0.1) [here] (https://bintray.com/artifact/download/nandhusriram/consul-starter/org/januslabs/consul-starter/0.0.1/consul-starter-0.0.1.jar).

Building
==========
Java Version: 1.8

Basic Compile and Test
======================
$ mvn clean install

###Maven:

```xml

<dependencies>
    <dependency>
     	<groupId>org.januslabs</groupId>
		<artifactId>consul-starter</artifactId>
		<version>0.0.1</version>
    </dependency>
    <!-- include spring boot,Jersey, Orbitzworldwide consul client -->
</dependencies>

<repositories>
    <repository>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
        <id>central</id>
        <name>bintray</name>
        <url>http://jcenter.bintray.com</url>
    </repository>
</repositories>
```
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




