plugins {
    id("java")
}

group = "io.leiwang.kafka"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation("org.apache.kafka:kafka-clients:3.6.1")
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("org.slf4j:slf4j-simple:2.0.12")
    implementation(project(":kafka-basics"))

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation("com.launchdarkly:okhttp-eventsource:4.1.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
