package org.librecores.fusesoc

import org.librecores.ci.LCCI

class FuseSoCBuilder implements Serializable {

  public static final String DEFAULT_IMAGE="onenashev/fusesoc-icarus"
  
  public static final String DEFAULT_CORES_REPO="https://github.com/openrisc/orpsoc-cores.git"

    /**
     * Default builder for {@code fusesoc sim} multi-branch projects.
     * It checks out the repo, determines commit ID and then invokes {@code sim()} to perform thecore build.
     *
     * @param coreName name of the FuseSoC core
     * @param githubOrganization Name of organization or User to be used as override of the orpsoc-cores repo
     * @param extraSimArgs Extra parameters to be passed to FuseSoC Sim
     */
    static void defaultCoreCIBuild(def script, String coreName, String githubOrganization,
                                   String extraSimArgs="",
                                   String nodeLabels="docker-fusesoc-icarus") {
        def commitId = null

        script.stage('Determine required patches') {
            script.node("librecores-ci") {
                // Now we take only one repo, but maybe we need more ones later
                new LCCI(script).checkoutScmOrFallback("https://github.com/${githubOrganization}/${coreName}.git")
                script.sh 'git rev-parse HEAD > GIT_COMMIT'
                commitId = script.readFile('GIT_COMMIT')
            }
        }

        script.stage('Build component') {
            script.node(nodeLabels) {
                FuseSoCBuilder.sim(script, coreName, githubOrganization, commitId,
                    "/fusesoc", extraSimArgs)
            }
        }
    }

    /**
     * Default builder for {@code fusesoc sim}.
     * It picks core definitions from {@link #DEFAULT_CORES_REPO} and patches it before the build.
     *
     *
     * @param coreName name of the FuseSoC core
     * @param githubOrganization Name of organization or User to be used as override of the orpsoc-cores repo
     * @param version Version to be used
     * @param fusesocRoot Root directory of FuseSoc
     * @param extraSimArgs Extra parameters to be passed to FuseSoC Sim
     */
    static void sim(def script, String coreName, String githubOrganization, String version,
                  String fusesocRoot, String extraSimArgs="") {
      script.stage ("Patch the orpsoc-cores registry") {
          script.sh "cd ${fusesocRoot} && rm -rf orpsoc-cores"
          script.dir('orpsoc-cores') {
              script.git DEFAULT_CORES_REPO
          }
          script.sh "cp -R orpsoc-cores /fusesoc/"
          // TODO: get rid of the name hardcode
          script.sh "ls -la /fusesoc/orpsoc-cores/cores/${coreName}/"

          //new GitHubTools(script).
          FuseSoCBuilder.patchCoreSource(script, coreName, githubOrganization, version)

          script.stage "FuseSoC sim"
          try {
              script.sh "fusesoc sim ${coreName} ${extraSimArgs}"
          } finally {
              script.archiveArtifacts artifacts: 'fusesoc.log', excludes: null
          }
      }
  }
  
    /**
     * Patches core provider definition.
     */
    static void patchCoreSource(def script, String _coreName, String _userName, String _version) {
            String filePath = "/fusesoc/orpsoc-cores/cores/${_coreName}/${_coreName}.core"
        FuseSoCBuilder.patchCoreProvider(script, filePath, _coreName, _userName, _version)
    }

    /**
     * Patches core provider definition.
     */
    static void patchCoreProvider(def script, String filePath, String _coreName, String _userName, String _version) {
        String modified = "\n\n[provider] \nname = github \n" +
          "user = ${_userName} \n" +
          "repo = ${_coreName} \n" +
          "version = ${_version}"
        String oldCoreDef = script.readFile(filePath)
        String newCoreDef = oldCoreDef.substring(0,oldCoreDef.indexOf('[provider]')) + modified
        script.writeFile file: filePath, text: newCoreDef
        script.sh "cp ${filePath} modified_${_coreName}.core"
        script.archiveArtifacts artifacts: "modified_${_coreName}.core", excludes: null
    }
}
