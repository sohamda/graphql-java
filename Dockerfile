FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=target/graphql-java*.jar
ADD ${JAR_FILE} graphql-java.jar
ENTRYPOINT java -Xshareclasses -Xquickstart -XX:+UseSerialGC -XX:MaxRAM=150m -jar /graphql-java.jar
