#!/usr/bin/env groovy

/**
 * Builds a parallel job
 * @param jobConfig
 * @return
 */
def buildStage(jobConfig) {
    final String DOCKER_IMAGE = 'librecores/librecores-ci-openrisc'
    final COMMAND = "/src/.travis/test.sh"

    def name = jobConfig.get('name')
    def job = jobConfig.get('job')
    def sim = jobConfig.get('sim', '')
    def pipeline = jobConfig.get('pipeline', '')
    def expectedFailures = jobConfig.get('expectedFailures', '')
    def extraCoreArgs = jobConfig.get('extraCoreArgs', '')

    def envVars = "-e \"JOB=${job}\" " +
            "-e \"SIM=${sim}\" " +
            "-e \"PIPELINE=${pipeline}\"" +
            " -e \"EXPECTED_FAILURES=${expectedFailures}\" " +
            "-e \"EXTRA_CORE_ARGS=${extraCoreArgs}\""

    return {
        stage("${name}") {
            sh "docker run --rm -v \$(pwd):/src ${envVars} ${DOCKER_IMAGE} ${COMMAND}"
        }
    }
}

/**
 * Pipeline for OpenRISC projects
 * @param jobs
 * @return
 */
def call(jobs) {

    def parallelJobs = jobs.collectEntries {
        ["${it.name}": buildStage(it)]
    }

    pipeline {
        agent any
        stages {
            stage("pre-build") {
                steps {
                    sh 'docker pull librecores/librecores-ci-openrisc'
                    sh 'docker images'
                }
            }

            stage("build") {
                steps {
                    script {
                        parallel parallelJobs
                    }
                }
            }
        }
    }
}
