package org.jboss.tools.fuse.ui.bot.test.matcher;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class EndsWith extends BaseMatcher<SWTBotTreeItem> {

	private String endsWith;

	public EndsWith(String endsWith) {
		this.endsWith = endsWith;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("ends with '" + endsWith + "'");
	}

	@Override
	public boolean matches(Object obj) {
		if (obj instanceof SWTBotTreeItem) {
			SWTBotTreeItem item = (SWTBotTreeItem) obj;
			return item.getText().endsWith(endsWith);
		}
		return false;
	}

}
