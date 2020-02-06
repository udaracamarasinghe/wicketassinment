package com.mycompany.detailscreen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

public class SortableDataProvideImpl extends SortableDataProvider<Contact, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5634873378707786942L;

	private List<Contact> contacts = new ArrayList<Contact>();

	public SortableDataProvideImpl() {
		contacts.add(new Contact("10"));
		contacts.add(new Contact("11"));
		contacts.add(new Contact("12"));
	}

	@Override
	public Iterator<? extends Contact> iterator(long first, long count) {
		// TODO Auto-generated method stub
		return contacts.subList((int) first, (int) (first + count)).iterator();
	}

	@Override
	public long size() {
		// TODO Auto-generated method stub
		return contacts.size();
	}

	@Override
	public IModel<Contact> model(Contact object) {
		return new DetachableContactModel(object);
	}

}
