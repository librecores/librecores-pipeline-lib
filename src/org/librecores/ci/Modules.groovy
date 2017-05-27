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
      steps.sh "source /usr/share/modules/init/bash && module avail ${it} 2> module-avail"
      def output = steps.readFile('module-avail').trim()
      steps.echo "${output}"

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
