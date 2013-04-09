#!/bin/sh
DISPLAY=:2 mvn clean integration-test \
    -Punified.target,server \
    -P!jbosstools-nightly-staging-composite,!jbosstools-nightly-staging-composite-soa-tooling \
    -Dswtbot.test.skip=false -DdebugPort=8001
