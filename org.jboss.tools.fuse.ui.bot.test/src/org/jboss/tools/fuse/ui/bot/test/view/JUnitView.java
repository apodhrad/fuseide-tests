package org.jboss.tools.fuse.ui.bot.test.view;

import java.util.List;
import java.util.Vector;

import org.jboss.tools.fuse.ui.bot.test.condition.ConsoleCondition;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;
import org.jboss.tools.ui.bot.ext.gen.IView;

public class JUnitView extends ViewBase {

	public JUnitView() {
		viewObject = new IView() {
			public String getName() {
				return "JUnit";
			}

			public List<String> getGroupPath() {
				List<String> l = new Vector<String>();
				l.add("Java");
				return l;
			}
		};
	}

	public void reRunTest() {
		bot.waitWhile(new NonSystemJobRunsCondition(), TaskDuration.LONG.getTimeout());
		getToolbarButtonWitTooltip("Rerun Test").click();
		bot.waitWhile(new ConsoleCondition(), 15 * 60 * 1000, 5 * 1000);
	}

	public String getRuns() {
		return bot().text(0).getText();
	}

	public String getErrors() {
		return bot().text(1).getText();
	}

	public String getFailures() {
		return bot().text(2).getText();
	}
}
