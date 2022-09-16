plugins {
    `java-library`
    `maven-publish`
}

gradle.buildFinished {
	project.buildDir.deleteRecursively()
}

allprojects {
	group = rootProject.group
	version = rootProject.version
	description = rootProject.description
}

subprojects {
	apply(plugin = "java-library")
	apply(plugin = "maven-publish")

	java {
		toolchain.languageVersion.set(JavaLanguageVersion.of(17))
		withJavadocJar()
		withSourcesJar()
	}

	tasks {
		compileJava {
			options.encoding = Charsets.UTF_8.name()
			options.release.set(17)
		}
		processResources {
			filteringCharset = Charsets.UTF_8.name()
			filesMatching("**/plugin.yml") {
				expand( project.properties )
			}
		}
	}

	repositories {
		mavenCentral()
        maven("https://repo.codemc.io/repository/maven-public/")
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.maven.apache.org/maven2/")
        maven("https://repo.aikar.co/content/groups/aikar/")
        maven("https://jitpack.io")
	}

	publishing {
		repositories {
			maven {
				url = uri("https://nexus.civunion.com/repository/maven-releases/")
				credentials {
					username = System.getenv("REPO_USERNAME")
					password = System.getenv("REPO_PASSWORD")
				}
			}
		}
		publications {
			register<MavenPublication>("main") {
				from(components["java"])
			}
		}
	}
}
