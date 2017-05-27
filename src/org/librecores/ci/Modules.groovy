package org.librecores.ci
class Modules implements Serializable {
  def steps
  Modules(steps) {this.steps = steps}
  
  def modulesToLoad = []
  
  def load(modules) {
    if (modules instanceof String) {
        modules = [modules]
    }

    for (int i = 0; i < modules.size(); ++i) {
      def module = modules[i]
      steps.sh "source /usr/share/modules/init/bash && /usr/bin/modulecmd bash avail ${module} 2> module-avail"
      def output = steps.readFile('module-avail').trim()
      if (output.length() == 0) {
        steps.error "Module ${module} not found on this node"
      }
     }
    modulesToLoad += modules
  }
  
  def sh(command) {
     String toInvoke = command
     for (int i = 0; i < modulesToLoad.size(); ++i) {
       def module = modulesToLoad[i]
       toInvoke = "module load ${module} && ${toInvoke}"
     }
    
     toInvoke = "source /usr/share/modules/init/bash && ${toInvoke}" 
     steps.sh "$toInvoke"
  }
}
