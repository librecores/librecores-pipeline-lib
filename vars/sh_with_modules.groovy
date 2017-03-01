#!/usr/bin/env groovy

/**
 * Simple wrapper step for invoking shell steps with modules
 */
 //TODO: somehow add support of closures
def call(Map params = [:]) {
    def modulesToLoad = params.containsKey('modules') ? params.modules : []
    def command = params.containsKey('command') ? params.command : "echo 'Command is not defined'"
    
    String toInvoke = command
    for (int i = 0; i < modulesToLoad.size(); ++i) {
        String module = modulesToLoad[i]
        command = "module load ${module} && ${toInvoke}"
    }

    sh "$command"
}

