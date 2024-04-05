plugins {
    `java`
    `application`
    id( "com.google.cloud.tools.jib") version ("3.0.0")
}

group = "org.example"
version = "1.0-SNAPSHOT"

val vertxVersion = "4.5.7"

application {
    mainClass.set("org.example.Main")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.vertx:vertx-web:$vertxVersion")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

jib {
    from {
        image = "adoptopenjdk:openj9"
    }
    to {
        image = "phipex/${name}:${version}"
    }
    container {
        mainClass= "org.example.Main"

    }
}