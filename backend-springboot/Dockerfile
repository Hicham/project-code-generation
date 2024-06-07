FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install openjdk-21-jdk -y
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean install -U
EXPOSE 8080
ENTRYPOINT ["./mvnw", "spring-boot:run"]


#FROM ubuntu:latest
#
#RUN apt-get update && apt-get install openjdk-21-jdk -y
#
#COPY . .
#
## Add this line to set execute permissions on the mvnw script
#RUN chmod +x mvnw
#
#RUN ./mvnw clean install -U
#
#EXPOSE 8080
#
#ENTRYPOINT ["./mvnw", "spring-boot:run"]
