package com.sae.gandhi.spring.ui.cursos.students;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.sae.gandhi.spring.service.AlumnoCursoService;
import com.sae.gandhi.spring.ui.common.AbstractEditorDialog;
import com.sae.gandhi.spring.vo.AlumnosVO;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class CursoStudentEditorDialog extends AbstractEditorDialog<Grid<AlumnosVO>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final TextField searchField = new TextField("", "Buscar Alumnos");

	private AlumnoCursoService alumnoCursoService;
	private Integer cursoId;
	//private List<Integer> lstAlumnosId;
	
	//private Grid<AlumnosVO> grid = new Grid<>();
	private List<AlumnosVO> lstAlumnos;
	
	public CursoStudentEditorDialog(BiConsumer<Grid<AlumnosVO>, Operation> itemSaver,
			Consumer<Grid<AlumnosVO>> itemDeleter, AlumnoCursoService alumnoCursoService, 
			Integer cursoId){
		super("Alumnos", itemSaver, itemDeleter);
		this.alumnoCursoService = alumnoCursoService;
		this.cursoId = cursoId;
	}
	
	public void init(){
		Div viewToolbar = new Div();
		viewToolbar.addClassName("view-toolbar");
		
		searchField.setPrefixComponent(new Icon("lumo", "search"));
		searchField.addClassName("view-toolbar__search-field");
		searchField.addValueChangeListener(e -> updateView());
		searchField.setValueChangeMode(ValueChangeMode.EAGER);
		searchField.setAutoselect(true);
		searchField.setClearButtonVisible(true);
		
		viewToolbar.add(searchField);

		//////////////////////////////////////////////////////////
		
		VerticalLayout container = new VerticalLayout();
        container.setClassName("view-container");
        container.setAlignItems(Alignment.STRETCH);
		
		lstAlumnos = alumnoCursoService.findStudentNotInCurso(cursoId);
		this.getCurrentItem().setItems(lstAlumnos);
		//grid.setItems(lstAlumnos);
		
		this.getCurrentItem().addColumn(AlumnosVO::getAlumnoNombreCompleto).setHeader("Alumno");
		this.getCurrentItem().setSelectionMode(SelectionMode.MULTI);
		//grid.addColumn(AlumnosVO::getAlumnoNombreCompleto).setHeader("Alumno");
		//grid.setSelectionMode(SelectionMode.MULTI);
		
		this.getCurrentItem().asMultiSelect().addValueChangeListener(event -> {
			//lstAlumnosId = event.getValue().stream().map(AlumnosVO::getAlumnoId).collect(Collectors.toList());
		});
		
		container.add(viewToolbar,this.getCurrentItem());
		add(container);
        
	}
	
	private void updateView() {
    	List<AlumnosVO> lstAlumnosGrid = new ArrayList<>();
        if (searchField.getValue().length() > 0 && !searchField.getValue().trim().equals("")) {
        	for(AlumnosVO vo: lstAlumnos){
        		//String nombre = vo.getAlumnoNombre()+" "+vo.getAlumnoApPaterno()+" "+vo.getAlumnoApMaterno();
        		if(vo.getAlumnoNombreCompleto().toUpperCase().contains(searchField.getValue().toUpperCase()) || 
        				vo.getAlumnoTutor().toUpperCase().contains(searchField.getValue().toUpperCase())){
        			lstAlumnosGrid.add(vo);
        		}
        	}
        } else {
        	lstAlumnosGrid = lstAlumnos;
        }
        //grid.setItems(lstAlumnosGrid);
        this.getCurrentItem().setItems(lstAlumnosGrid);
    }
	
	
	//Siguientes pasos:
	//	* Guardar la información y mostrarla en la lista
	//	* Editar la información desde la lista
	
	@Override
	protected void confirmDelete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Boolean validateFields() {
		// TODO Auto-generated method stub
		return true;
	}

}
