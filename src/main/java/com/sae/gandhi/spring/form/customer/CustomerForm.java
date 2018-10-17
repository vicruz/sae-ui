package com.sae.gandhi.spring.form.customer;

import com.sae.gandhi.spring.MainView;
import com.sae.gandhi.spring.entity.Customer;
import com.sae.gandhi.spring.service.CustomerService;
import com.sae.gandhi.spring.utils.CustomerStatus;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;


public class CustomerForm extends FormLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TextField firstName = new TextField("First name");
	private TextField lastName = new TextField("Last name");
	private ComboBox<CustomerStatus> status = new ComboBox<>("Status");
	private Button save = new Button("Save");
	private Button delete = new Button("Delete");
	
	private CustomerService service = CustomerService.getInstance();
	private Customer customer;
	private MainView view;
	
	//Para bindear los campos de la vista
	private Binder<Customer> binder = new Binder<>(Customer.class);
	
	public CustomerForm(MainView view) {
	    this.view = view;
	    binder.bindInstanceFields(this);
	    
	    HorizontalLayout buttons = new HorizontalLayout(save, delete);
	    
	    status.setItems(CustomerStatus.values());
	    save.getElement().setAttribute("theme", "primary");

	    add(firstName, lastName, status, buttons);
	    
	    //asegura que estan deshabilitados los botones 'save' y 'delete' 
	    setCustomer(null);
	    
	    save.addClickListener(e -> this.save());
	    delete.addClickListener(e -> this.delete());
	}
	
	//Mapea en automático los campos
	public void setCustomer(Customer customer) {
		this.customer = customer;
		binder.setBean(customer);
	    boolean enabled = customer != null;
	    save.setEnabled(enabled);
	    delete.setEnabled(enabled);
	    if (enabled) {
	        firstName.focus();
	    }
	}
	
	private void delete() {
	    service.delete(customer);
//	    view.updateList();
	    setCustomer(null);
	}

	private void save() {
	    service.save(customer);
//	    view.updateList();
	    setCustomer(null);
	}
	
}
