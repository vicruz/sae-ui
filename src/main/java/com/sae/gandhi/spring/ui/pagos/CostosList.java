package com.sae.gandhi.spring.ui.pagos;

import java.text.NumberFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sae.gandhi.spring.MainView;
import com.sae.gandhi.spring.service.CostosService;
import com.sae.gandhi.spring.ui.common.AbstractEditorDialog;
import com.sae.gandhi.spring.vo.CostosVO;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "costos", layout = MainView.class)
@PageTitle("Costos")
public class CostosList extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final TextField searchField = new TextField("", "Buscar Costos");
    private final H3 header = new H3("Costos");
    
    private final CostosEditorDialog form = new CostosEditorDialog(this::saveCosto, this::deleteCosto);
    
    private CostosService costosService;
    

    //Grid que contendrá los costos
    private final Grid<CostosVO> grid = new Grid<>();

    @Autowired
    public CostosList(CostosService costosService) {
    	
    	this.costosService = costosService;
        initView();

        addSearchBar();
        addContent();

        updateView();
    }

    private void initView() {
        addClassName("pagos-list");
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
    }

    private void addSearchBar() {
        Div viewToolbar = new Div();
        viewToolbar.addClassName("view-toolbar");

        searchField.setPrefixComponent(new Icon("lumo", "search"));
        searchField.addClassName("view-toolbar__search-field");
//        searchField.addValueChangeListener(e -> updateView());
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

//        Button newButton = new Button("Nuevo Costo", new Icon("lumo", "plus"));
        Button newButton = new Button("Nuevo Costo",event -> form.open(new CostosVO(),
                AbstractEditorDialog.Operation.ADD));
        newButton.setIcon(new Icon(VaadinIcon.PLUS));
        newButton.getElement().setAttribute("theme", "primary");
        newButton.addClassName("view-toolbar__button");
        newButton.addClickListener(e -> form.open(new CostosVO(),
                AbstractEditorDialog.Operation.ADD));

        viewToolbar.add(searchField, newButton);
        add(viewToolbar);
    }

    private void addContent() {
        VerticalLayout container = new VerticalLayout();
        container.setClassName("view-container");
        container.setAlignItems(Alignment.STRETCH);

        grid.addColumn(CostosVO::getCostoNombre).setHeader("Costo").setWidth("6em").setResizable(true);
        grid.addColumn(new NumberRenderer<>(CostosVO::getCostoMonto, NumberFormat.getCurrencyInstance()))
        	.setHeader("Monto").setWidth("4em").setResizable(true);

        //Se envian metodos que cumplen con la funcion requerida
        grid.addColumn(new ComponentRenderer<>(this::createEditButton)).setFlexGrow(0);
        //grid.addColumn(new ComponentRenderer<>(this::createInactiveButton)).setFlexGrow(0);
        grid.setSelectionMode(SelectionMode.SINGLE);
      
        container.add(header, grid);
        add(container);
    }
    
    //Boton de editar
    private Button createEditButton(CostosVO costo) {
        Button edit = new Button("",event -> form.open(costo,
                AbstractEditorDialog.Operation.EDIT));
        edit.setIcon(new Icon(VaadinIcon.EDIT));
        edit.addClassName("review__edit");
        edit.getElement().setAttribute("theme", "tertiary");
        edit.getElement().setAttribute("title", "Editar");
        
        return edit;
    }
    /*
    //Boton de Eliminar
    private Button createInactiveButton(CostosDTO costo) {
        Button edit = new Button("", event -> form.open(costo,
                AbstractEditorDialog.Operation.EDIT));
        //edit.setIcon(new Icon("lumo", "close"));
        edit.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE));
        edit.addClassName("review__edit");
        edit.getElement().setAttribute("theme", "tertiary");
        edit.getElement().setAttribute("title", "Desactivar");
        return edit;
    }
    */
    //Carga los datos del grid
    private void updateView() {
        List<CostosVO> lstCostos = costosService.findAllActive();
        grid.setItems(lstCostos);

        if (searchField.getValue().length() > 0) {
            header.setText("Search for “"+ searchField.getValue() +"”");
        } else {
            header.setText("Costos");
        }
    }
    
    //Metodo de salvar
    private void saveCosto(CostosVO costos,
            AbstractEditorDialog.Operation operation) {
    	String operationKind;
    	if(operation.getNameInText().equals(AbstractEditorDialog.Operation.ADD.getNameInText())){
    		costosService.save(costos);
    		operationKind = " agregado";
    	}else{
    		costosService.update(costos);
    		operationKind = " modificado";
    	}
        Notification.show(
                "Costo " + costos.getCostoNombre() + operationKind , 3000, Position.BOTTOM_END);
        updateView();
    }
    
    //Eliminar
    private void deleteCosto(CostosVO costos) {
    	costosService.deactivate(costos);
    	
        Notification.show("Costo "+costos.getCostoNombre() +" eliminado", 3000, Position.BOTTOM_END);
        updateView();
    }
	
}
