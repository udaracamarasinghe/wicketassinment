package com.mycompany.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EmployeeRepod {

	private static final EmployeeRepod employeeRepo = new EmployeeRepod();;

	private static List<Employee> employees = new ArrayList<Employee>();

	static {
		Employee e1 = new Employee();
		e1.setId(1L);
		e1.setName("Test");
		e1.setEmail("test@test.com");
		Calendar calendar = Calendar.getInstance();
		calendar.set(2000, 1, 3);
		e1.setDob(calendar.getTime());
		e1.setAge(21);

		employees.add(e1);

		Employee e2 = new Employee();
		e2.setId(2L);
		e2.setName("Test wewere");
		e2.setEmail("test@test.com");
		calendar.set(1900, 1, 3);
		e2.setDob(calendar.getTime());
		e2.setAge(28);

		employees.add(e2);
	}

	public Employee getById(Long id) {
		for (Employee e : employees) {
			if (e.getId().equals(id)) {
				return e;
			}
		}

		return null;
	}

	public static EmployeeRepod getDB() {
		return employeeRepo;
	}

	public List<Employee> getEmployees(long first, long count) {
		return employees.subList((int) first, (int) (first + count));
	}

	public long getSize() {
		return employees.size();
	}

	public Employee addEmployee(Employee employee) {
		Long max = 0L;
		for (Employee e : employees) {
			if (e.getId() > max)
				max = e.getId();
		}

		employee.setId(++max);
		employees.add(employee);

		return employee;
	}

	public void updateEmployee(Employee employee) {
		for (Employee e : employees) {
			if (e.getId().equals(employee.getId())) {
				e.setAge(employee.getAge());
				e.setEmail(employee.getEmail());
				e.setDob(employee.getDob());
				e.setName(employee.getName());
				e.setPhoto(employee.getPhoto());
				return;
			}
		}
	}
}
