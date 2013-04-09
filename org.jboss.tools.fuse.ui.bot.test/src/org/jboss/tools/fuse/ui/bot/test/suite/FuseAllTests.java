package org.jboss.tools.fuse.ui.bot.test.suite;

import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;


@SuiteClasses({
	FuseSimpleTest.class,
//	FuseProjectCreationTest.class,
	FuseVersionTest.class
	})
@RunWith(RequirementAwareSuite.class)
public class FuseAllTests {

}