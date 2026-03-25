plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.taxmate"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot 핵심
    implementation("org.springframework.boot:spring-boot-starter-web")        // REST API 서버
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")   // DB 접근 (JPA)
    implementation("org.springframework.boot:spring-boot-starter-data-redis") // Redis
    implementation("org.springframework.boot:spring-boot-starter-validation") // 입력 검증 (@Valid)
    implementation("org.springframework.boot:spring-boot-starter-security")   // 인증/인가

    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")      // JSON 직렬화
    implementation("org.jetbrains.kotlin:kotlin-reflect")                     // 리플렉션

    // DB
    runtimeOnly("com.mysql:mysql-connector-j")                               // MySQL 드라이버

    // JWT (토큰 기반 인증)
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // 비밀번호 암호화 (Spring Security에 포함되어 있지만 명시적 선언)
    // BCryptPasswordEncoder는 spring-boot-starter-security에 포함

    // Swagger (API 문서 자동 생성)
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")

    // 테스트
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
