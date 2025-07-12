FROM amazoncorretto:21-al2-jdk

WORKDIR /app

COPY build/libs/task_manager_app.jar /app/task_manager_app.jar

ENTRYPOINT ["java", "-jar", "/app/task_manager_app.jar"]

EXPOSE 8080