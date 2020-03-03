#!/usr/bin/env bash

function set_bash_fail_on_error() {
    set -e
}

function go_to_root_directory() {
    root_directory=$(git rev-parse --show-toplevel)
    cd "$root_directory"
}

function lint_and_test_backend() {
  ./mvnw clean spotbugs:check test
}

function lint_and_test_frontend() {
  bash -ec "cd client-implicit-flow && yarn lint && yarn test-ci && yarn e2e"
}

function push_code() {
  git push
}

function main() {
    set_bash_fail_on_error
    go_to_root_directory

    lint_and_test_frontend
    lint_and_test_backend

    push_code
}

main "$@"

# todo: do not ship if un-staged changes
# todo: do not ship if WIP commits
# todo: what if master moved?
# todo: what if I am not in master branch?
