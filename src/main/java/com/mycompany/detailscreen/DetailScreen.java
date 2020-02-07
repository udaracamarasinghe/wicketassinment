package com.mycompany.detailscreen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.string.StringValue;
import org.joda.time.DateTime;

import com.mycompany.basepage.BasePage;
import com.mycompany.listscreen.ListScreen;
import com.mycompany.model.Employee;
import com.mycompany.model.EmployeeRepod;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextField;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.DateTextFieldConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.datetime.DatetimePicker;

public class DetailScreen extends BasePage {

	private static final long serialVersionUID = -2041198553962047287L;

	public DetailScreen(final PageParameters parameters) {

		File imagesDir = new File(System.getProperty("user.dir") + "/empimages");
		boolean mkdir = imagesDir.mkdir();

		StringValue stringValue = parameters.get("selected");

		Employee employee;

		if (stringValue.isNull()) {
			employee = new Employee();
		} else {
			employee = EmployeeRepod.getDB().getById(stringValue.toLong());
		}
		Calendar calendar = Calendar.getInstance();

		final FileUploadField fileUpload = new FileUploadField("fileInput");

		Form form = new Form("form") {
			@Override
			protected void onSubmit() {
				final FileUpload uploadedFile = fileUpload.getFileUpload();
				if (uploadedFile != null) {

					// write to a new file
					File newFile = new File(
							System.getProperty("user.dir") + "/empimages/" + uploadedFile.getClientFileName());

					if (newFile.exists()) {
						newFile.delete();
					}

					try {
						newFile.createNewFile();
						uploadedFile.writeTo(newFile);

						info("saved file: " + uploadedFile.getClientFileName());
					} catch (Exception e) {
						throw new IllegalStateException("Error");
					}

					employee.setPhoto(uploadedFile.getClientFileName());
				}

				if (employee.getId() == null) {
					Employee temp = EmployeeRepod.getDB().addEmployee(employee);
					employee.setId(temp.getId());
					setResponsePage(ListScreen.class);
				} else {
					EmployeeRepod.getDB().updateEmployee(employee);
					setResponsePage(ListScreen.class);

				}
			}
		};

		form.setMultiPart(true);

		form.add(fileUpload);

		// Set maximum size to 100K for demo purposes
		form.setMaxSize(Bytes.kilobytes(100));

		// Set maximum size per file to 90K for demo purposes
		form.setFileMaxSize(Bytes.kilobytes(90));

		add(form);

		form.add(new TextField<String>("nameWId", new PropertyModel<String>(employee, "name")).setRequired(true));

		form.add(new TextField<String>("emailWId", new PropertyModel<String>(employee, "email")).setRequired(true));

		TextField<Integer> ageField = new TextField<Integer>("ageWId", new PropertyModel<Integer>(employee, "age"));
		ageField.setRequired(true).setLabel(new Model<>("String"));
		ageField.setOutputMarkupId(true);
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

											target.add(ageField);
										}

									}
								}));

		if (employee.getPhoto() != null && !employee.getPhoto().isEmpty()) {

			try {
				File file = new File(System.getProperty("user.dir") + "/empimages/" + employee.getPhoto());
				boolean canRead = file.canRead();
				boolean exists = file.exists();

				byte[] content = Files.readAllBytes(file.toPath());

				Image image = new Image("photoWId", new Model<IResource>()) {

					private static final long serialVersionUID = -8457850449086490660L;

					@Override
					protected IResource getImageResource() {
						return new DynamicImageResource() {

							private static final long serialVersionUID = 923201517955737928L;

							@Override
							protected byte[] getImageData(final IResource.Attributes attributes) {
								return content;
							}
						};
					}
				};
				image.setOutputMarkupId(true);
				form.add(image);
//
//				form.add(new Image("photoWId", new ContextRelativeResource(
//						System.getProperty("user.dir") + "/empimages/" + employee.getPhoto())));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			form.add(new Image("photoWId", new Model<String>()));
		}

		Button newform = new Button("newButton") {

			private static final long serialVersionUID = 8591729905940501087L;

			@Override
			public void onSubmit() {
				setResponsePage(DetailScreen.class);
			}
		};

		form.add(newform);

	}

}
