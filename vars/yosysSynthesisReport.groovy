#!/usr/bin/env groovy

def call(String core, String targetName, String yosysLogPath) {
    fusesoc {
        image 'librecores/librecores-ci:0.5.0'
        library core, '/src'

        run(core) {
            target targetName
        }

        shell "/test-scripts/extract-yosys-stats.py < \"${yosysLogPath}\""
    }
}
