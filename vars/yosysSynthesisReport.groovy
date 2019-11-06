#!/usr/bin/env groovy

import org.librecores.fusesoc.YosysJobSpec

def call(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = YosysJobSpec) Closure cl) {

    YosysJobSpec jobSpec = new YosysJobSpec()
    cl.delegate = jobSpec
    cl()

    jobSpec.build()

    fusesoc {
        image 'librecores/librecores-ci:0.5.0'
        library jobSpec.core, '/src'

        run(jobSpec.core) {
            target jobSpec.target
        }

        shell "/test-scripts/extract-yosys-stats.py < \"${jobSpec.logPath}\""

    }

    plotGraph 'yosys-stats.csv', 'Resource Usage'
    plotGraph 'yosys-cell-stats.csv', 'Cell Count'
}

def plotGraph(csvSource, title) {
    plot csvFileName: "plot-${csvSource}", csvSeries: [[file: csvSource, url: '']],
            exclZero: true,
            group: title,
            numBuilds: '15',
            style: 'line',
            title: title,
            useDescr: true,
            yaxis: 'Values'
}
