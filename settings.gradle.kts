pluginManagement {
    repositories {
        google()           // 🔑 Needed for Android Gradle Plugin
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()           // 🔑 Needed for Firebase + Android libs
        mavenCentral()
    }
}

rootProject.name = "TeemTracker"
include(":app")
