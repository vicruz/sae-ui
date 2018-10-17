package com.sae.gandhi.spring.ui.pagos;


import java.math.BigDecimal;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.sae.gandhi.spring.ui.common.AbstractEditorDialog;
import com.sae.gandhi.spring.vo.CostosVO;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.validator.StringLengthValidator;

public class CostosEditorDialog extends AbstractEditorDialog<CostosVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7571487691909042470L;
	
	private final TextField costoNameField = new TextField("Nombre");
	private final TextField montoField = new TextField("Monto");

	protected CostosEditorDialog(BiConsumer<CostosVO, Operation> itemSaver,
			Consumer<CostosVO> itemDeleter) {
		super("Costos", itemSaver, itemDeleter);
		
		addCostoNameField();
		addMontoField();
	}

	private void addCostoNameField(){
		costoNameField.setLabel("Nombre");
		costoNameField.setRequired(true);
		costoNameField.setPreventInvalidInput(true);
		
		getFormLayout().add(costoNameField);
		
		getBinder().forField(costoNameField)
			.withConverter(String::trim, String::trim) //Quita espacios a la cadena introducida
			.withNullRepresentation("") //cuando no existe un texto, debe haber una validación para el bindeo
			.withValidator(new StringLengthValidator(
                    "El nombre debe ser por lo menos 3 caracteres",
                    3, null))
            .bind(CostosVO::getCostoNombre, CostosVO::setCostoNombre); //Establece setter y getter para su bindeo 
	}
	
	private void addMontoField(){
		montoField.setLabel("Monto");
		montoField.setRequired(true);
		//montoField.setPattern("\\d+(\\.\\d{1,2})?"); //Formato #0.00
		montoField.setPattern("\\d+(\\.)?(\\d{1,2})?"); //Formato #0.00
		montoField.setPreventInvalidInput(true);
		
		getFormLayout().add(montoField);
		
		getBinder().forField(montoField)
			.withConverter(
                new StringToBigDecimalConverter(BigDecimal.ZERO, "Debe ingresar un número."))
			.withNullRepresentation(BigDecimal.ZERO)
//			.withValidator(new DoubleRangeValidator(
//                "El campo de 'Monto' debe ser mayor a 0.", 1))
            .bind(CostosVO::getCostoMonto, CostosVO::setCostoMonto); //Establece setter y getter para su bindeo 
		
		//Para poner el signo de pesos al inicio del combo
		//String currencySymbol = Currency.getInstance(Locale.US).getSymbol();
		montoField.setPrefixComponent(new Span("$"));
		
	}
	
	@Override
	protected void confirmDelete() {
		openConfirmationDialog("Borrar Costo",
                "Desea borrar el costo '" + getCurrentItem().getCostoNombre() + "'?", "");
		
	}

}
