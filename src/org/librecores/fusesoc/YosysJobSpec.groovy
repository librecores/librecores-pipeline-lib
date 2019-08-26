package org.librecores.fusesoc

class YosysJobSpec {
    String core
    String logPath
    String target

    def core(String value) {
        this.core = value
    }

    def logPath(String value) {
        this.logPath = value
    }

    def target(String value) {
        this.target = value
    }
}
