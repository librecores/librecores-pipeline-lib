#!/usr/bin/env groovy

class lcci implements Serializable {
    def steps
    lcci(steps) {this.steps = steps}
    
    private String name
    def setName(value) {
        name = value
    }
    def getName() {
        return name
    }
    def caution(message) {
        echo "Hello, ${name}! CAUTION: ${message}"
    }
}
