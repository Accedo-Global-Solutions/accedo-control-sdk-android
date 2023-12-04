plugins {
    id("com.android.library") version "7.4.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.jfrog.artifactory") version "5.1.11"
    id("org.sonarqube") version "4.2.1.3168"
}

sonar {
    properties {
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.organization", "accedo-global-solutions")
        property("sonar.projectKey", "Accedo-Global-Solutions_accedo-control-sdk-android")
        property("sonar.projectName", "Control SDK Android")
        property("sonar.pullrequest.github.repository", "Accedo-Global-Solutions/accedo-control-sdk-android")
        property("sonar.pullrequest.provider", "GitHub")
        property("sonar.java.coveragePlugin", "jacoco")
        property("sonar.language", "java")
        property("sonar.log.level", "TRACE")
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.tags", "control-sdk-android, android")
        property("sonar.verbose", true)
        property("sonar.tests", "$projectDir/src/androidTest/java")
    }
}

subprojects {
    sonar {
        properties {
            property("sonar.projectVersion", "#${this@subprojects.version}")
            property("sonar.coverage.jacoco.xmlReportPaths", "${this@subprojects.buildDir}/reports/jacoco.xml")
            property("sonar.java.binaries", "${this@subprojects.buildDir}/classes/java")
            property("sonar.junit.reportPaths", "$buildDir/test-results/testDebugUnitTest")
            property("sonar.sources", "${this@subprojects.projectDir}/src/main/java")
            property("sonar.tests", "${this@subprojects.projectDir}/src/test/java")
            property("sonar.android.lint.report", "${this@subprojects.buildDir}/reports/lint-results-debug.xml")
        }
    }
}

tasks {
    register("libraryVersion") {
        doLast {
            val controlSdkVersion = subprojects.find { it.name == "control-sdk" }?.version?.toString()
            if (!controlSdkVersion.isNullOrEmpty() && controlSdkVersion != "unspecified") {
                val versionsFile = file("libraryVersion.txt")
                if (versionsFile.exists()) {
                    versionsFile.delete()
                }
                versionsFile.writeText(controlSdkVersion)
            }
        }
    }

}
