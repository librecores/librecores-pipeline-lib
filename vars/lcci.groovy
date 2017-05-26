#!/usr/bin/env groovy

class lcci implements Serializable {
    private String name
    def setName(value) {
        name = value
    }
    def getName() {
        return name
    }
    def caution(String message) {
        echo "Hello, ${name}! CAUTION: ${message}"
    }
}
