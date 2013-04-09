package org.jboss.tools.fuse.ui.bot.test.suite;

import java.util.List;

import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.swt.finder.finders.MenuFinder;
import org.eclipse.swtbot.swt.finder.matchers.WithText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.Test;

/**
 * Tests if the About screen displays correct version
 * 
 * @author Andrej Podhradsky (apodhrad@redhat.com)
 * 
 */
@Require(perspective = "Java")
public class FuseVersionTest extends SWTTestExt {

	@Test
	public void versionTest() {

		List<MenuItem> menus = new MenuFinder().findMenus(new StartsWith("About"));
		new SWTBotMenu(menus.get(0)).click();

		System.out.println(bot.activeShell().getText());
		System.out.println(bot.styledText().getText());
		bot.sleep(TIME_5S);
		bot.button("OK").click();

		System.out.println("done");
	}

	public class StartsWith extends BaseMatcher<MenuItem> {

		private String text;

		public StartsWith(String text) {
			this.text = text;
		}

		@Override
		public boolean matches(Object item) {
			if (item instanceof MenuItem) {
				SWTBotMenu menu = new SWTBotMenu((MenuItem) item);
				return getMnemonic(menu.getText()).startsWith(text);
			}
			return false;
		}

		@Override
		public void describeTo(Description description) {

		}

		private String getMnemonic(String text) {
			return text.replaceAll("&", "").split("\t")[0];
		}

	}
}
