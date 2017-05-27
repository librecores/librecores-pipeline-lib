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
       def avail = steps.sh (returnStdout: true, script: "source /usr/share/modules/init/bash && module avail ${it}").trim()
      steps.echo "${avail}"
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
