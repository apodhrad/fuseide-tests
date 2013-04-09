package org.jboss.tools.fuse.ui.bot.test.suite;

import org.jboss.tools.fuse.ui.bot.test.wizard.FuseProject;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.view.ProblemsView;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests if all Fuse project are created properly
 * 
 * @author Andrej Podhradsky (apodhrad@redhat.com)
 * 
 */
@Require(perspective = "Java")
public class FuseProjectCreationTest extends SWTTestExt {

	@BeforeClass
	public static void maximizeEclipse() {
		eclipse.maximizeActiveShell();
	}

	@Test
	public void camelActiveMQTest() {
		createProject("camel-archetype-activemq");
	}

	@Test
	public void camelBlueprintTest() {
		createProject("camel-archetype-blueprint");
	}

	@Test
	public void camelComponentTest() {
		createProject("camel-archetype-component");
	}

	@Test
	public void camelDataFormatTest() {
		createProject("camel-archetype-dataformat");
	}

	@Test
	public void camelJavaTest() {
		createProject("camel-archetype-java");
	}

	@Test
	public void camelSpringTest() {
		createProject("camel-archetype-spring");
	}

	@Test
	public void camelSpringDMTest() {
		createProject("camel-archetype-spring-dm");
	}

	@Test
	public void camelWebTest() {
		createProject("camel-archetype-web");
	}

	@Test
	public void camelWebConsoleTest() {
		createProject("camel-archetype-webconsole");
	}

	@Test
	public void camelCxfJaxRSTest() {
		createProject("cxf-jaxrs-service");
	}

	@Test
	public void camelCxfJaxWSTest() {
		createProject("cxf-jaxws-javafirst");
	}

	@Test
	public void camelCxfCodeTest() {
		createProject("camel-cxf-code-first-archetype");
	}

	@Test
	public void camelCxfContractTest() {
		createProject("camel-cxf-contract-first-archetype");
	}

	@Test
	public void camelDroolsTest() {
		createProject("camel-drools-archetype");
	}

	private void createProject(String archetype) {
		new FuseProject(archetype).execute();
		assertNull("Some errors occur during creating '" + archetype + "'",
				ProblemsView.getErrorsNode(bot));
	}

	@After
	public void deleteAllProjects() {
		projectExplorer.deleteAllProjects();
	}

}
