package org.jboss.tools.fuse.ui.bot.test.condition;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.jboss.tools.fuse.ui.bot.test.view.ConsoleView;

public class ConsoleCondition implements ICondition {

	private String lastConsoleText;
	
	@Override
	public boolean test() throws Exception {
		ConsoleView consoleView = new ConsoleView();
		consoleView.show();
		String consoleText = consoleView.getConsoleText();
		return isChanged(consoleText) && !consoleView.isTerminated();
	}

	@Override
	public void init(SWTBot bot) {

	}

	@Override
	public String getFailureMessage() {
		return null;
	}

	private boolean isChanged(String consoleText) {
		if (!consoleText.equals(lastConsoleText)) {
			lastConsoleText = consoleText;
			return true;
		} else {
			String[] lines = consoleText.split("\n");
			String lastLine = lines[lines.length - 1];
			return lastLine.contains("Download");
		}
	}

}
