#!/bin/sh
DISPLAY=:2 mvn clean install -Pdefault,!jbosstools-nightly-staging-composite,!jbosstools-nightly-staging-composite-soa-tooling -Dswtbot.test.skip=false