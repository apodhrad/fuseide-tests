package org.jboss.tools.fuse.ui.bot.test.wizard;

import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotNewObjectWizard;

/**
 * Fuse project Creation Wizard
 * 
 * @author apodhrad
 * 
 */
public class FuseProject extends SWTBotNewObjectWizard {

	private String archetype;

	public FuseProject(String archetype) {
		super();
		this.archetype = archetype;
	}

	public void open() {
		open("Fuse Project", "Fuse Plugins for Eclipse");
	}

	@Override
	public void finishWithWait() {
		super.finishWithWait();
		// Wait for downloading artifacts
		bot().sleep(5 * 1000);
		bot().waitWhile(new NonSystemJobRunsCondition(),
				TaskDuration.LONG.getTimeout());
		// Wait for updating indexes
		bot().sleep(5 * 1000);
		bot().waitWhile(new NonSystemJobRunsCondition(),
				TaskDuration.LONG.getTimeout());
	}

	public void execute() {
		open();
		next();
		bot().textWithLabel("Filter:").setText(archetype);
		try {
			bot().table().select(0);
		} catch (Exception e) {
			cancel();
			throw new RuntimeException(archetype + " is not available!");
		}
		finishWithWait();
	}

}
