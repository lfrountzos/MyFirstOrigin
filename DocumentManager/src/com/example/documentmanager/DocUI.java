package com.example.documentmanager;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.myPkg.baseDTOs.myEnglish.IrregularVerb;
import com.myPkg.services.myEnglishImpl.IrregularVerbImpl;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.FilesystemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.TextFileProperty;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
@Theme("documentmanager")
public class DocUI extends UI {

	FilesystemContainer docs = new FilesystemContainer(new File("E:\\DocsAndOthers\\DataForApplicationTests\\tmp\\docs"));
	Table docList = new Table("Documents", docs);
	DocEditor docView = new DocEditor();
	
	List<IrregularVerb> lst = new IrregularVerbImpl().getIrregularVerb();
	//Table iregList = new Table("Irregular", this.getContainer(lst));
	Table iregList = new Table("Irregular", null);
	
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = DocUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		HorizontalSplitPanel hSplit = new HorizontalSplitPanel();
		VerticalSplitPanel vSplit = new VerticalSplitPanel();
		
		Button button = new Button("Press this Button");
		
		final GridLayout grid = new GridLayout(1, 2);
		
		grid.addComponent(button, 0, 0);
		grid.addComponent(iregList, 0, 1);
		grid.setComponentAlignment(button, Alignment.TOP_CENTER);
		grid.setComponentAlignment(iregList, Alignment.BOTTOM_CENTER);
		
		setContent(hSplit);
		hSplit.addComponent(vSplit);
		hSplit.addComponent(grid);
		vSplit.addComponent(docList);
		vSplit.addComponent(docView);
		
		docList.setSizeFull();
		iregList.setSizeFull();
		grid.setSizeFull();
		
		docList.addValueChangeListener(new ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				docView.setPropertyDataSource(new TextFileProperty((File) event.getProperty().getValue(), Charset.forName("utf-8")));			
			}			
		});

		docList.setImmediate(true);
		docList.setSelectable(true);
		iregList.setImmediate(true);
		iregList.setSelectable(true);
		
		
		button.addClickListener(new Button.ClickListener() {
		    public void buttonClick(ClickEvent event) {		       
		        List<IrregularVerb> lst = new IrregularVerbImpl().getIrregularVerb();
		        
		        iregList.setContainerDataSource(DocUI.getContainer(lst));
		        Notification.show("Do not press this button again");
		    }
		});

		iregList.addItemClickListener(new ItemClickListener() {

			public void itemClick(ItemClickEvent event) {
				Item item = (Item)event.getItem();
				if (item != null){
					String haystack = (item.getItemProperty("Present").getValue()
                        + " - " + item.getItemProperty("Past Simple").getValue() 
                        + " - " + item.getItemProperty("Past Participle").getValue());
					 Notification.show(haystack);
				}				
			}
		});
//		iregList.addItemSetChangeListener(new ItemSetChangeListener() {
//
//			public void containerItemSetChange(ItemSetChangeEvent event) {
//				Item item = (Item)event.getContainer().getContainerProperty(itemId, propertyId).getItem();
//				if (item != null){
//					String haystack = (item.getItemProperty("Present").getValue()
//                        + " " + item.getItemProperty("Past Simple").getValue() 
//                        + " " + item.getItemProperty("Past Participle").getValue()).toUpperCase();
//					 Notification.show(haystack);
//				}				
//		});
	}

	private static Container getContainer(List<IrregularVerb> lst) { 
		String PROPERTY1 = "Present";
		String PROPERTY2 = "Past Simple";
		String PROPERTY3 = "Past Participle"; 
		final IndexedContainer cont = new IndexedContainer(); 

		cont.addContainerProperty(PROPERTY1, String.class, null); 
		cont.addContainerProperty(PROPERTY2, String.class, null); 
		cont.addContainerProperty(PROPERTY3, String.class, null); 

		for (IrregularVerb irVerb : lst) {
			Item added = cont.addItem(irVerb.getRowId());
			added.getItemProperty(PROPERTY1).setValue(irVerb.getBaseForm());
			added.getItemProperty(PROPERTY2).setValue(irVerb.getPastSimple());
			added.getItemProperty(PROPERTY3).setValue(irVerb.getPastParticiple());
		}
		return cont; 
	} 
}