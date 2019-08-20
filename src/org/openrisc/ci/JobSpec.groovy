package org.openrisc.ci

/**
 * Builder for a Job
 */
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
