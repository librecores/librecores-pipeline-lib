package org.librecores.ci
class Modules implements Serializable {
  def steps
  Modules(steps) {this.steps = steps}
  
  def modulesToLoad = []
  
  def load(modules) {
    if (modules instanceof String) {
        modules = [modules]
    }
    steps.echo modules.size().toString()
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
     modulesToLoad.each {
        toInvoke = "module load ${it} && ${toInvoke}"
     }
    
    steps.echo toInvoke
     toInvoke = "source /usr/share/modules/init/bash && ${toInvoke}" 
     steps.sh "$toInvoke"
  }
}
