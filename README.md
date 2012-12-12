fuseide-tests
=============

SWT Bot Tests for Fuse IDE

## Runnig bot tests

1. Set your settings.xml as described in Configuring Maven to use the JBoss Repository [1]
2. Execute mvn clean install -Pdefault -Dswtbot.test.skip=false

Note that it can take several minutes to run bots for the first time.

[1] https://community.jboss.org/wiki/MavenGettingStarted-Developers