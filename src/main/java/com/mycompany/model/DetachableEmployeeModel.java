package com.mycompany.model;

import org.apache.wicket.model.LoadableDetachableModel;

public class DetachableEmployeeModel extends LoadableDetachableModel<Employee> {

	private static final long serialVersionUID = -5085886954745585365L;

	private final Long id;

	public DetachableEmployeeModel(Employee c) {
		this(c.getId());
	}

	public DetachableEmployeeModel(Long id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		this.id = id;
	}

	@Override
	protected Employee load() {
		return EmployeeRepod.getDB().getById(id);
	}

	@Override
	public int hashCode() {
		return Long.valueOf(id).hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (obj instanceof DetachableEmployeeModel) {
			DetachableEmployeeModel other = (DetachableEmployeeModel) obj;
			return other.id == id;
		}
		return false;
	}

}