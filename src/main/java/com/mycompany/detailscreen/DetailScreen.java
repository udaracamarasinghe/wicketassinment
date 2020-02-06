package com.mycompany.detailscreen;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.joda.time.DateTime;

import com.mycompany.basepage.BasePage;
import com.mycompany.model.Employee;
import com.mycompany.model.EmployeeRepo;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePicker;

public class DetailScreen extends BasePage {

	private static final long serialVersionUID = -2041198553962047287L;

	public DetailScreen(final PageParameters parameters) {

		StringValue stringValue = parameters.get("selected");

		final Employee employee;

		if (stringValue.isNull()) {
			employee = new Employee();
		} else {
			employee = EmployeeRepo.getDB().getById(stringValue.toLong());
		}
		Calendar calendar = Calendar.getInstance();

		Form form = new Form("form") {
			@Override
			protected void onSubmit() {
				if (employee.getId() == null) {
					EmployeeRepo.getDB().addEmployee(employee);
				} else {
					EmployeeRepo.getDB().updateEmployee(employee);
				}
			}
		};
		add(form);

		form.add(new TextField<String>("nameWId", new PropertyModel<String>(employee, "name")).setRequired(true));

		form.add(new TextField<String>("emailWId", new PropertyModel<String>(employee, "email")).setRequired(true));

		TextField<Integer> ageField = new TextField<Integer>("ageWId", new PropertyModel<Integer>(employee, "age"));
		ageField.setRequired(true).setLabel(new Model<>("String"));
		form.add(ageField);

		form.add(new DateTextField("dobWId", new PropertyModel<Date>(employee, "dob"),
				new DateTextFieldConfig().autoClose(true).withView(DateTextFieldConfig.View.Decade)
						.withStartDate(new DateTime().withYear(1900)).withEndDate(new DateTime().withYear(2019)))
								.add(new OnChangeAjaxBehavior() {

									private static final long serialVersionUID = 1L;

									@Override
									protected void onUpdate(AjaxRequestTarget target) {
										if (employee != null && employee.getDob() != null) {
											calendar.setTime(employee.getDob());

											LocalDate l1 = LocalDate.of(calendar.get(Calendar.YEAR),
													calendar.get(Calendar.MONTH) + 1,
													calendar.get(Calendar.DAY_OF_MONTH));
											LocalDate now1 = LocalDate.now();
											Period diff1 = Period.between(l1, now1);

											employee.setAge(diff1.getYears());

											form.add(ageField);
											target.add(form);
										}

									}
								}));

	}

}
