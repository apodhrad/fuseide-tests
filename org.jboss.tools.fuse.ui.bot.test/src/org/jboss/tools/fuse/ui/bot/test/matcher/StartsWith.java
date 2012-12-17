package org.jboss.tools.fuse.ui.bot.test.matcher;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class StartsWith extends BaseMatcher<SWTBotTreeItem> {

	private String startsWith;

	public StartsWith(String startsWith) {
		this.startsWith = startsWith;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("ends with '" + startsWith + "'");
	}

	@Override
	public boolean matches(Object obj) {
		if (obj instanceof SWTBotTreeItem) {
			SWTBotTreeItem item = (SWTBotTreeItem) obj;
			return item.getText().startsWith(startsWith);
		}
		return false;
	}

}
