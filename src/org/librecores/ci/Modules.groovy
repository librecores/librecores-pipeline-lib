package org.librecores.ci

import java.util.ArrayList

/**
 * Environment Modules based projects
 *
 * This is the class to more conveniently build CI based on Environment
 * Modules (http://modules.sourceforge.net/). First modules are loaded to
 * the environment and calling sh() then executes the command(s) in that
 * environment.
 */
class Modules implements Serializable {
  def steps  //!< Private variable to access default steps.

  /**
   * The constructor needs access to the default steps.
   */
  Modules(steps) {
    this.steps = steps
  }

  def modulesToLoad = [] //!< List of modules to load into environment

  /**
   * Load a module
   */
  def load(String module) {
    load([module])
  }
  
  /**
   * Load a list of modules
   */
  def load(ArrayList<String> modules) {
    for (int i = 0; i < modules.size(); ++i) {
      // Check if a module is available
      def module = modules[i]
      // returnStdout didn't work so far, so lets write the output to a file
      steps.sh "source /usr/share/modules/init/bash && /usr/bin/modulecmd bash avail ${module} 2> module-avail"
      def output = steps.readFile('module-avail').trim()
      // If the module is not found "module avail" does not produce output
      if (output.length() == 0) {
        steps.error "Module ${module} not found on this node"
      }
    }
    // Store the modules back to our list
    modulesToLoad += modules
  }

  def sh(command) {
    // Assemble the invocation string by adding all "module load" calls
    String toInvoke = command
    for (int i = 0; i < modulesToLoad.size(); ++i) {
      def module = modulesToLoad[i]
      toInvoke = "module load ${module} && ${toInvoke}"
    }

    // Load "modules" itself
    toInvoke = "source /usr/share/modules/init/bash && ${toInvoke}"

    // Run the command
    steps.sh "$toInvoke"
  }
}
