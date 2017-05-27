package org.librecores.ci
class Modules implements Serializable {
  def steps
  Modules(steps) {this.steps = steps}
  
  String[] modules = []
  
  def load(String[] mods) {
     modules += mods
  }
}
