package org.librecores.ci
class Modules implements Serializable {
  def steps
  Modules(steps) {this.steps = steps}
  
  String[] modulesToLoad = []
  
  def load(String[] modules) {
     for (int i = 0; i < modules.size(); ++i) {
        String module = modules[i]
       // TODO: Try load and check parse output, currently I can't get returnStdout to work..
        steps.sh returnStdout: false, script: "source /usr/share/modules/init/bash && module load ${module}"
       //steps.echo "${status}"
       modulesToLoad = module
     }
  }
}
