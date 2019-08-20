package org.openrisc.ci

/**
 * Builder for a OpenRISC Pipeline
 */
class PipelineSpec {
    def jobConfigs = []
    def image = 'librecores/librecores-ci-openrisc'

    def image(value) {
        this.image = value
    }


    def job(name, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = JobSpec) Closure closure) {
        JobSpec jobSpec = new JobSpec(name)
        closure.delegate = jobSpec
        closure()
        jobConfigs << jobSpec.jobConfig
    }
}
