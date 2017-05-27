package org.librecores.ci
class Modules implements Serializable {
  def steps
  Modules(steps) {this.steps = steps}
  def load(args) {
    steps.echo "${args}"
  }
}
