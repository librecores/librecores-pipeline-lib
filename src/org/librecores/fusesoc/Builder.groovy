package org.librecores.fusesoc;

//@Library('fusesoc')
//import org.librecores.fusesoc.GitHubTools;

public class Builder {
  
  def script
  public Builder(script) {this.script = script}
  
  public static final String DEFAULT_IMAGE="onenashev/fusesoc-icarus";
  
  public static final String DEFAULT_CORES_REPO="https://github.com/openrisc/orpsoc-cores.git";
  
  /**
  * Default builder.
  * It picks core definitions from DEFAULT_CORES_REPO and pathes it before the build
  */
  public void build(String coreName, String githubUsername, String version) {
    script.docker.image(DEFAULT_IMAGE).inside {
      script.stage "Patch orpsoc-cores registry"
      script.sh "cd /fusesoc && rm -rf orpsoc-cores"
      script.dir('orpsoc-cores') {
          script.git DEFAULT_CORES_REPO;
      }
      script.sh "cp -R orpsoc-cores /fusesoc/"
      // TODO: get rid of the name hardcode
      script.sh "ls -la /fusesoc/orpsoc-cores/cores/${coreName}/"
      
      //new GitHubTools(script).
      patchCoreSource(coreName, githubUsername, version)

      script.stage "FuseSoC sim"
      try {
          script.sh 'fusesoc sim ${coreName}'
      } finally {
          script.archiveArtifacts artifacts: 'fusesoc.log', excludes: null
      }
    }
  }
  
  /**
   * Pathes core provider definition.
   */
  public void patchCoreSource(String _coreName, String _userName, String _version) {
    String filePath = "/fusesoc/orpsoc-cores/cores/${_coreName}/${_coreName}.core"
    patchCoreSource(filePath, _coreName, _userName, _version);
  }

  /**
   * Pathes core provider definition.
   */
  public void patchCoreProvider(String filepath, String _coreName, String _userName, String _version) {
    
    String modified = "\n\n[provider] \nname = github \n" + 
        "user = ${_userName} \n" +
        "repo = ${_coreName} \n" +
        "version = ${_version}"
    sh "ls ${filePath}"
    String oldCoreDef = readFile(filePath)
    String newCoreDef = oldCoreDef.substring(0,oldCoreDef.indexOf('[provider]')) + modified;
    writeFile file: filePath, text: newCoreDef
  }
}