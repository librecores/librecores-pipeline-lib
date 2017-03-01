package org.librecores.fusesoc;

/**
* Provides helper methods for FuseSoc files.
*/
public class GitHubTools {
  
  def script;
  public GitHubTools(script) {this.script = script}

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