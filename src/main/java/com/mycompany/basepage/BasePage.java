package com.mycompany.basepage;

import java.util.Properties;

import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import com.mycompany.detailscreen.DetailScreen;
import com.mycompany.listscreen.ListScreen;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.core.settings.ITheme;

public abstract class BasePage extends GenericWebPage<Void> {

	private static final long serialVersionUID = 7862587404745121363L;

	public BasePage() {

		add(newNavbar("navbar"));

	}

	public Properties getProperties() {
		return com.mycompany.WicketApplication.get().getProperties();
	}

	protected Navbar newNavbar(String markupId) {
		Navbar navbar = new Navbar(markupId);

		navbar.setPosition(Navbar.Position.TOP);
		navbar.setInverted(true);

		navbar.setBrandName(Model.of("Employees"));

		navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,
				new NavbarButton<Void>(ListScreen.class, Model.of("List Screen")),
				new NavbarButton<Void>(DetailScreen.class, Model.of("Detail Screen"))));

		return navbar;
	}

	private void configureTheme(PageParameters pageParameters) {
		StringValue theme = pageParameters.get("theme");

		if (!theme.isEmpty()) {
			IBootstrapSettings settings = Bootstrap.getSettings(getApplication());
			settings.getActiveThemeProvider().setActiveTheme(theme.toString(""));
		}
	}

	protected ITheme activeTheme() {
		IBootstrapSettings settings = Bootstrap.getSettings(getApplication());

		return settings.getActiveThemeProvider().getActiveTheme();
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();

		configureTheme(getPageParameters());
	}

	protected boolean hasNavigation() {
		return false;
	}

}