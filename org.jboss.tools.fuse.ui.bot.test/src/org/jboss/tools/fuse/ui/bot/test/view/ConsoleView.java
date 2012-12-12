package org.jboss.tools.fuse.ui.bot.test.view;

import org.jboss.tools.ui.bot.ext.gen.ActionItem.View.GeneralConsole;

public class ConsoleView extends ViewBase {

	public ConsoleView() {
		viewObject = GeneralConsole.LABEL;
	}

	public boolean isTerminated() {
		return !getToolbarButtonWitTooltip("Terminate").isEnabled();
	}

	public void terminate() {
		if (!isTerminated()) {
			getToolbarButtonWitTooltip("Terminate").click();
		}
	}

	public String getConsoleText() {
		return bot().styledText().getText();
	}
}
