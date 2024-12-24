plugins {
    id("java")
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.springframework.boot") version "3.4.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Spring
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
}

dependencies{
    implementation ("com.mysql:mysql-connector-j:9.1.0")
}

dependencies {
    compileOnly ("org.projectlombok:lombok:1.18.36")
    testCompileOnly ("org.projectlombok:lombok:1.18.36")
    annotationProcessor ("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor ("org.projectlombok:lombok:1.18.36")
}


tasks.test {
    useJUnitPlatform()
}