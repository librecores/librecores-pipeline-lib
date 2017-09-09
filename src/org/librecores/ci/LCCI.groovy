package org.librecores.ci

class LCCI {

    def steps

    LCCI(steps) {
        this.steps = steps
    }

    @NonCPS
    Boolean isRunningOnProduction() {
        return steps.env.JENKINS_URL == 'https://ci.librecores.org/'
    }

    @NonCPS
    void checkoutScmOrFallback(def fallback = null) {
        if (isRunningOnProduction()) {
            steps.checkout steps.scm
        } else if (fallback == null) {
            steps.fail "Cannot checkout SCM. Running on a non-production instance with undefined fallback"
        }

        steps.echo "Checking out from a fallback"
        steps.git fallback
    }

}
