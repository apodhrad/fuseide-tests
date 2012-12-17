package org.jboss.tools.fuse.ui.bot.test.view;

import java.util.List;
import java.util.Vector;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.Matcher;
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

	public SWTBotTreeItem getProcess(Matcher<SWTBotTreeItem> matcher) {
		SWTBotTreeItem lpItem = bot().tree().expandNode("Local Processes");
		lpItem.contextMenu("Refresh").click();
		SWTBotTreeItem[] process = lpItem.getItems();
		for (int i = 0; i < process.length; i++) {
			if (matcher.matches(process[i])) {
				process[i].contextMenu("Refresh").click();
				// try to avoid TreeItem {Loading...}
				bot().sleep(5 * 1000);
				return process[i];
			}
		}
		// not found
		return null;
	}

}
