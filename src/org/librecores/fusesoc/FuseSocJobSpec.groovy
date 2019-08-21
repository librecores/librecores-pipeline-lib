package org.librecores.fusesoc

class FuseSocJobSpec {
    private String image = ''
    private Map<String, String> libraries = [:]
    private List<FuseSocRunSpec> runTargets = []
    private List<String> shellCommands = []

    def image(String value) {
        this.image = value
    }

    def library(String name, String path) {
        libraries[name] = path
    }

    def run(system, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = FuseSocRunSpec) Closure cl) {
        FuseSocRunSpec frs = new FuseSocRunSpec(system)
        cl.delegate = frs
        cl()
        runTargets << frs
    }

    def shell(command) {
        shellCommands << command
    }

    def build() {
        return (prepareLibraryCommands() + prepareRunCommands() + shellCommands).join(" && ")
    }

    private List<String> prepareRunCommands() {
        runTargets.collect { entry ->
            entry.build()
        }
    }

    private List<String> prepareLibraryCommands() {
        libraries.collect { entry ->
            "fusesoc library add ${entry.key} ${entry.value}".toString()
        }
    }
}

