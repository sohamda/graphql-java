FROM adoptopenjdk/openjdk11-openj9:alpine-slim
ARG JAR_FILE=target/graphql-java*.jar
ADD ${JAR_FILE} graphql-java.jar
ENTRYPOINT java -Xshareclasses -Xquickstart -XX:+UseSerialGC -XX:MaxRAM=150m -jar /graphql-java.jar
