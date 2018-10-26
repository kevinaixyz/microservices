# microservices
This is a collection of microservice that is implemented by SpringBoot.
- edge-server: provide the router service
- eureka-server: provide service registrtion
- common: provide lib for common usages like wrapper of request and response json
- cms-service: the implementation of simple content management service
- etl-service: provide the simple ETL function to handle CSV, TXT, Excel files by web request and schedule task

## Requirements
Maven, Java IDE  
The edge-server, eureka-server are mandatory as they are the basic service for microservice architecture.  
The common lib is used in other business related service.

## Installation
Clone the projects to local.  
1. edge-server
- Open the terminal and cd to the project directory.
- Run package.bat in terminal. This is to build the pakage for runing
- Run runapp-dev-9906.bat
2. eureka-server
Follow the above step1.  
3. common
- Open the terminal and cd to the project directory.
- Run package.bat in terminal. This is to build the pakage for other project reference.
- The successfully built packaeg(prototype-commons-1.0.0-RELEASE.jar) can be found in the target folder under the same directory.
4. For business related projects(e.g. cms-service and etl-service)
- cd to ~\cms-service\src\main\resources.
- Put the prototype-commons-1.0.0-RELEASE.jar built in common to this directory.
- Run install-common.bat to install the common lib in maven.
- cd to ~\cms-service\ and Run runapp-dev-9102.bat

## Issues
The cms-service and etl-service are still in the development phase.