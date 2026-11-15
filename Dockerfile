FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /workspace

COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew
RUN ./gradlew --no-daemon dependencies

COPY src ./src
RUN ./gradlew --no-daemon clean bootJar

FROM eclipse-temurin:21-jre-jammy AS extractor
WORKDIR /extracted
COPY --from=build /workspace/build/libs/*.jar app.jar
RUN java -Djarmode=tools -jar app.jar extract --layers --launcher

FROM eclipse-temurin:21-jre-jammy
WORKDIR /application

RUN useradd --no-create-home --shell /usr/sbin/nologin appuser
COPY --from=extractor /extracted/app/dependencies/ ./
COPY --from=extractor /extracted/app/spring-boot-loader/ ./
COPY --from=extractor /extracted/app/snapshot-dependencies/ ./
COPY --from=extractor /extracted/app/application/ ./
USER appuser

EXPOSE 8082
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
