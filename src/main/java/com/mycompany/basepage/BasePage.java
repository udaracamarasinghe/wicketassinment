package com.mycompany.basepage;

import com.mycompany.HomePage;
import com.mycompany.detailscreen.DetailScreen;
import com.mycompany.listscreen.ListScreen;
import com.newrelic.api.agent.NewRelic;
import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.markup.html.bootstrap.block.Code;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapExternalLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.DropDownButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuDivider;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuHeader;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.*;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.*;
import de.agilecoders.wicket.core.markup.html.references.BootlintHeaderItem;
import de.agilecoders.wicket.core.markup.html.references.RespondJavaScriptReference;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.core.settings.ITheme;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.filter.FilteredHeaderItem;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//import wicket.markup.html.basic.Label;
//import wicket.markup.html.link.BookmarkablePageLink;

public abstract class BasePage extends GenericWebPage<Void> {
	public BasePage() {

//		MobileViewportMetaTag mvt = new MobileViewportMetaTag("viewport");

		add(newNavbar("navbar"));
//		add(newNavigation("navigation"));
//		add(new Footer("footer"));

//		add(new Code("code-internal"));

//		add(new HeaderResponseContainer("footer-container", "footer-container"));

		// add new relic RUM scripts.
//		add(new Label("newrelic", Model.of(NewRelic.getBrowserTimingHeader())).setEscapeModelStrings(false)
//				.setRenderBodyOnly(true).add(new AttributeModifier("id", "newrelic-rum-header")));
//		add(new Label("newrelic-footer", Model.of(NewRelic.getBrowserTimingFooter())).setEscapeModelStrings(false)
//				.setRenderBodyOnly(true).add(new AttributeModifier("id", "newrelic-rum-footer")));

	}

	/**
	 * @return application properties
	 */
	public Properties getProperties() {
		return com.mycompany.WicketApplication.get().getProperties();
	}

	/**
	 * creates a new {@link Navbar} instance
	 *
	 * @param markupId The components markup id.
	 * @return a new {@link Navbar} instance
	 */
	protected Navbar newNavbar(String markupId) {
		Navbar navbar = new Navbar(markupId);

		navbar.setPosition(Navbar.Position.TOP);
		navbar.setInverted(true);

		// show brand name
		navbar.setBrandName(Model.of("Employees"));

		navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,
				new NavbarButton<Void>(ListScreen.class, Model.of("List Screen")),
				new NavbarButton<Void>(DetailScreen.class, Model.of("Detail Screen"))));

		return navbar;
	}

	/**
	 * sets the theme for the current user.
	 *
	 * @param pageParameters current page parameters
	 */
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

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		response.render(new FilteredHeaderItem(JavaScriptHeaderItem.forReference(ApplicationJavaScript.INSTANCE),
				"footer-container"));
		response.render(RespondJavaScriptReference.headerItem());

		if (!getRequest().getRequestParameters().getParameterValue("bootlint").isNull()) {
			response.render(BootlintHeaderItem.INSTANCE);
		}
	}

	protected boolean hasNavigation() {
		return false;
	}

}