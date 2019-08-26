# LibreCores Jenkins Pipeline Library

This library contains common operations and logic for LibreCores CI.

## Status

The library is under development.
All commands may change, the compatibility is not guaranteed.

## Classes

### Modules

This is the class to more conveniently build CI based on Environment Modules (http://modules.sourceforge.net/). First modules are loaded to the environment and calling sh() then executes the command(s) in that environment.

Usage:

```groovy
@Library('librecoresci') import org.librecores.ci.Modules
def lcci = new Modules(steps)

node('librecores-ci-modules') {
  lcci.load(["eda/verilator/3.902"])

  stage('Simulation Build') {
    lcci.sh 'verilator -f verilate.vc'
  }
}
```

## Commands

### sh_with_modules()

Executes the `sh()` command with initializing the specified modules before executing.

Example:

```groovy
node('librecores-ci-modules') {
    sh_with_modules(modules: ["gcc", "fusesoc/1.6"], command: "make all")
}
```

Effectively the example above creates commands like `source ${modulesPath}/init/bash && sh "module load gcc && module load fusesoc/1.6 && make all"`.

### openriscPipeline

Builds a pipeline for OpenRISC projects.

Out of the box it configures:
- Pulls Docker images for `librecores-ci-openrisc`
- Executes jobs in parallel inside Docker images

#### Example

This snippet is from the Jenkins pipeline for [mor1kx](https://github.com/openrisc/mor1kx)

```groovy
@Library('librecoresci') _

openriscPipeline {
        job('verilator') {
            job 'verilator'
        }

        job('testing-1') {
            job 'or1k-tests'
            sim 'icarus'
            pipeline 'CAPPUCCINO'
            expectedFailures 'or1k-cy'
        }
}
```
### fusesoc

Builds a step for a generic FuseSoC invocation.

- Chooses a base docker image with FuseSoC
- Adds library to FuseSoC
- Runs FuseSoC step
- Runs arbitrary shell commands in this step

#### Example

```groovy
@Library('librecoresci') _

pipeline {
    stages {
        stage('example-fusesoc-step') {
            fusesoc {
                image 'librecores/librecores-ci:0.5.0'
                library 'some_core', '/src'
        
                run('some_core') {
                    target 'fusesoc_target'
                }
        
                shell "echo 'Additional steps on shell'"
            }
        }
    }
}
```
### yosysSynthesisReport

Build a step for [Yosys Synthesis](http://www.clifford.at/yosys/) for monitoring resource usage statistics and publish on Jenkins.

- Generalised Yosys Synthesis 
- Can be configured for various hardware projects 
- Simple declarative call which requires parameters `core`, `target`, `logpath`
- Can be modified and extend to include more paramaters in future.

#### Example 

```groovy
@Library('librecoresci') _

pipeline {
    agent any 
    stages{
        stage('yosys') {
            steps {
                yosysSynthesisReport {
					core 'mor1kx'
					target 'synth'
					logPath 'build/mor1kx_5.0-r3/synth-icestorm/yosys.log'
				}
            }
        }
    }
}
```
