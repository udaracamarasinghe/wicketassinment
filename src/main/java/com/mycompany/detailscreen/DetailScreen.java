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
import org.joda.time.DateTime;

import com.mycompany.basepage.BasePage;
import com.mycompany.model.Employee;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePicker;

public class DetailScreen extends BasePage {

	private static final long serialVersionUID = -2041198553962047287L;

	public DetailScreen() {
		Calendar calendar = Calendar.getInstance();

		Employee employee = new Employee();

		Form form = new Form("form") {
			@Override
			protected void onSubmit() {
				System.out.println("Form submitted.");
			}
		};
		add(form);

		TextField<String> ageField = new TextField<String>("ageWId", new PropertyModel<String>(employee, "age"));
		ageField.setRequired(true).setLabel(new Model<>("String"));

		form.add(new TextField<String>("nameWId").setRequired(true).setLabel(new Model<>("String")));
		form.add(new DateTextField("dobWId", new PropertyModel<Date>(employee, "dob"), new DateTextFieldConfig()
				.autoClose(true).withView(DateTextFieldConfig.View.Decade).withStartDate(new DateTime().withYear(1900)))
						.add(new OnChangeAjaxBehavior() {

							private static final long serialVersionUID = 1L;

							@Override
							protected void onUpdate(AjaxRequestTarget target) {
								calendar.setTime(employee.getDob());

								LocalDate l1 = LocalDate.of(calendar.get(Calendar.YEAR),
										calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
								LocalDate now1 = LocalDate.now();
								Period diff1 = Period.between(l1, now1);

								employee.setAge(diff1.getYears());

								form.add(ageField);
								target.add(form);

							}
						}));

		form.add(ageField);
		form.add(new TextField<String>("emailWId").setRequired(true).setLabel(new Model<>("String")));
	}

}
