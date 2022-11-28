FROM eclipse-temurin:17
RUN mkdir /opt/app
COPY MockMock/release/MockMock.jar /opt/app
CMD ["java", "-jar", "/opt/app/MockMock.jar", "-p", "25000", "-h", "8282"]