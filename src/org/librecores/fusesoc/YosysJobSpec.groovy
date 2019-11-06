package org.librecores.fusesoc

class YosysJobSpec {
    String core
    String target = "synth"
    String logPath

    def core(String value) {
        this.core = value
    }

    def logPath(String value) {
        this.logPath = value
    }

    def target(String value) {
        this.target = value
    }

    /**
     * Expand defaults if required.
     */
    void build() {
        this.logPath = this.logPath ?: "build/${core}_*/${target}-*/yosys.log"
    }

}
