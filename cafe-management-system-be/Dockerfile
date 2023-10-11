FROM maven:3.8-openjdk-17

WORKDIR /cafe-management-system-be

# Copying necessary files.
COPY pom.xml ./
COPY src ./src

# Command we want to run when our image is executed inside a container
CMD ["mvn", "spring-boot:run", "-Pdocker"]