package org.librecores.ci
class Modules implements Serializable {
  def steps
  Modules(steps) {this.steps = steps}
  
  def modulesToLoad = []
  
  def load(modules) {
    if (modules instanceof String) {
        modules = [modules]
    }
    modules.each {
       steps.sh "source /usr/share/modules/init/bash && module avail ${it} 2> output"
       def VERSION = steps.sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
      steps.echo "${VERSION}"
       //steps.echo "${status}"
       //modulesToLoad << module
     }
    modulesToLoad += modules
  }
  
  def sh(command) {
     String toInvoke = command
    modulesToLoad.each {
        toInvoke = "module load ${it} && ${toInvoke}"
     }
    
     toInvoke = "source /usr/share/modules/init/bash && ${toInvoke}" 
     steps.sh "$toInvoke"
  }
}
