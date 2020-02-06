package com.mycompany.datatablepage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;

/**
 * simple database for contacts
 * 
 * @author igor
 * 
 */
public class ContactsDatabase {
	private final Map<Long, Contact> map = Collections.synchronizedMap(new HashMap<Long, Contact>());
	private final List<Contact> fnameIdx = Collections.synchronizedList(new ArrayList<Contact>());
	private final List<Contact> lnameIdx = Collections.synchronizedList(new ArrayList<Contact>());
	private final List<Contact> fnameDescIdx = Collections.synchronizedList(new ArrayList<Contact>());
	private final List<Contact> lnameDescIdx = Collections.synchronizedList(new ArrayList<Contact>());

	/**
	 * Constructor
	 * 
	 * @param count number of contacts to generate at startup
	 */
	public ContactsDatabase(int count) {
		for (int i = 0; i < count; i++) {
			add(ContactGenerator.getInstance().generate());
		}
		updateIndecies();
	}

	/**
	 * find contact by id
	 * 
	 * @param id
	 * @return contact
	 */
	public Contact get(long id) {
		Contact c = map.get(id);
		if (c == null) {
			throw new RuntimeException("contact with id [" + id + "] not found in the database");
		}
		return c;
	}

	protected void add(final Contact contact) {
		map.put(contact.getId(), contact);
		fnameIdx.add(contact);
		lnameIdx.add(contact);
		fnameDescIdx.add(contact);
		lnameDescIdx.add(contact);
	}

	/**
	 * select contacts and apply sort
	 * 
	 * @param first
	 * @param count
	 * @param sort
	 * @return list of contacts
	 */
	public List<Contact> find(long first, long count, SortParam sort) {
		return getIndex(sort).subList((int) first, (int) (first + count));
	}

	public List<Contact> getIndex(SortParam sort) {
		if (sort == null) {
			return fnameIdx;
		}

		if (sort.getProperty().equals("firstName")) {
			return sort.isAscending() ? fnameIdx : fnameDescIdx;
		} else if (sort.getProperty().equals("lastName")) {
			return sort.isAscending() ? lnameIdx : lnameDescIdx;
		}
		throw new RuntimeException("unknown sort option [" + sort + "]. valid fields: [firstName], [lastName]");
	}

	/**
	 * @return number of contacts in the database
	 */
	public int getCount() {
		return fnameIdx.size();
	}

	/**
	 * add contact to the database
	 * 
	 * @param contact
	 */
	public void save(final Contact contact) {
		if (contact.getId() == 0) {
			contact.setId(ContactGenerator.getInstance().generateId());
			add(contact);
			updateIndecies();
		} else {
			throw new IllegalArgumentException("contact [" + contact.getFirstName() + "] is already persistent");
		}
	}

	/**
	 * delete contact from the database
	 * 
	 * @param contact
	 */
	public void delete(final Contact contact) {
		map.remove(contact.getId());

		fnameIdx.remove(contact);
		lnameIdx.remove(contact);
		fnameDescIdx.remove(contact);
		lnameDescIdx.remove(contact);

		contact.setId(0);
	}

	private void updateIndecies() {
		Collections.sort(fnameIdx, new Comparator<Contact>() {
			public int compare(Contact arg0, Contact arg1) {
				return (arg0).getFirstName().compareTo((arg1).getFirstName());
			}
		});

		Collections.sort(lnameIdx, new Comparator<Contact>() {
			public int compare(Contact arg0, Contact arg1) {
				return (arg0).getLastName().compareTo((arg1).getLastName());
			}
		});

		Collections.sort(fnameDescIdx, new Comparator<Contact>() {
			public int compare(Contact arg0, Contact arg1) {
				return (arg1).getFirstName().compareTo((arg0).getFirstName());
			}
		});

		Collections.sort(lnameDescIdx, new Comparator<Contact>() {
			public int compare(Contact arg0, Contact arg1) {
				return (arg1).getLastName().compareTo((arg0).getLastName());
			}
		});

	}

}