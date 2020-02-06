package com.mycompany.basepage;

import org.apache.wicket.markup.html.basic.Label;

public class Page1 extends BasePage {
	public Page1() {
		add(new Label("label1", "This is in the subclass Page1"));
	}
}
