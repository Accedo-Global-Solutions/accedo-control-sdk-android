/**
 * Accedo Control SDK Android CI
 *
 * This pipeline uses Shared Jenkins Library located in: https://github.com/Accedo-Global-Solutions/shared-jenkins-library
 *
 * Alternative Android images you may also use:
 * public.ecr.aws/z5n7u8s2/android-alpine:latest
 * androidsdk/android-31
 **/
@Library("shared-jenkins-library") _

runPipeline(
        nodeLabel: 'linux-ireland-5',
        numberOfBuildsToKeep: 3,
        slackChannel: '#solution-team-build-notifications',
        options: [
                disableConcurrentBuilds(abortPrevious: true)
        ],
        dockerImages: [
                android: 'alvrme/alpine-android:android-33-jdk17-v2023.06.10'
        ],
        envVarsFileId: 'env-vars',
        credentials: [
                artifactoryResolverCredentials(),
                usernamePassword(
                        credentialsId: "artifactorydeployer",
                        usernameVariable: "ARTIFACTORY_DEPLOY_USER",
                        passwordVariable: "ARTIFACTORY_DEPLOY_PASSWORD"
                ),
                usernamePassword(
                        credentialsId: "Jenkins-Oauth-GitHub",
                        usernameVariable: "GITHUB_DEPLOY_USER",
                        passwordVariable: "GITHUB_DEPLOY_TOKEN"
                ),
        ]
) { cts ->
    stage('Build') {
        cts.android.run(gradleTask('assembleDebug'))
    }
    stage('Lint Check') {
        cts.android.run(gradleTask('lintDebug'))
    }
    stage('UnitTest Check') {
        cts.android.run(gradleTask('unitTests koverXmlReport koverMergedReport koverMergedVerify'))
    }
    stage('Sonar Analysis') {
        def branchArgs = ''

        if (isPullRequest()) {
            def prArgs = [
                    'key'   : env.CHANGE_ID,
                    'branch': env.CHANGE_BRANCH,
                    'base'  : env.CHANGE_TARGET
            ]
            branchArgs = prArgs.collect { key, value -> "-Dsonar.pullrequest.${key}=${value}" }.join(' ')
        } else if (isBranch()) {
            branchArgs = "-Dsonar.branch.name=${env.BRANCH_NAME}"
        }

        timeout(time: 20, unit: 'MINUTES') {
            cts.android.run(gradleTask("""sonar \
                ${branchArgs}"""))
        }

    }
    stage('Release new version') {
        when(isBranch('master')) {
            cts.android.run(gradleTask('libraryVersion'))

            // Get app version output
            String libraryVersion = readFile(file: "libraryVersion.txt").trim()

            if (libraryVersion != null) {

                echo "libraryVersion: ${libraryVersion}"

                gitSetUser()

                sh("git fetch --prune --prune-tags")

                def versionTagList = sh(script: 'git tag -l', returnStdout: true).trim().split('\n')

                String isTagged = versionTagList.find { tag -> tag.contains(libraryVersion) }

                if (isTagged == null) {
                    echo "Releasing and Tagging a new version ${libraryVersion}"

                    try {
                        cts.android.run(gradleTask("control-sdk:assembleRelease control-sdk:artifactoryPublish control-sdk:publishAllPublicationsToGithubPackagesRepository"))
                    } catch (e) {
                        echo "ERROR: ${e.message}"
                    }

                    gitTag(libraryVersion.replace(":", "-"))
                } else {
                    echo "Tag ${libraryVersion} already exists."
                }


            } else {
                error("Component should not be null or empty")
            }
        }
    }
}

String gradleTask(String taskName) {
    return """./gradlew ${taskName} \
           -Partifactory_deploy_user=${env.ARTIFACTORY_DEPLOY_USER} \
           -Partifactory_deploy_password=${env.ARTIFACTORY_DEPLOY_PASSWORD} \
           -Pgithub_deploy_user=${env.GITHUB_DEPLOY_USER} \
           -Pgithub_deploy_token=${env.GITHUB_DEPLOY_TOKEN} \
           --no-daemon \
           """
}

