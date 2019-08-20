#!/usr/bin/env groovy

class JobSpec {
    def jobConfig = [:]

    JobSpec(name) {
        jobConfig["name"] = name
    }

    def name(value) {
        jobConfig["name"] = value
    }

    def job(value) {
        jobConfig["job"] = value
    }

    def sim(value) {
        jobConfig["sim"] = value
    }

    def pipeline(value) {
        jobConfig["pipeline"] = value
    }

    def expectedFailures(value) {
        jobConfig["expectedFailures"] = value
    }

    def extraCoreArgs(value) {
        jobConfig["extraCoreArgs"] = value
    }
}

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

class PipelineSpec {
    def jobConfigs = []
    def image = 'librecores/librecores-ci-openrisc'

    def image(value) {
        this.image = value
    }


    def job(name, @DelegatesTo(JobSpec) Closure closure) {
        JobSpec jobSpec = new JobSpec(name)
        closure.delegate = jobSpec
        closure()
        jobConfigs << jobSpec.jobConfig
    }
}

/**
 * Pipeline for OpenRISC projects
 * @param jobs
 * @return
 */
def call(@DelegatesTo(PipelineSpec) Closure closure) {

    PipelineSpec pipelineSpec = new PipelineSpec()
    closure.delegate = pipelineSpec
    closure()

    def parallelJobs = pipelineSpec.jobConfigs.collectEntries {
        ["${it.name}": buildStage(it)]
    }

    pipeline {
        agent any
        stages {
            stage("pre-build") {
                steps {
                    sh "docker pull ${pipelineSpec.image}"
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
