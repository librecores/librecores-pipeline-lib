package org.librecores.fusesoc

class FuseSocRunSpec {
    private String system
    private String target
    private String tool
    private String backendArgs = ''

    FuseSocRunSpec(String system) {
        this.system = system
    }

    def target(value) {
        this.target = value
    }

    def tool(value) {
        this.tool = value
    }

    def backendArgs(value) {
        this.backendArgs = value
    }

    def build() {
        def fusesocArgs = []

        if (target != null) {
            fusesocArgs << "--target=${target}"
        }

        if (tool != null) {
            fusesocArgs << "--tool=${tool}"
        }

        return "fusesoc run ${fusesocArgs.join(' ')} ${system} ${backendArgs}".toString()
    }
}