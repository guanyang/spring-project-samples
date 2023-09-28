FROM amazoncorretto:21-alpine3.18-jdk

VOLUME /tmp

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} ${JAVA_AGENT} -jar app.jar ${0} ${@}"]