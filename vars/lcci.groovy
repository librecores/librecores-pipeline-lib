#!/usr/bin/env groovy

class lcci implements Serializable {
    private String name
    def setName(value) {
        name = value
    }
    def getName() {
        name
    }
    def caution(message) {
        steps.echo "Hello, ${name}! CAUTION: ${message}"
    }
}
