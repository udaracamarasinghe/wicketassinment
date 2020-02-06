package com.mycompany.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeRepo {

	private static final EmployeeRepo employeeRepo = new EmployeeRepo();;

	private static List<Employee> employees = new ArrayList<Employee>();

	static {
		Employee e1 = new Employee();
		e1.setId(1L);
		e1.setName("Test");
		e1.setEmail("test@test.com");
		e1.setDob(new Date(2000, 3, 3));
		e1.setAge(21);

		employees.add(e1);
	}

	public Employee getById(Long id) {
		for (Employee e : employees) {
			if (e.getId().equals(id)) {
				return e;
			}
		}

		return null;
	}

	public static EmployeeRepo getDB() {
		return employeeRepo;
	}

	public List<Employee> getEmployees(long first, long count) {
		return employees.subList((int) first, (int) (first + count));
	}

	public long getSize() {
		return employees.size();
	}
}
