package org.jboss.tools.fuse.ui.bot.test.suite;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.fuse.ui.bot.test.Activator;
import org.jboss.tools.fuse.ui.bot.test.FuseBot;
import org.jboss.tools.fuse.ui.bot.test.FuseEsbServer;
import org.jboss.tools.fuse.ui.bot.test.condition.ConsoleCondition;
import org.jboss.tools.fuse.ui.bot.test.editor.CamelEditor;
import org.jboss.tools.fuse.ui.bot.test.view.ConsoleView;
import org.jboss.tools.fuse.ui.bot.test.view.JUnitView;
import org.jboss.tools.fuse.ui.bot.test.wizard.FuseProject;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.TestConfigurator;
import org.jboss.tools.ui.bot.ext.helper.ResourceHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <b>Simple Test:</b>
 * 
 * <ol>
 * <li>Create and run Fuse ESB server</li>
 * <li>Create a project camel-spring</li>
 * <li>Delete camel-Context.xml and create new one with name camel-context.xml</li>
 * <li>Add endpoint with uri file:src/data?noop=true</li>
 * <li>Add endpoint with uri file:target/messages/others</li>
 * <li>Select the camel xml file and Run As > Local Camel Context (without
 * tests)</li>
 * <li>Create and edit a camel test case</li>
 * <li>Select the camel test case and Run As > JUnit Test</li>
 * <li>Deploy the project to Fuse ESB server</li>
 * </ol>
 * 
 * @author Andrej Podhradsky (apodhrad@redhat.com)
 * 
 */
@Require(perspective = "Fuse Integration ")
public class FuseSimpleTest extends SWTTestExt {

	public static final String CAMEL_VESRION = "Apache Camel 2.10.0.redhat";
	public static final String CAMEL_PROJECT = "camel-spring";
	public static final String CAMEL_FILE = "camel-context.xml";
	public static final String CAMEL_ARCHETYPE = "camel-archetype-spring";

	private FuseBot fuseBot;
	private FuseEsbServer fuseEsbServer;

	public FuseSimpleTest() {
		fuseBot = new FuseBot();
	}

//	@BeforeClass
	public static void setSshHome() {
		bot.menu("Window").menu("Preferences").click();
		bot.tree().expandNode("General").expandNode("Network Connections").select("SSH2");
		bot.textWithLabel("SSH2 home:").setText(
				ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, ".ssh"));
		bot.button("OK").click();
	}

//	@BeforeClass
	public static void setMavenSettings() {
		bot.menu("Window").menu("Preferences").click();
		bot.tree().expandNode("Maven").select("User Settings");
		bot.text(1).setText(
				ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID,
						"resources/settings.xml"));
		bot.button("OK").click();
	}

	@Before
	public void startFuseEsbServer() {
		String path = TestConfigurator.getProperty("FUSE_ESB");
//		path = ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, path);
		fuseEsbServer = new FuseEsbServer(path);
		fuseEsbServer.start();

		assertNotNull(fuseEsbServer.getFuseEsbProcess());
	}

	@After
	public void stopFuseEsbServer() {
		fuseEsbServer.stop();
	}

	@Test
	public void fuseTest() {
		eclipse.maximizeActiveShell();

		new FuseProject(CAMEL_ARCHETYPE).execute();

		deleteFile(CAMEL_FILE, CAMEL_PROJECT, "src/main/resources", "META-INF", "spring");

		fuseBot.createNewCamelFile(CAMEL_PROJECT, CAMEL_FILE);

		CamelEditor camelEditor = fuseBot.getCamelEditor(CAMEL_FILE);
		camelEditor.addEndpoint("file:src/data?noop=true");
		camelEditor.addEndpoint("file:target/messages/others");
		camelEditor.saveAndClose();

		selectCamelFile();
		runAs("2 Local Camel Context (without tests)");

		ConsoleView console = new ConsoleView();
		// assertTrue(console.getConsoleText().contains(CAMEL_VESRION +
		// " starting"));
		assertTrue(console.getConsoleText().contains(CAMEL_VESRION));
		assertTrue(console.getConsoleText().contains("started in"));
		// CAMEL_VESRION + " (CamelContext: camel-1) started in"));
		console.terminate();

		fuseBot.createNewCamelTest();
		fixCamelTest();

		selectCamelTest();
		runAs("1 JUnit Test");

		JUnitView junitView = new JUnitView();
		junitView.show();

		assertEquals("0", junitView.getErrors());
		assertEquals("0", junitView.getFailures());

		// Apply a workaround described at ECLIPSE-848
		fixPomFile();

		// Deploy the project to Fuse ESB
		fuseEsbServer.deployProject(CAMEL_PROJECT);
		assertEquals("ACTIVE", fuseEsbServer.getBundleStatus("mycompany"));
	}

	private void fixCamelTest() {
		SWTBotEditor editor = bot.editorByTitle("CamelContextXmlTest.java");
		SWTBotEclipseEditor textEditor = editor.toTextEditor();
		int lineCount = textEditor.getLineCount();
		for (int i = 0; i < lineCount; i++) {
			String line = textEditor.getTextOnLine(i);
			if (line.contains("outputEndpoint.expectedBodiesReceivedInAnyOrder(expectedBodies);")) {
				textEditor.insertText(i, 0, "//");
				textEditor.insertText(i + 1, 0, "outputEndpoint.expectedMessageCount(2);");
				break;
			}
		}
		textEditor.saveAndClose();
	}

	private void fixPomFile() {
		bot.sleep(TIME_5S);
		projectExplorer.openFile(CAMEL_PROJECT, "pom.xml");
		SWTBotEditor editor = bot.editorByTitle(CAMEL_PROJECT + "/pom.xml");
		editor.bot().cTabItem("pom.xml").activate();
		SWTBotEclipseEditor textEditor = editor.toTextEditor();
		int lineCount = textEditor.getLineCount();
		for (int i = 0; i < lineCount; i++) {
			String line = textEditor.getTextOnLine(i);
			if (line.contains("<artifactId>log4j</artifactId>")) {
				textEditor.insertText(i + 1, 0, "<scope>provided</scope>");
				break;
			}
		}
		textEditor.saveAndClose();
	}

	private void runAs(String runCommand) {
		bot.menu("Run").menu("Run As").menu(runCommand).click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TaskDuration.LONG.getTimeout());
		bot.waitWhile(new ConsoleCondition(), 15 * TIME_60S, TIME_5S);
	}

	private SWTBotTreeItem selectCamelFile() {
		packageExplorer.show();
		return packageExplorer.selectTreeItem(CAMEL_FILE, new String[] { CAMEL_PROJECT,
				"src/main/resources", "META-INF", "spring" });
	}

	private void deleteFile(String fileName, String... path) {
		packageExplorer.selectTreeItem(fileName, path).contextMenu("Delete").click();
		bot.button("OK").click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TaskDuration.LONG.getTimeout());
	}

	private SWTBotTreeItem selectCamelTest() {
		packageExplorer.show();
		return packageExplorer.selectTreeItem("CamelContextXmlTest.java", new String[] {
				CAMEL_PROJECT, "src/test/java", "com.mycompany.camel.spring" });
	}
}
