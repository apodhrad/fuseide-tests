package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarDropDownButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.fuse.ui.bot.test.condition.ConsoleCondition;
import org.jboss.tools.fuse.ui.bot.test.matcher.StartsWith;
import org.jboss.tools.fuse.ui.bot.test.view.JMXView;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;
import org.jboss.tools.ui.bot.ext.view.ProjectExplorer;
import org.jboss.tools.ui.bot.ext.view.PropertiesView;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotNewObjectWizard;

public class FuseEsbServer {

	public static final String FUSE_ESB_SERVER = "Fuse ESB Enterprise 7.x Server";
	public static final String TOOLTIP_DEPLOY = "Deploys the bundle to...";

	private String serverName;

	public FuseEsbServer(String path) {
		SWTBotNewObjectWizard wizard = new SWTBotNewObjectWizard();
		wizard.open("Server", "Server");
		wizard.bot().tree().expandNode("Red Hat, Inc.").select(FUSE_ESB_SERVER);
		wizard.next();
		wizard.bot().textWithLabel("Installation directory: ").setText(path);
		wizard.next();
		wizard.bot().textWithLabel("User Name:").setText("admin");
		wizard.bot().textWithLabel("Password: ").setText("admin");
		wizard.finishWithWait();
		serverName = "fuse-esb";
	}

	public void start() {
		ServersView serversView = new ServersView();
		serversView.show();
		serversView.startServer(serverName);
	}

	public void stop() {
		ServersView serversView = new ServersView();
		serversView.show();
		serversView.stopServer(serverName);
	}

	public void deployProject(String projectName) {
		SWTBotTreeItem fuseEsbProcess = getFuseEsbProcess();
		if (fuseEsbProcess == null) {
			throw new IllegalStateException("Fuse ESB server is not started");
		}
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.show();
		projectExplorer.selectProject(projectName);
		SWTBotToolbarDropDownButton b = new SWTBot()
				.toolbarDropDownButtonWithTooltip(TOOLTIP_DEPLOY);
		b.menuItem(fuseEsbProcess.getText()).click();
		b.pressShortcut(Keystrokes.ESC);
		projectExplorer.bot().sleep(5 * 1000);
		projectExplorer.bot().waitWhile(new NonSystemJobRunsCondition(),
				TaskDuration.LONG.getTimeout());
		projectExplorer.bot().waitWhile(new ConsoleCondition(), 15 * 60 * 1000, 5 * 1000);
	}

	public String getBundleStatus(String bundle) {
		SWTBotTreeItem fuseEsbProcess = getFuseEsbProcess();
		fuseEsbProcess.expand();
		fuseEsbProcess.select("Bundles");

		PropertiesView propertiesView = new PropertiesView();
		propertiesView.show();
		propertiesView.bot().textWithLabel("Search: ").typeText(bundle);

		SWTBotTable table = propertiesView.bot().table();

		assertTrue("No row in a table", table.rowCount() > 0);
		return table.cell(0, 17);
	}

	public SWTBotTreeItem getFuseEsbProcess() {
		JMXView jmxView = new JMXView();
		jmxView.show();
		return jmxView.getProcess(new StartsWith("Fuse ESB"));
	}
}
