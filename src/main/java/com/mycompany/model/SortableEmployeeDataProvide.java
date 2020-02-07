package com.mycompany.model;

import java.util.Iterator;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

public class SortableEmployeeDataProvide extends SortableDataProvider<Employee, Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6687887719665294289L;

	public SortableEmployeeDataProvide() {

	}

	@Override
	public Iterator<? extends Employee> iterator(long first, long count) {
		// TODO Auto-generated method stub
		return EmployeeRepod.getDB().getEmployees(first, (first + count)).iterator();
	}

	@Override
	public long size() {
		return EmployeeRepod.getDB().getSize();
	}

	@Override
	public IModel<Employee> model(Employee object) {
		return new DetachableEmployeeModel(object);
	}

}
