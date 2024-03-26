pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
            credentials { username = "jp_2c5mu6fnqbihhee0j2dblbe20k" }
        }
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
            credentials { username = "jp_2c5mu6fnqbihhee0j2dblbe20k" }
        }
        gradlePluginPortal()
    }
}

rootProject.name = "Countdown"
include(":app")
include(":finalcountdown")
