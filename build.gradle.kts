val pluginName: String by project
val pluginId: String by project
val pluginGroup: String by project
val pluginLicense: String by project
val pluginVersion: String by project
val pluginAuthors: String by project
val pluginDescription: String by project
val pluginRepository: String by project
val minecraftVersion: String by project

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("xyz.jpenilla.run-paper") version "2.2.0"
    id("maven-publish")
}

group = pluginGroup
version = pluginVersion

repositories {
    maven() {
        url = uri("https://maven.aliyun.com/repository/public/")
    }
    maven() {
        url = uri("https://maven.aliyun.com/repository/spring/")
    }
    maven("papermc-repo") {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven("sonatype") {
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    mavenLocal()
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")

    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

tasks.jar {
    manifest {
        attributes(
                "Implementation-Title" to pluginName,
                "Implementation-Version" to pluginVersion
        )
    }
}

tasks.processResources {
    val props = mapOf(
            "plugin_name" to pluginName,
            "plugin_group" to pluginGroup,
            "plugin_version" to pluginVersion,
            "plugin_authors" to pluginAuthors,
            "plugin_description" to pluginDescription
    )
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks {
    shadowJar {
        minimize()
    }
    runServer {
        downloadPlugins {
//            hangar("PlaceholderAPI", "2.11.5")
//            github("PlaceholderAPI", "PlaceholderAPI", "2.11.4", "PlaceholderAPI-2.11.4.jar")
//            url("https://github.com/PlaceholderAPI/PlaceholderAPI/releases/download/2.11.4/PlaceholderAPI-2.11.4.jar")
//            url("https://repo.extendedclip.com/content/repositories/placeholderapi/me/clip/placeholderapi/2.11.5/placeholderapi-2.11.5.jar")
        }
        minecraftVersion(minecraftVersion)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifactId = pluginId
            pom {
                name.set(pluginName)
                description.set(pluginDescription)
                url.set(pluginRepository)
                licenses {
                    license {
                        name.set(pluginLicense)
                        url.set(pluginRepository)
                    }
                }
            }
        }
    }
}
