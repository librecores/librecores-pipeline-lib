package org.librecores.ci
class Modules implements Serializable {
  def steps
  Modules(steps) {this.steps = steps}
  
  def modulesToLoad = []
  
  def load(modules) {
     for (int i = 0; i < modules.size(); ++i) {
        String module = modules[i]
       // TODO: Try load and check parse output, currently I can't get returnStdout to work..
        steps.sh returnStdout: false, script: "source /usr/share/modules/init/bash && module load ${module}"
       //steps.echo "${status}"
       //modulesToLoad << module
     }
  }
  
  def sh(command) {
     /*String toInvoke = command
     for (int i = 0; i < modulesToLoad.size(); ++i) {
//        String module = modulesToLoad[i]
//        toInvoke = "module load ${module} && ${toInvoke}"
     }
    
     toInvoke = "source /usr/share/modules/init/bash && ${toInvoke}" 
     sh "$command"*/
  }
}
