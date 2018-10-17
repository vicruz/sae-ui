package com.sae.gandhi.spring.ui.cursos.pagos;


import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sae.gandhi.spring.service.CostosService;
import com.sae.gandhi.spring.service.CursoCostosService;
import com.sae.gandhi.spring.vo.CursoCostosVO;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.ui.Label;

@Controller
public class CursoCostosList extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6134396839514764206L;
	
//	private CursoCostoEditorDialog form;
	
    private CursoCostosService cursoCostoService;
	
    //private final CursosEditorDialog form = new CursosEditorDialog(this::saveCursoCosto, this::deleteCursoCosto);
    private Div viewTab = new Div();
    private final Grid<CursoCostosVO> grid = new Grid<>();
    
    //El autowired en atributos privados no esta funcionando
    @Autowired
    public CursoCostosList(CostosService costosService, CursoCostosService cursoCostoService){
    	this.cursoCostoService = cursoCostoService;
//    	form = new CursoCostoEditorDialog(this::saveCursoCosto, this::deleteCursoCosto, costosService);
    	initView();
        addContent();
//        updateView();
        add(viewTab);
    }
    
    private void initView() {
        addClassName("cursosCostos-list");
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
        viewTab.addClassName("tab-container");
    }
    
    private void addContent() {

//        grid.addColumn(CursoCostosVO::getCostoId).setHeader("Pago").setFlexGrow(0);
        grid.addColumn(CursoCostosVO::getCostoNombre).setHeader("Concepto").setWidth("6em").setResizable(true);
        grid.addColumn(new NumberRenderer<>(CursoCostosVO::getCostoMonto, "$%(,.2f",Locale.US, "$0.00")).setHeader("Monto").setFlexGrow(0);
        //grid.addColumn(new TextRenderer<>(this::createMonthLabel)).setHeader("Mes").setWidth("6em").setResizable(true);
        //grid.addColumn(CursoCostosVO::getCursoCostoAnio).setHeader("Año").setWidth("6em").setResizable(true);
        //grid.addColumn(CursoCostosVO::getCursoCostoFechaLimite).setHeader("Límite de Pago").setWidth("6em").setResizable(true);
        //grid.addColumn(CursosVO::getCursoStatus).setHeader("Estatus").setWidth("6em").setResizable(true);
//        grid.addColumn(new ComponentRenderer<>(this::createDay)).setHeader("Día de Pago").setFlexGrow(0);
        grid.addColumn(CursoCostosVO::getCursoCostoDiaPago).setHeader("Dia de pago").setWidth("6em").setResizable(true);
        
        //Se envian metodos que cumplen con la funcion requerida
        grid.addColumn(new ComponentRenderer<>(this::createUniqueIcon)).setHeader("Único").setFlexGrow(0);
        grid.addColumn(new ComponentRenderer<>(this::createScholarshipIcon)).setHeader("Beca").setFlexGrow(0);
        grid.addColumn(new ComponentRenderer<>(this::createDebitIcon)).setHeader("Adeudo").setFlexGrow(0);
        grid.addColumn(new ComponentRenderer<>(this::createEditButton)).setHeader("Editar").setFlexGrow(0);
        grid.addColumn(new ComponentRenderer<>(this::createDeleteButton)).setHeader("Borrar").setFlexGrow(0);
        grid.setSelectionMode(SelectionMode.SINGLE);
        
        grid.setWidth("80vw");
        
        viewTab.add(grid);
    }

    //Obtiene el mes correspondiente al pago
  /*  private String createMonthLabel(CursoCostosVO cursoCosto){
    	switch(cursoCosto.getCursoCostoMes()){
    	case 1:
    		return SaeEnums.Mes.ENERO.getStatus();
    	case 2:
    		return SaeEnums.Mes.FEBRERO.getStatus();
    	case 3:
    		return SaeEnums.Mes.MARZO.getStatus();
    	case 4:
    		return SaeEnums.Mes.ABRIL.getStatus();
    	case 5:
    		return SaeEnums.Mes.MAYO.getStatus();
    	case 6:
    		return SaeEnums.Mes.JUNIO.getStatus();
    	case 7:
    		return SaeEnums.Mes.JULIO.getStatus();
    	case 8:
    		return SaeEnums.Mes.AGOSTO.getStatus();
    	case 9:
    		return SaeEnums.Mes.SEPTIEMBRE.getStatus();
    	case 10:
    		return SaeEnums.Mes.OCTUBRE.getStatus();
    	case 11:
    		return SaeEnums.Mes.NOVIEMBRE.getStatus();
    	default:
    		return SaeEnums.Mes.DICIEMBRE.getStatus();
    		
    	}
    }
*/    
    
    private Icon createUniqueIcon(CursoCostosVO cursoCosto) {
        Icon icon;
        
        if(cursoCosto.getCursoCostoPagoUnico())
        	icon = new Icon(VaadinIcon.CHECK_CIRCLE_O);
        else
        	icon = new Icon(VaadinIcon.CLOSE_CIRCLE_O);
                
        return icon;
    }
    
    private Icon createScholarshipIcon(CursoCostosVO cursoCosto) {
        Icon icon;
        
        if(cursoCosto.getCursoCostoAplicaBeca())
        	icon = new Icon(VaadinIcon.CHECK_CIRCLE_O);
        else
        	icon = new Icon(VaadinIcon.CLOSE_CIRCLE_O);
                
        return icon;
    }
    
    private Icon createDebitIcon(CursoCostosVO cursoCosto) {
        Icon icon;
        
        if(cursoCosto.getCursoCostoGeneraAdeudo())
        	icon = new Icon(VaadinIcon.CHECK_CIRCLE_O);
        else
        	icon = new Icon(VaadinIcon.CLOSE_CIRCLE_O);
                
        return icon;
    }
    
    private Label createDay(CursoCostosVO cursoCosto) {
    	Label label = new Label();
        if(Objects.nonNull(cursoCosto.getCursoCostoDiaPago()))
        	label.setValue(""+cursoCosto.getCursoCostoDiaPago());
       	return label;
    }
    
    
    //Boton de editar
    private Button createEditButton(CursoCostosVO cursoCosto) {
        Button edit = new Button("");
//        edit.addClickListener(event -> form.open(cursoCosto,
//                AbstractEditorDialog.Operation.EDIT));
//        		{edit.getUI().ifPresent(ui -> ui.navigate("cursos/edit/"+curso.getCursoId()));});
        edit.setIcon(new Icon(VaadinIcon.EDIT));
        edit.addClassName("review__edit");
        edit.getElement().setAttribute("theme", "tertiary");
        edit.getElement().setAttribute("title", "Editar");
        
        return edit;
    }
    
    //Boton de Eliminar
    private Button createDeleteButton(CursoCostosVO curso) {
        Button edit = new Button();//"",event -> deleteCurso(curso));
//                AbstractEditorDialog.Operation.EDIT));
        //edit.setIcon(new Icon("lumo", "close"));
        edit.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE));
        edit.addClassName("review__edit");
        edit.getElement().setAttribute("theme", "tertiary");
        edit.getElement().setAttribute("title", "Eliminar");
        return edit;
    }
  /*  
  //Metodo de salvar
    private void saveCursoCosto(CursoCostosVO cursoCostos,
            AbstractEditorDialog.Operation operation) {
    	String operationKind;
    	if(operation.getNameInText().equals(AbstractEditorDialog.Operation.ADD.getNameInText())){
    		cursoCostoService.save(cursoCostos);
    		operationKind = " agregado";
    	}else{
    		cursoCostoService.update(cursoCostos);
    		operationKind = " modificado";
    	}
        Notification.show(
                "Costo "  + operationKind , 3000, Position.BOTTOM_END);
        updateView();
    }
    
    //Eliminar
    private void deleteCursoCosto(CursoCostosVO cursoCostos) {
    	cursoCostoService.delete(cursoCostos.getCursoCostoId());
    	
        Notification.show("Costo eliminado", 3000, Position.BOTTOM_END);
        updateView();
    }
    */
    public void updateView(Integer cursoId) {
        List<CursoCostosVO> lstCostos = cursoCostoService.findByCurso(cursoId, true);
        grid.setItems(lstCostos);

//        if (searchField.getValue().length() > 0) {
//            header.setText("Search for “"+ searchField.getValue() +"”");
//        } else {
//            header.setText("Costos");
//        }
    }
    
 
}
