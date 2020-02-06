package com.mycompany.basepage;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.mycompany.detailscreen.DetailScreen;
import com.mycompany.model.Employee;

public class ActionPanel extends Panel {

	private static final long serialVersionUID = 1807966176036970309L;

	private Employee selected;

	/**
	 * @param id    component id
	 * @param model model for contact
	 */
	public ActionPanel(String id, IModel<Employee> model) {
		super(id, model);
		add(new Link<Object>("select") {

			private static final long serialVersionUID = 4909750415858066329L;

			@Override
			public void onClick() {
				selected = (Employee) getParent().getDefaultModelObject();

				PageParameters pageParameters = new PageParameters();
				pageParameters.add("selected", selected.getId());

				setResponsePage(DetailScreen.class, pageParameters);
			}

			@Override
			public MarkupContainer setDefaultModel(IModel<?> model) {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}
}
