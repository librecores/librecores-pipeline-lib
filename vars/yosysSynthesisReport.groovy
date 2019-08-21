#!/usr/bin/env groovy

def call(String core, String targetName, String yosysLogPath) {
    fusesoc {
        image 'librecores/librecores-ci:0.5.0'
        library core, '/src'

        run(core) {
            target targetName
        }

        shell "/test-scripts/extract-yosys-stats.py < \"${yosysLogPath}\""

        plotGraph 'yosys-stats.csv', 'Resource Usage'
        plotGraph 'yosys-cell-stats.csv', 'Cell Count'
    }
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
