package com.mycompany.datatablepage;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ColGroup;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.CSVDataExporter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.ExportToolbar;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

public class DataTablePage extends WebPage {

	public DataTablePage() {
		List<IColumn<Contact, String>> columns = new ArrayList<>();

		columns.add(new PropertyColumn<Contact, String>(new Model<>("ID"), "id") {
			@Override
			public String getCssClass() {
				return "numeric";
			}
		});

		columns.add(new PropertyColumn<Contact, String>(new Model<>("First Name"), "firstName", "firstName"));

		columns.add(new PropertyColumn<Contact, String>(new Model<>("Last Name"), "lastName", "lastName") {
			@Override
			public String getCssClass() {
				return "last-name";
			}
		});

		columns.add(new PropertyColumn<Contact, String>(new Model<>("Home Phone"), "homePhone"));
		columns.add(new PropertyColumn<Contact, String>(new Model<>("Cell Phone"), "cellPhone"));

		SortableContactDataProvider dataProvider = new SortableContactDataProvider();
		DataTable<Contact, String> dataTable = new DefaultDataTable<>("table", columns, dataProvider, 8);

		dataTable.addBottomToolbar(new ExportToolbar(dataTable).addDataExporter(new CSVDataExporter()));

		add(dataTable);

		DataTable<Contact, String> tableWithColGroup = new DataTable<>("tableWithColGroup", columns, dataProvider, 8);
		tableWithColGroup.addTopToolbar(new HeadersToolbar<>(tableWithColGroup, dataProvider));
		add(tableWithColGroup);

		// This is a table that uses ColGroup to style the columns:
		ColGroup colgroup = tableWithColGroup.getColGroup();
		colgroup.add(AttributeModifier.append("style", "border: solid 1px green;"));
		colgroup.addCol(colgroup.new Col(AttributeModifier.append("style", "background-color: lightblue;")));
		colgroup.addCol(colgroup.new Col(AttributeModifier.append("style", "background-color: lightgreen")));
		colgroup.addCol(colgroup.new Col(AttributeModifier.append("style", "background-color: pink")));
		colgroup.addCol(colgroup.new Col(AttributeModifier.append("style", "background-color: yellow")));
		colgroup.addCol(colgroup.new Col(AttributeModifier.append("span", "2"),
				AttributeModifier.append("style", "background-color: #CC6633")));

	}

}