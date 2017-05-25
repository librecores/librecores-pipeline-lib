#!/usr/bin/env groovy

def setName(value) {
    name = value
}
def getName() {
    name
}
def sh(message) {
    echo "Hello, ${name}! CAUTION: ${message}"
}
