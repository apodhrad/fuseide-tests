package org.jboss.tools.fuse.ui.bot.test.condition;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.jboss.tools.fuse.ui.bot.test.view.ConsoleView;

public class ServerStartedCondition implements ICondition {

	@Override
	public boolean test() throws Exception {
		ConsoleView consoleView = new ConsoleView();
		consoleView.show();
		return consoleView.getConsoleText().contains("100%");
	}

	@Override
	public void init(SWTBot bot) {
	}

	@Override
	public String getFailureMessage() {
		return "The Fuse ESB server wasn't started";
	}

}
