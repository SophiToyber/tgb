FROM openjdk:11

COPY build/libs/*.jar telegram.jar

EXPOSE 8086 8006

ENTRYPOINT [ "java", "-jar", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8006", "telegram.jar"]
