package com.example.documentmanager;

import java.io.File;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.myPkg.baseDTOs.myEnglish.IrregularVerb;
import com.myPkg.services.myEnglishImpl.IrregularVerbImpl;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.FilesystemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.TextFileProperty;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;

@SuppressWarnings("serial")
@Theme("documentmanager")
public class DocUI extends UI {

	FilesystemContainer docs = new FilesystemContainer(new File("E:\\DocsAndOthers\\DataForApplicationTests\\tmp\\docs"));
	Table docList = new Table("Documents", docs);
	DocEditor docView = new DocEditor();
	
	List<IrregularVerb> lst = new IrregularVerbImpl().getIrregularVerb();
	Table iregList = new Table(this.getContainer(lst));
	
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = DocUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		HorizontalSplitPanel hSplit = new HorizontalSplitPanel();
		VerticalSplitPanel vSplit = new VerticalSplitPanel();
		setContent(hSplit);
		hSplit.addComponent(vSplit);
		hSplit.addComponent(iregList);
		vSplit.addComponent(docList);
		vSplit.addComponent(docView);
		
		docList.setSizeFull();

		docList.addValueChangeListener(new ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				docView.setPropertyDataSource(new TextFileProperty((File) event.getProperty().getValue()));
				
			}
		});
		docList.setImmediate(true);
		docList.setSelectable(true);
	}
	Container getContainer(List<Class<?>> lst) { 
		final IndexedContainer cont = new IndexedContainer(); 

		cont.addContainerProperty(PROPERTY1, String.class, null); 
		cont.addContainerProperty(PROPERTY2, String.class, null); 
		cont.addContainerProperty(CUSTOM, CssLayout.class, null); 

		for (Class<?> class1 : lst) {
			
		}
		for (final List<Class<?>> e : lst) { 
		final Item added = cont.addItem(e.getKey().getId()); 
		added.getItemProperty(PROPERTY1).setValue(e.getKey().getProperty1()); 
		added.getItemProperty(PROPERTY2).setValue(e.getKey().getProperty2()); 
		final CssLayout l = new CssLayout(); 
		if (e.getValue() == Boolean.class) { 
			l.addComponent(new CheckBox("My Check Box of Item id: " + e.getKey().getId())); 
		} else { 
			l.addComponent(new TextField("Textbox for id: " + e.getKey().getId())); 
		} 
		added.getItemProperty(CUSTOM).setValue(l); 
		} 

		return cont; 
	} 
}