package com.sae.gandhi.spring.ui.cursos;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

import org.claspina.confirmdialog.ButtonOption;
import org.claspina.confirmdialog.ConfirmDialog;
import org.springframework.beans.factory.annotation.Autowired;

import com.sae.gandhi.spring.MainView;
import com.sae.gandhi.spring.service.CursosService;
import com.sae.gandhi.spring.ui.common.AbstractEditorDialog;
import com.sae.gandhi.spring.utils.SaeEnums;
import com.sae.gandhi.spring.vo.CursosVO;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "cursos", layout = MainView.class)
@PageTitle("Cursos")
public class CursosList extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3147280917974043948L;
	
	private TextField searchField = new TextField("", "Buscar Cursos");
    private H3 header = new H3("Cursos");
    
    private CursosService cursosService;
    
    private CursosEditorDialog form = new CursosEditorDialog(this::saveCurso, this::deleteCurso);
    
    private Grid<CursosVO> grid = new Grid<>();
    List<CursosVO> lstCursos;
    //private SessionService sessionService;
    private boolean isAdmin;
    
    @Autowired
    public CursosList(//SessionService sessionService, 
    		CursosService cursosService){
    	this.cursosService = cursosService;
    	//this.sessionService = sessionService;
    	isAdmin = UI.getCurrent().getSession().getAttribute("isAdmin")==null?false:
    		(boolean)UI.getCurrent().getSession().getAttribute("isAdmin");
    	initView();

        addSearchBar();
        addContent();

        loadData();
    }
    
    private void initView() {
        addClassName("cursos-list");
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
    }
    
    private void addSearchBar() {
        Div viewToolbar = new Div();
        viewToolbar.addClassName("view-toolbar");

        searchField.setPrefixComponent(new Icon("lumo", "search"));
        searchField.addClassName("view-toolbar__search-field");
        searchField.addValueChangeListener(e -> updateView());
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        Button newButton = new Button("Nuevo Curso",event -> form.open(new CursosVO(),
                AbstractEditorDialog.Operation.ADD));
        newButton.setIcon(new Icon(VaadinIcon.PLUS));
        newButton.getElement().setAttribute("theme", "primary");
        newButton.addClassName("view-toolbar__button");
        newButton.addClickListener(e -> form.open(new CursosVO(),
                AbstractEditorDialog.Operation.ADD));

        viewToolbar.add(searchField, newButton);
        add(viewToolbar);
    }

    
    private void addContent() {
        VerticalLayout container = new VerticalLayout();
        container.setClassName("view-container");
        container.setAlignItems(Alignment.STRETCH);

        grid.addColumn(CursosVO::getCursoNombre).setHeader("Curso").setWidth("6em").setResizable(true);
        grid.addColumn(new LocalDateRenderer<>(CursosVO::getCursoFechaInicio,DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
        	.setHeader("Inicio").setWidth("6em").setResizable(true);
        grid.addColumn(new LocalDateRenderer<>(CursosVO::getCursoFechaFin,DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)))
        	.setHeader("Fin").setWidth("6em").setResizable(true);
//        grid.addColumn(CursosVO::getInscritos).setHeader("Alumnos").setWidth("6em").setResizable(true);
        //grid.addColumn(CursosVO::getCursoStatus).setHeader("Estatus").setWidth("6em").setResizable(true);
        
        //Se envian metodos que cumplen con la funcion requerida
        grid.addColumn(new ComponentRenderer<>(this::createStatusLabel)).setHeader("Estatus").setWidth("6em").setResizable(true);
        grid.addColumn(new ComponentRenderer<>(this::createEditButton)).setHeader("Editar").setFlexGrow(0);
        grid.addColumn(new ComponentRenderer<>(this::createInactiveButton)).setHeader("Cancelar").setFlexGrow(0);
        grid.addColumn(new ComponentRenderer<>(this::createCopyButton)).setHeader("Copiar").setFlexGrow(0);
        grid.setSelectionMode(SelectionMode.SINGLE);
      
        container.add(header, grid);
        add(container);
    }
    
    //Establece la etiqueta de estatus con formato
    private Label createStatusLabel(CursosVO curso) {
    	
    	Label lbStatus;
    	
        switch(curso.getCursoStatus()){
        case 1://SaeEnums.Curso.PREPARADO.getStatusId():
        	lbStatus = new Label(SaeEnums.Curso.PREPARADO.getStatus());
        	lbStatus.getStyle().set("color", "blue");
        	break;
        case 2://SaeEnums.Curso.ACTIVO.getStatusId():
        	lbStatus = new Label(SaeEnums.Curso.ACTIVO.getStatus());
        	lbStatus.getStyle().set("color", "green");
        	break;
        case 3://SaeEnums.Curso.FINALIZADO.getStatusId():
        	lbStatus = new Label(SaeEnums.Curso.FINALIZADO.getStatus());
        	break;
        case 4://SaeEnums.Curso.CANCELADO.getStatusId():
        	lbStatus = new Label(SaeEnums.Curso.CANCELADO.getStatus());
        	lbStatus.getStyle().set("color", "red");
        	break;
        default:
        	lbStatus = new Label();
        	break;
        }
    	    
        return lbStatus;
    }
    
    //Boton de editar
    private Button createEditButton(CursosVO curso) {
        Button edit = new Button("");
        edit.addClickListener(event -> //form.open(curso,
                //AbstractEditorDialog.Operation.EDIT));
        		{
        			UI.getCurrent().getSession().lock();
        			edit.getUI().ifPresent(ui -> ui.navigate("cursos/edit/"+curso.getCursoId()));
        			UI.getCurrent().getSession().unlock();
        		});
        edit.setIcon(new Icon(VaadinIcon.EDIT));
        edit.addClassName("review__edit");
        edit.getElement().setAttribute("theme", "tertiary");
        edit.getElement().setAttribute("title", "Editar");
        
        if(curso.getCursoStatus()==3 || curso.getCursoStatus()==4){
        	edit.setEnabled(false);
        }
        
        return edit;
    }
    
    //Boton de Eliminar
    private Button createInactiveButton(CursosVO curso) {
        Button edit = new Button("",event -> deleteCurso(curso));
//                AbstractEditorDialog.Operation.EDIT));
        //edit.setIcon(new Icon("lumo", "close"));
        edit.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE));
        edit.addClassName("review__edit");
        edit.getElement().setAttribute("theme", "tertiary");
        edit.getElement().setAttribute("title", "Eliminar");
        
        //Si el usuario NO es ADMIN, se deshabilita
        //if(!sessionService.isAdmin()){
        if(!isAdmin){
        	edit.setEnabled(false);
        }
        
        if(curso.getCursoStatus()==3 || curso.getCursoStatus()==4){
        	edit.setEnabled(false);
        }
        
        return edit;
    }
    
    //Boton de Copiar
    private Button createCopyButton(CursosVO curso) {
        Button copy = new Button("",event -> copyCurso(curso));
//                AbstractEditorDialog.Operation.EDIT));
        //edit.setIcon(new Icon("lumo", "close"));
        copy.setIcon(new Icon(VaadinIcon.COPY));
        copy.addClassName("review__edit");
        copy.getElement().setAttribute("theme", "tertiary");
        copy.getElement().setAttribute("title", "Eliminar");
        
        //Si el usuario NO es ADMIN, se deshabilita
        //if(!sessionService.isAdmin()){
        if(!isAdmin){
        	copy.setEnabled(false);
        }
        
        if(curso.getCursoStatus()==3 || curso.getCursoStatus()==4){
        	copy.setEnabled(false);
        }
        
        return copy;
    }
    
    //Carga los datos del grid
    private void loadData(){
    	lstCursos = cursosService.findAllActive();
        grid.setItems(lstCursos);
    }
    
    private void updateView() {
    	List<CursosVO> lstCursosGrid = new ArrayList<>();
        if (searchField.getValue().length() > 0 && !searchField.getValue().trim().equals("")) {
        	for(CursosVO vo: lstCursos){
        		if(vo.getCursoNombre().toUpperCase().contains(searchField.getValue().toUpperCase())){
        			lstCursosGrid.add(vo);
        		}
        	}
        } else {
        	lstCursosGrid = lstCursos;
        }
        grid.setItems(lstCursosGrid);
    }
    
    //Metodo de salvar
    private void saveCurso(CursosVO curso,
            AbstractEditorDialog.Operation operation) {
    	String operationKind;
    	if(operation.getNameInText().equals(AbstractEditorDialog.Operation.ADD.getNameInText())){
    		cursosService.save(curso);
    		operationKind = " agregado";
    	}else{
    		cursosService.update(curso);
    		operationKind = " modificado";
    	}
        Notification.show(
                "Curso " + curso.getCursoNombre() + operationKind , 3000, Position.BOTTOM_END);
        loadData();
    }
    
    //Eliminar
    private void deleteCurso(CursosVO curso) {
    	
    	ConfirmDialog
        .createQuestion()
        .withCaption("Cancelar Curso")
        .withMessage("Deseas cancelar el curso?")
        .withOkButton(() -> {
        	if(cursosService.delete(curso.getCursoId())){
        		Notification.show("Curso "+curso.getCursoNombre() +" Cancelado", 3000, Position.BOTTOM_END);
        		loadData();
        	}else{
        		Notification.show("El curso "+ curso.getCursoNombre() +" tiene alumnos registrados y no puede ser dado de baja", 
        				3000, Position.BOTTOM_END);
        	}
        }, ButtonOption.focus(), ButtonOption.caption("SI"))
        .withCancelButton(ButtonOption.caption("NO"))
        .open();
    }
    
    //Copiar curso
    private void copyCurso(CursosVO curso) {
    	
    	Dialog dialog = new Dialog();
    	TextField textField = new TextField("Curso");
    	dialog.add(textField);
    	
    	Button buttonCancel = new Button("Cancelar");
    	Button buttonAdd = new Button("Copiar");
    	
    	buttonCancel.addClickListener(e->dialog.close());
    	buttonAdd.addClickListener(e->cloneCurso(curso, textField.getValue(), dialog));
    	
    	buttonCancel.getElement().setAttribute("theme", "error");
    	buttonAdd.getElement().setAttribute("theme", "primary");
    	
    	HorizontalLayout buttonBar = new HorizontalLayout(buttonAdd, buttonCancel);

    	dialog.add(buttonBar);
    	
    	dialog.open();    	
    }
    
    private void cloneCurso(CursosVO cursos, String cursoName, Dialog dialog) {
    	if(cursoName==null || cursoName.isEmpty()) {
    		Notification.show("Debes poner un nombre al nuevo curso", 3000, Position.BOTTOM_END);
    		return;
    	}
    	
    	cursos.setCursoNombre(cursoName);
    	if(cursosService.copyCourse(cursos)) {
    		Notification.show("Curso '" + cursoName + "' creado correctamente", 3000, Position.BOTTOM_END);
    		loadData();
    	}else {
    		Notification.show("ERROR al crear el curso '" + cursoName + "'", 3000, Position.BOTTOM_END);
    	}
    	
    	dialog.close();
    }
    
}
