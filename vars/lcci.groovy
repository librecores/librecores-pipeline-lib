#!/usr/bin/env groovy

def setName(value) {
    name = value
}
def getName() {
    name
}
def caution(message) {
    echo "Hello, ${name}! CAUTION: ${message}"
}
