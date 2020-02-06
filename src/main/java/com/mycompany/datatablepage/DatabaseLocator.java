package com.mycompany.datatablepage;

import org.apache.wicket.Application;

import com.mycompany.WicketApplication;

/**
 * service locator class for contacts database
 * 
 * @author igor
 * 
 */
public class DatabaseLocator {
	/**
	 * @return contacts database
	 */
	public static ContactsDatabase getDatabase() {
		WicketApplication app = (WicketApplication) Application.get();
		return app.getContactsDB();
	}
}
