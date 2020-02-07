package com.mycompany;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.Application;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

import com.mycompany.listscreen.ListScreen;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;
import de.agilecoders.wicket.core.settings.CookieThemeProvider;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;

public class WicketApplication extends WebApplication {

	private Properties properties;

	public static WicketApplication get() {
		return (WicketApplication) Application.get();
	}

	public WicketApplication() {
		properties = loadProperties();
	}

	@Override
	public Class<? extends WebPage> getHomePage() {
		return ListScreen.class;
	}

	@Override
	public void init() {
		super.init();

		configureBootstrap();
	}

	public Properties getProperties() {
		return properties;
	}

	private Properties loadProperties() {
		Properties properties = new Properties();
		try {
			InputStream stream = getClass().getResourceAsStream("/config.properties");
			try {
				properties.load(stream);
			} finally {
				IOUtils.closeQuietly(stream);
			}
		} catch (IOException e) {
			throw new WicketRuntimeException(e);
		}
		return properties;
	}

	/**
	 * configures wicket-bootstrap and installs the settings.
	 */
	private void configureBootstrap() {
		final IBootstrapSettings settings = new BootstrapSettings();
		Bootstrap.builder().withBootstrapSettings(settings).install(this);

		settings.setJsResourceFilterName("footer-container").setActiveThemeProvider(new CookieThemeProvider());

		Bootstrap.install(this);
	}
}
