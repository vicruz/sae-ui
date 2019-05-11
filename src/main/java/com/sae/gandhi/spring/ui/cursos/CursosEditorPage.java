package com.sae.gandhi.spring.ui.cursos;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;

import com.sae.gandhi.spring.MainView;
import com.sae.gandhi.spring.service.CostosService;
import com.sae.gandhi.spring.service.CursoCostosService;
import com.sae.gandhi.spring.service.CursosService;
import com.sae.gandhi.spring.ui.common.AbstractEditorDialog;
import com.sae.gandhi.spring.ui.cursos.pagos.CursoCostoEditorDialog;
import com.sae.gandhi.spring.ui.cursos.pagos.CursoCostosList;
import com.sae.gandhi.spring.utils.SaeEnums;
import com.sae.gandhi.spring.vo.CursoCostosVO;
import com.sae.gandhi.spring.vo.CursosVO;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "cursos/edit", layout = MainView.class)
@PageTitle("Cursos")
public class CursosEditorPage extends VerticalLayout implements HasUrlParameter<Integer>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String COSTOS = "Costos";
	private static final String ALUMNOS = "Alumnos";
	private CursoCostoEditorDialog formCostoAdd;
	private Binder<CursosVO> binder = new Binder<>();
	
	private Integer cursoId;
	private CursosVO cursoVO;

	private CursosService cursosService;
	private CursoCostosList cursoCostosList;
	private CostosService costosService;
	private CursoCostosService cursoCostoService;
	
	private final H4 header = new H4("Editar Curso"); 
	private final DatePicker startDatePicker = new DatePicker("Inicio Curso");
	private final DatePicker endDatePicker = new DatePicker("Fin Curso");
	private TextField tfCursoName = new TextField("Nombre");
	private Label lbStatus = new Label();
	private Button btnChangeStatus;
	private Button btnSave;
	private Tabs tabs = new Tabs();
	
	@Autowired
	public CursosEditorPage(CursosService cursosService, CursoCostosList cursoCostosList, 
			CostosService costosService, CursoCostosService cursoCostoService){
		this.cursosService = cursosService;
		this.cursoCostosList = cursoCostosList;
		this.costosService = costosService;
		this.cursoCostoService = cursoCostoService;
	}
	
	@Override
	public void setParameter(BeforeEvent event, Integer parameter) {
		cursoId = parameter;
		//formCostoAdd = new CursoCostoEditorDialog(this::saveCursoCosto, this::deleteCursoCosto, this.costosService, cursoId);
		cursoCostosList.updateView(cursoId);
		loadData();
		addTitle();
		addFields();
		addButtons();
//		addNameField();
//		addDatesField();
		addTabs();
		
		//Iniciar con el focus en el nombre 
		tfCursoName.focus();
	}
	
	private void loadData(){
		cursoVO = cursosService.findById(cursoId);
		//Obtener alumnos
		//Obtener pagos
	}
	
	private void addTitle(){
		HorizontalLayout hl = new HorizontalLayout();
		
		updateLabel();
        
		header.getStyle().set("width", "50%");
        lbStatus.getStyle().set("textAlign", "right")
        	.set("width", "50%")
        	.set("paddingTop","30px");
        hl.add(header, lbStatus);
        hl.setSizeFull();
		add(hl);
		
	}
	
	private void addFields(){
		LocalDate localDate = LocalDate.now();
		HorizontalLayout hl = new HorizontalLayout();
		
		tfCursoName.setLabel("Nombre");
		tfCursoName.setRequired(true);
		tfCursoName.setPreventInvalidInput(true);
		tfCursoName.setValue(cursoVO.getCursoNombre());
		
		binder.forField(tfCursoName)
		.withConverter(String::trim, String::trim) //Quita espacios a la cadena introducida
		.withNullRepresentation("") //cuando no existe un texto, debe haber una validación para el bindeo
		.withValidator(new StringLengthValidator(
                "El nombre debe ser por lo menos 3 caracteres",
                3, null))
        .bind(CursosVO::getCursoNombre, CursosVO::setCursoNombre); //Establece setter y getter para su bindeo
		
		startDatePicker.setValue(cursoVO.getCursoFechaInicio());
		endDatePicker.setValue(cursoVO.getCursoFechaFin());
		
		startDatePicker.setLocale(Locale.UK);
		endDatePicker.setLocale(Locale.UK);
		endDatePicker.setMin(startDatePicker.getValue().plusDays(1));
		
		startDatePicker.setRequired(true);
		startDatePicker.setMin(LocalDate.now());
		endDatePicker.setRequired(true);
		hl.getStyle().set("colspan", "0")
			.set("paddingBottom", "10px");
		hl.add(tfCursoName,startDatePicker,endDatePicker);
		
		//add(startDatePicker);
		//add(endDatePicker);
		add(hl);
		
		//Codigo para hacer dependiente la fecha final de la fecha inicial
		startDatePicker.addValueChangeListener(event -> {
		    LocalDate selectedDate = event.getValue();
		    LocalDate endDate = endDatePicker.getValue();
		    if (selectedDate != null) {
		        endDatePicker.setMin(selectedDate.plusDays(1));
		        if(selectedDate.isEqual(endDate) || selectedDate.isAfter(endDate)){
		        	endDatePicker.setValue(selectedDate.plusDays(1));
		        }
	            endDatePicker.setOpened(true);
		    } else {
		        endDatePicker.setMin(null);
		    }
		});
		
		endDatePicker.addValueChangeListener(event -> {
		    LocalDate selectedDate = event.getValue();
		    if (selectedDate != null) {
		        startDatePicker.setMax(selectedDate.minusDays(1));
		    } else {
		        startDatePicker.setMax(null);
		    }
		});
		
		binder.forField(startDatePicker)
//			.withNullRepresentation(localDate) //cuando no existe un texto, debe haber una validación para el bindeo
            .bind(CursosVO::getCursoFechaInicio, CursosVO::setCursoFechaInicio); //Establece setter y getter para su bindeo
		
		binder.forField(endDatePicker)
//			.withNullRepresentation(localDate) //cuando no existe un texto, debe haber una validación para el bindeo
			.bind(CursosVO::getCursoFechaFin, CursosVO::setCursoFechaFin); //Establece setter y getter para su bindeo
		
		if(localDate.isAfter(cursoVO.getCursoFechaInicio())){
			startDatePicker.setEnabled(false);
			endDatePicker.setEnabled(false);
		}
		
	}
	
	private void addButtons(){
		HorizontalLayout hl = new HorizontalLayout();
		
		btnSave = new Button("Guardar");
		if(cursoVO.getCursoStatus()==SaeEnums.Curso.CANCELADO.getStatusId()){
			btnChangeStatus = new Button("Activar");			
		}else{
			btnChangeStatus = new Button("Cancelar");
		}
		
		btnSave.addClickListener(e -> saveButton());
		btnChangeStatus.addClickListener(e -> cancelButton());
		
		hl.setSizeFull();
		hl.getStyle().set("width", "100%");
		hl.add(btnSave,btnChangeStatus);
		add(hl);
	}
	
	private void updateLabel(){
		switch(cursoVO.getCursoStatus()){
        case 1://SaeEnums.Curso.PREPARADO.getStatusId():
        	lbStatus.setText(SaeEnums.Curso.PREPARADO.getStatus());
        	lbStatus.getStyle().set("color", "blue");
        	if(Objects.nonNull(btnChangeStatus))
        		btnChangeStatus.setText("Cancelar");
        	break;
        case 2://SaeEnums.Curso.ACTIVO.getStatusId():
        	lbStatus.setText(SaeEnums.Curso.ACTIVO.getStatus());
        	lbStatus.getStyle().set("color", "green");
        	if(Objects.nonNull(btnChangeStatus))
        		btnChangeStatus.setText("Cancelar");
        	break;
        case 3://SaeEnums.Curso.FINALIZADO.getStatusId():
        	lbStatus.setText(SaeEnums.Curso.FINALIZADO.getStatus());
        	btnChangeStatus.setEnabled(false);
        	break;
        case 4://SaeEnums.Curso.CANCELADO.getStatusId():
        	lbStatus.setText(SaeEnums.Curso.CANCELADO.getStatus());
        	lbStatus.getStyle().set("color", "red");
        	if(Objects.nonNull(btnChangeStatus)){
        		tfCursoName.setEnabled(false);
        		startDatePicker.setEnabled(false);
        		endDatePicker.setEnabled(false);
        		btnChangeStatus.setEnabled(false);
        		btnSave.setEnabled(false);
        	}
        	break;
        default:
        	break;
        }
	}
	
	
	private void addTabs(){
		HorizontalLayout hlTab = new HorizontalLayout();
		Tab tabCostos = new Tab(COSTOS);
		Tab tabStudents = new Tab(ALUMNOS);
		tabStudents.setEnabled(false);
		
		Button addButton = new Button();
		addButton.setIcon(new Icon(VaadinIcon.PLUS));
		addButton.getStyle().set("borderRadius", "50%"); //Se hace redondo
		addButton.getStyle().set("backgroundColor", "lightgray");
		addButton.addClickListener(event -> addEventButton());
		
		tabs.add(tabCostos, tabStudents);
		tabs.setFlexGrowForEnclosedTabs(1);
		
		hlTab.add(tabs, addButton);
		hlTab.setVerticalComponentAlignment(Alignment.START, tabs);
		hlTab.setVerticalComponentAlignment(Alignment.END, addButton);
		
		Div divCostos = new Div();
		divCostos.add(cursoCostosList);
		divCostos.setHeight("30vh");
		
		Div divStudents = new Div();
		divStudents.setText(ALUMNOS);
		divStudents.setVisible(false);
		
		Map<Tab, Div> tabsToPages = new HashMap<>();
		tabsToPages.put(tabCostos, divCostos);
		tabsToPages.put(tabStudents, divStudents);
//		tabsToPages.put(tab3, page3);
		Div pages = new Div(divCostos, divStudents);//, page3);
		Set<Component> pagesShown = Stream.of(divCostos)
		        .collect(Collectors.toSet());
		
		tabs.addSelectedChangeListener(event -> {
		    pagesShown.forEach(page -> page.setVisible(false));
		    pagesShown.clear();
		    Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
		    selectedPage.setVisible(true);
		    pagesShown.add(selectedPage);
		    
		    System.out.println(tabs.getSelectedTab().getLabel());
		});
		
		
		add(hlTab, pages);
	}
	
	
	private void saveButton(){
		binder.writeBeanIfValid(cursoVO);
		cursosService.update(cursoVO);
		Notification.show("Curso actualizado", 5000, Notification.Position.BOTTOM_END);
	}
	
	private void cancelButton(){
		ConfirmDialog
        .createQuestion()
        .withCaption("Cancelar Curso")
        .withMessage("Deseas cancelar el curso?")
        .withOkButton(() -> {
        	cursoVO.setCursoStatus(SaeEnums.Curso.CANCELADO.getStatusId());
        	cursosService.delete(cursoVO.getCursoId());
        	updateLabel();
            Notification.show("Curso "+cursoVO.getCursoNombre() +" cancelado", 3000, Position.BOTTOM_END);
        }, ButtonOption.focus(), ButtonOption.caption("SI"))
        .withCancelButton(ButtonOption.caption("NO"))
        .open();
	}
	
	///Boton Add de tabs
    private void addEventButton(){
    	if(tabs.getSelectedTab().getLabel().equals(COSTOS)){
    		CursoCostosVO costoVO = new CursoCostosVO(); 
    		costoVO.setCursoId(cursoId);
    		costoVO.setCursoCostoDiaPago(5); //
    		costoVO.setCursoCostoGeneraAdeudo(true);
    		formCostoAdd = new CursoCostoEditorDialog(this::saveCursoCosto, this::deleteCursoCosto, this.costosService, cursoId);
    		formCostoAdd.init();
    		formCostoAdd.open(costoVO, AbstractEditorDialog.Operation.ADD);
    	}else{
    		System.out.println("Add Alumnos");
    	}
    	
	}
	
	
	////////////////////////////////////////////////////////////////
	//Metodos de guardar y eliminar de Costos
	////////////////////////////////////////////////////////////////
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
        cursoCostosList.updateView(cursoId);
    }
    
    //Eliminar
    private void deleteCursoCosto(CursoCostosVO cursoCostos) {
    	cursoCostoService.delete(cursoCostos.getCursoCostoId());
    	
        Notification.show("Costo eliminado", 3000, Position.BOTTOM_END);
        cursoCostosList.updateView(cursoId);
    }
	
}
