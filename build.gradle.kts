plugins {
    java
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
}

group = "org.easylauncher.modules"
version = "1.0.0"

val lwjglVersion = "3.3.4"
val jomlVersion = "1.10.7"
val lwjglNatives = "natives-windows"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))
    implementation("com.huskerdev:openglfx:4.1.1")
    implementation("com.huskerdev:openglfx-lwjgl:4.1.1")

    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-stb")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)
    implementation("org.joml", "joml", jomlVersion)

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
}