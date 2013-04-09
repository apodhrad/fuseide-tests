package org.jboss.tools.fuse.ui.bot.test;

import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.jboss.tools.fuse.ui.bot.test.editor.CamelEditor;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotNewObjectWizard;

public class FuseBot {

	public static final String FUSE_CATEGORY = "Fuse Plugins for Eclipse";

	public void createNewFuseProject(String archetype) {
		SWTBotNewObjectWizard wizard = new SWTBotNewObjectWizard();
		wizard.open("Fuse Project", FUSE_CATEGORY);
		wizard.next();
		wizard.bot().textWithLabel("Filter:").setText(archetype);
		wizard.bot().table().select(0);
		wizard.finishWithWait();
	}

	public void createNewCamelFile(String projectName, String fileName) {
		SWTBotNewObjectWizard wizard = new SWTBotNewObjectWizard();
		wizard.bot().sleep(5 * 1000);
		wizard.open("Camel XML File", FUSE_CATEGORY);
		wizard.bot().text(0).setText("/" + projectName + "/src/main/resources/META-INF/spring");
		wizard.bot().text(1).setText(fileName);
		wizard.finishWithWait();
	}

	public void createNewCamelTest() {
		SWTBotNewObjectWizard wizard = new SWTBotNewObjectWizard();
		wizard.bot().sleep(5 * 1000);
		wizard.open("Camel Test Case", FUSE_CATEGORY);
		wizard.bot().textWithLabel("Source folder:").setText("camel-spring/src/test/java");
		wizard.bot().textWithLabel("Package:").setText("com.mycompany.camel.spring");
		wizard.bot().button("Browse...", 2).click();
		wizard.bot().button("OK").click();
		wizard.finishWithWait();
	}

	public CamelEditor getCamelEditor(String fileName) {
		SWTGefBot bot = new SWTGefBot();
		SWTBotGefEditor editor = bot.gefEditor(fileName);
		return new CamelEditor(editor.getReference(), bot);
	}
}
