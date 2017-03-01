# LibreCores Jenkins Pipeline Library 

This library contains common operations and logic for LibreCores CI.

## Status 

The library is under development.
All commands may change, the compatibility is not guaranteed.

## Commands

### sh_with_modules()

Executes the `sh()` command with initializing the specified modules before executing. 

Example:

```groovy
node('librecores-ci-modules').inside() {
    sh_with_modules(modules: ["gcc", "fusesoc/1.6"], command: "make all")
}
```

Effectively the example above creates commands like `sh "module load gcc && module load fusesoc/1.6 && make all"`