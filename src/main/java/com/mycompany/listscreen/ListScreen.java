package com.mycompany.listscreen;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.CSVDataExporter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.ExportToolbar;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.mycompany.basepage.ActionPanel;
import com.mycompany.basepage.BasePage;
import com.mycompany.detailscreen.DetailScreen;
import com.mycompany.model.Employee;
import com.mycompany.model.SortableEmployeeDataProvide;

public class ListScreen extends BasePage {

	private static final long serialVersionUID = -345991361440463702L;

	public ListScreen() {
		SortableEmployeeDataProvide dataProvider = new SortableEmployeeDataProvide();

		Form form = new Form("form");
		add(form);

		List<IColumn<Employee, Long>> columns = new ArrayList<>();

		columns.add(new AbstractColumn<Employee, Long>(Model.of("Actions")) {
			@Override
			public void populateItem(Item<ICellPopulator<Employee>> cellItem, String componentId,
					IModel<Employee> model) {
				cellItem.add(new ActionPanel(componentId, model));
			}
		});

		columns.add(new PropertyColumn<Employee, Long>(new Model<>("ID"), "id"));
		columns.add(new PropertyColumn<Employee, Long>(new Model<>("NAME"), "name"));
		columns.add(new PropertyColumn<Employee, Long>(new Model<>("DOB"), "dob"));
		columns.add(new PropertyColumn<Employee, Long>(new Model<>("AGE"), "age"));
		columns.add(new PropertyColumn<Employee, Long>(new Model<>("EMAIL"), "email"));
		columns.add(new PropertyColumn<Employee, Long>(new Model<>("PHOTO"), "photo"));

		DataTable<Employee, Long> dataTable = new DefaultDataTable<>("table", columns, dataProvider, 8);

		dataTable.addBottomToolbar(new ExportToolbar(dataTable).addDataExporter(new CSVDataExporter()));

		add(dataTable);

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
