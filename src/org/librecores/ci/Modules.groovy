package org.librecores.ci
class Modules implements Serializable {
  def steps
  Modules(steps) {this.steps = steps}
  
  def modulesToLoad = []
  
  def load(modules) {
    if (modules instanceof String) {
        modules = [modules]
    }
    modules.each() {
        String module = modules[i]
       // TODO: Try load and check parse output, currently I can't get returnStdout to work..
        steps.sh returnStdout: false, script: "source /usr/share/modules/init/bash && module load ${module}"
       //steps.echo "${status}"
       //modulesToLoad << module
     }
    modulesToLoad += modules
  }
  
  def sh(command) {
     String toInvoke = command
    modulesToLoad.each() {
        String module = modulesToLoad[i]
        toInvoke = "module load ${it} && ${toInvoke}"
     }
    
     toInvoke = "source /usr/share/modules/init/bash && ${toInvoke}" 
     sh "$command"
  }
}
