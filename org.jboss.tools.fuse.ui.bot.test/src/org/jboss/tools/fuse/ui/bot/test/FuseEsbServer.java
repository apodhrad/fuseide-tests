package org.jboss.tools.fuse.ui.bot.test;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.fuse.ui.bot.test.view.JMXView;
import org.jboss.tools.ui.bot.ext.view.ProjectExplorer;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotNewObjectWizard;

public class FuseEsbServer {

	public static final String FUSE_ESB_SERVER = "Fuse ESB Enterprise 7.x Server";

	private String serverName;

	public FuseEsbServer(String path) {
		SWTBotNewObjectWizard wizard = new SWTBotNewObjectWizard();
		wizard.open("Server", "Server");
		wizard.bot().tree().expandNode("FuseSource").select(FUSE_ESB_SERVER);
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
		JMXView jmxView = new JMXView();
		jmxView.show();
		SWTBotTreeItem fuseEsbProcess = jmxView.getFuseEsbProcess();
		if (fuseEsbProcess == null) {
			throw new IllegalStateException("Fuse ESB server is not started");
		}
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.show();
		SWTBotTreeItem project = projectExplorer.bot().tree().getTreeItem(projectName);
		project.contextMenu("Deploy to...").menu(fuseEsbProcess.getText()).click();
	}
}
