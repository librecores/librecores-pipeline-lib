#!/usr/bin/env groovy

class acme implements Serializable {
    private String name
    def setName(value) {
        name = value
    }
    def getName() {
        name
    }
    def sh(message) {
      echo "${message}"
    }
}
