#!/usr/bin/env bash

function set_bash_fail_on_error() {
    set -e
}

function go_to_root_directory() {
    root_directory=$(git rev-parse --show-toplevel)
    cd "$root_directory"
}

function run_linter() {
  ./mvnw clean spotbugs:check
}

function run_tests() {
    ./mvnw clean test
}

function push_code() {
  git push
}

function main() {
    set_bash_fail_on_error
    go_to_root_directory
    run_linter
    run_tests
    push_code
}

main "$@"
