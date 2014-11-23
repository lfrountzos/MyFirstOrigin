package com.example.documentmanager;

import java.io.File;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.FilesystemContainer;
import com.vaadin.data.util.TextFileProperty;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("documentmanager")
public class DocUI extends UI {

	FilesystemContainer docs = new FilesystemContainer(new File("E:\\DocsAndOthers\\DataForApplicationTests\\tmp\\docs"));
	Table docList = new Table("Documents", docs);
	Label docView = new Label("", ContentMode.HTML);
	
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = DocUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		
		HorizontalSplitPanel split = new HorizontalSplitPanel();
		setContent(split);
		split.addComponent(docList);
		split.addComponent(docView);
		docList.setSizeFull();

		docList.addValueChangeListener(new ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				docView.setPropertyDataSource(new TextFileProperty((File) event.getProperty().getValue()));
				
			}
		});
		docList.setImmediate(true);
		docList.setSelectable(true);
	}

}