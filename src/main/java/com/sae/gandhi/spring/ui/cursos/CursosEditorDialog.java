package com.sae.gandhi.spring.ui.cursos;

import java.time.LocalDate;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.sae.gandhi.spring.ui.common.AbstractEditorDialog;
import com.sae.gandhi.spring.vo.CursosVO;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.StringLengthValidator;

public class CursosEditorDialog extends AbstractEditorDialog<CursosVO>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final TextField cursoNameField = new TextField("Nombre");
	private final DatePicker startDatePicker = new DatePicker("Inicio Curso");
	private final DatePicker endDatePicker = new DatePicker("Fin Curso");
	
	public CursosEditorDialog(BiConsumer<CursosVO, Operation> itemSaver,
			Consumer<CursosVO> itemDeleter){
		super("Curso", itemSaver, itemDeleter);
		
		addCursoNameField();
		addDatesField();
	}

	
	private void addCursoNameField(){
		cursoNameField.setLabel("Nombre");
		cursoNameField.setRequired(true);
		cursoNameField.setPreventInvalidInput(true);
		//cursoNameField.setMaxLength(100);
		cursoNameField.getStyle().set("width", "100%");
		cursoNameField.focus();
		
		getFormLayout().add(cursoNameField);
		
		getBinder().forField(cursoNameField)
			.withConverter(String::trim, String::trim) //Quita espacios a la cadena introducida
			.withNullRepresentation("") //cuando no existe un texto, debe haber una validación para el bindeo
			.withValidator(new StringLengthValidator(
                    "El nombre debe ser por lo menos 3 caracteres",
                    3, null))
            .bind(CursosVO::getCursoNombre, CursosVO::setCursoNombre); //Establece setter y getter para su bindeo 
	}
	
	private void addDatesField(){
		//HorizontalLayout hl = new HorizontalLayout();
		startDatePicker.setRequired(true);
		endDatePicker.setRequired(true);
		//hl.add(startDatePicker,endDatePicker);
		
		getFormLayout().add(startDatePicker);
		getFormLayout().add(endDatePicker);
		
		//Codigo para hacer dependiente la fecha final de la fecha inicial
		startDatePicker.addValueChangeListener(event -> {
		    LocalDate selectedDate = event.getValue();
		    LocalDate endDate = endDatePicker.getValue();
		    if (selectedDate != null) {
		        endDatePicker.setMin(selectedDate.plusDays(1));
		        if (endDate == null) {
		            endDatePicker.setOpened(true);
		        } 
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
		
		getBinder().forField(startDatePicker)
//			.withNullRepresentation(localDate) //cuando no existe un texto, debe haber una validación para el bindeo
            .bind(CursosVO::getCursoFechaInicio, CursosVO::setCursoFechaInicio); //Establece setter y getter para su bindeo
		
		getBinder().forField(endDatePicker)
//		.withNullRepresentation(localDate) //cuando no existe un texto, debe haber una validación para el bindeo
        .bind(CursosVO::getCursoFechaFin, CursosVO::setCursoFechaFin); //Establece setter y getter para su bindeo
	}
	
	
	@Override
	protected void confirmDelete() {
		
	}
	
}
