FROM openjdk:8
ADD target/employeeManagement.jar employeeManagement.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "employeeManagement.jar"]