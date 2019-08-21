import org.librecores.fusesoc.FuseSocJobSpec

def call(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = FuseSocJobSpec) Closure<Void> cl) {
    FuseSocJobSpec fjs = new FuseSocJobSpec()
    cl.delegate = fjs
    cl()

    command = fjs.build()

    sh "docker run --rm -v \$(pwd):/src -w /src ${fjs.image} /bin/bash -c \"${command}\""
}