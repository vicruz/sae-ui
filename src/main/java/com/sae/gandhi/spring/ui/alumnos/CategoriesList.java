package com.sae.gandhi.spring.ui.alumnos;

import com.sae.gandhi.spring.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "categories", layout = MainView.class)
@PageTitle("Categories List")
public class CategoriesList extends VerticalLayout {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3151919812818601772L;
	private final TextField searchField = new TextField("", "Search categories");
    private final H2 header = new H2("Categories");


    public CategoriesList() {
        initView();

        addSearchBar();
        addContent();
    }

    private void initView() {
        addClassName("categories-list");
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
    }

    private void addSearchBar() {
        Div viewToolbar = new Div();
        viewToolbar.addClassName("view-toolbar");

        searchField.setPrefixComponent(new Icon("lumo", "search"));
        searchField.addClassName("view-toolbar__search-field");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        Button newButton = new Button("New category", new Icon("lumo", "plus"));
        newButton.getElement().setAttribute("theme", "primary");
        newButton.addClassName("view-toolbar__button");

        viewToolbar.add(searchField, newButton);
        add(viewToolbar);
    }

    private void addContent() {
        VerticalLayout container = new VerticalLayout();
        container.setClassName("view-container");
        container.setAlignItems(Alignment.STRETCH);

        Label lbCategory = new Label("Category");
      
        container.add(header, lbCategory);
        add(container);
    }

  
}