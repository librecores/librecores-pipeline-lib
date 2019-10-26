#!/usr/bin/env groovy

import org.openrisc.ci.PipelineSpec

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
            "-e \"PIPELINE=${pipeline}\" " +
            "-e \"EXPECTED_FAILURES=${expectedFailures}\" " +
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
def call(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = PipelineSpec) Closure closure) {

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

            stage("Yosys Synthesis resource usage statistics parsing and publishing ") {
                when {
                    expression {
                        pipelineSpec.yosysJobSpec != null
                    }
                }
                steps {
                    yosysSynthesisReport pipelineSpec.yosysJobSpec
                }
            }
        }
    }
}
