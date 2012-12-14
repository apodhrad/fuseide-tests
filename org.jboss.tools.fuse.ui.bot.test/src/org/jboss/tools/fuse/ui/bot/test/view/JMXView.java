package org.jboss.tools.fuse.ui.bot.test.view;

import java.util.List;
import java.util.Vector;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.gen.IView;

public class JMXView extends ViewBase {

	public JMXView() {
		viewObject = new IView() {
			public String getName() {
				return "JMX Explorer";
			}

			public List<String> getGroupPath() {
				List<String> l = new Vector<String>();
				l.add("Fuse IDE");
				return l;
			}
		};
	}

	public SWTBotTreeItem getFuseEsbProcess() {
		SWTBotTreeItem lpItem = bot().tree().expandNode("Local Processes");
		lpItem.contextMenu("Refresh").click();
		SWTBotTreeItem[] process = lpItem.getItems();
		for (int i = 0; i < process.length; i++) {
			if (process[i].getText().startsWith("Fuse ESB")) {
				process[i].contextMenu("Refresh").click();
				return process[i];
			}
		}
		// not found
		return null;
	}
}
