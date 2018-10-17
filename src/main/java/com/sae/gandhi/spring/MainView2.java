package com.sae.gandhi.spring;


import org.springframework.beans.factory.annotation.Autowired;

import com.sae.gandhi.spring.entity.Customer;
import com.sae.gandhi.spring.form.customer.CustomerForm;
import com.sae.gandhi.spring.service.CustomerService;
import com.sae.gandhi.spring.utils.SaeConstants;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

/**
 * The main view contains a simple label element and a template element.
 */
//@HtmlImport("styles/shared-styles.html")
//@Viewport(SaeConstants.VIEWPORT)
//@Route("")
public class MainView2 extends VerticalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7313168790614726485L;
	// Add the next two lines:
    private CustomerService service = CustomerService.getInstance();
    private Grid<Customer> grid = new Grid<>();
    private TextField filterText = new TextField();
    //private CustomerForm form = new CustomerForm(this);

    @Autowired
    public MainView2(ExampleTemplate template) {
        
    	//Filtro
    	filterText.setPlaceholder("Filter by name...");
    	filterText.setValueChangeMode(ValueChangeMode.EAGER);
    	filterText.addValueChangeListener(e -> updateList());
    	
    	Button clearFilterTextBtn = new Button(
    	        new Icon(VaadinIcon.CLOSE_CIRCLE));
    	clearFilterTextBtn.addClickListener(e -> filterText.clear());
    	
    	Button addCustomerBtn = new Button("Add new customer");
    	addCustomerBtn.addClickListener(e -> {
    	    grid.asSingleSelect().clear(); //elimina cualquier seleccion del grid
//    	    form.setCustomer(new Customer()); //Envia un nuevo elemento a la forma
    	});
    	
    	HorizontalLayout filtering = new HorizontalLayout(filterText,
    		    clearFilterTextBtn);
    	
    	HorizontalLayout toolbar = new HorizontalLayout(filtering,
    		    addCustomerBtn);
    	
    	///Grid y agregar elemento
    	grid.setSizeFull();
    	
    	grid.addColumn(Customer::getFirstName).setHeader("First name");
    	grid.addColumn(Customer::getLastName).setHeader("Last name");
    	grid.addColumn(Customer::getStatus).setHeader("Status");
    	
//    	HorizontalLayout main = new HorizontalLayout(grid, form);
//    	main.setAlignItems(Alignment.START);
//    	main.setSizeFull();
    	

//        add(toolbar, main);
        setHeight("100vh");
        updateList();
        
        grid.asSingleSelect().addValueChangeListener(event -> {
//            form.setCustomer(event.getValue());
        });
    	
    }
    
    public void updateList() {
        grid.setItems(service.findAll(filterText.getValue()));
    }

}
