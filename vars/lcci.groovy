#!/usr/bin/env groovy

class lcci implements Serializable {
    private GString name
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
