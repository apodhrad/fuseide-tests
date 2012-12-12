package org.jboss.tools.fuse.ui.bot.test.editor;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.ui.IEditorReference;
import org.jboss.tools.ui.bot.ext.view.PropertiesView;

public class CamelEditor extends SWTBotGefEditor {

	public CamelEditor(IEditorReference reference, SWTWorkbenchBot bot)
			throws WidgetNotFoundException {
		super(reference, bot);
	}

	public void addEndpoint(String uri) {
		clickContextMenu("Add").clickContextMenu("Endpoints").clickContextMenu("Endpoint");
		PropertiesView properties = new PropertiesView();
		properties.show();
		properties.bot().comboBox().setText(uri);
	}

	@Override
	public void save() {
		bot.cTabItem("Source").activate();
		bot.cTabItem("Design").activate();
		super.save();
	}
}
