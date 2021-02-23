package com.sae.gandhi.spring.ui.cursos.pagos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.sae.gandhi.spring.ui.common.AbstractEditorDialog;
import com.sae.gandhi.spring.vo.CursoCostosVO;
import com.vaadin.flow.component.HasValue.ValueChangeEvent;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;

public class CursoCostoEditorDialog extends AbstractEditorDialog<CursoCostosVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	private CostosService costosService;
//	private Integer cursoId;
	
	//private ComboBox<CostosVO> comboBox = new ComboBox<>();
	private TextField tfCosto = new TextField("Costo");
	private TextField tfMonto = new TextField("Monto");
	private Checkbox cbUniquePay = new Checkbox("Pago Único");
	private Checkbox cbApplyScholarship = new Checkbox("Aplica Beca");
	private Checkbox cbGeneradeDebit = new Checkbox("Genera Adeudo");
	private ComboBox<Integer> cmbDay = new ComboBox<>();
	
	public CursoCostoEditorDialog(BiConsumer<CursoCostosVO, Operation> itemSaver,
			Consumer<CursoCostosVO> itemDeleter){
		super("Costos", itemSaver, itemDeleter);
//		this.costosService = costosService;
//		this.cursoId = cursoId;
		init();
		tfCosto.focus();
	}
	
	public void init(){
		//addCostoCombobox();
		addTextBoxs();
		addChecks();
	}
	
	//https://vaadin.com/components/vaadin-combo-box/java-examples/using-templates
	/*private void addCostoCombobox(){
		comboBox.setLabel("Costos");
		comboBox.setRequired(true);
		//Muestra los pagos en un template indicando el nombre y debajo el costo de cada curso 
		comboBox.setRenderer(TemplateRenderer.<CostosVO> of(
		        "<div><strong>[[item.nombre]]</strong><br><small>$[[item.monto]]</small></div>")
		        .withProperty("nombre", CostosVO::getCostoNombre)
		        .withProperty("monto", CostosVO::getCostoMonto));
		//Al seleccionar el costo, pone en el combo solamente el nombre
		comboBox.setItemLabelGenerator(CostosVO::getCostoNombre);
		
		List<CostosVO> lstCostos = costosService.findNotInCurso(cursoId);
		comboBox.setItems(lstCostos);
		
		comboBox.addValueChangeListener(event -> {
		    if (event.getSource().isEmpty()) {
		        //No hay nada seleccionado
		    } else {
		      System.out.println(event.getSource());  
		      System.out.println(event.getValue().getCostoId());
		    }
		});
		
		getFormLayout().add(comboBox);
		
		getBinder().forField(comboBox)
            .bind(CursoCostosVO::getCostosVO, CursoCostosVO::setCostosVO); //Establece setter y getter para su bindeo
	}
	*/
	
	private void addTextBoxs() {
		tfCosto.setRequired(true);
		tfCosto.setErrorMessage("Nombre del costo es requerido");
		
		tfMonto.setRequired(true);
		tfMonto.setPattern("\\d+(\\.)?(\\d{1,2})?"); //Formato #0.00
		tfMonto.setPreventInvalidInput(true);
		tfMonto.setErrorMessage("El monto debe ser mayor a $0.00");
		
		getFormLayout().add(tfCosto);
		getFormLayout().add(tfMonto);
		
		getBinder().forField(tfCosto)
        .bind(CursoCostosVO::getCostoNombre, CursoCostosVO::setCostoNombre); 
		
		getBinder().forField(tfMonto)
		.withConverter(
            new StringToBigDecimalConverter(BigDecimal.ZERO, "Debe ingresar un número."))
		.withNullRepresentation(BigDecimal.ZERO)
        .bind(CursoCostosVO::getCostoMonto, CursoCostosVO::setCostoMonto); //Establece setter y getter para su bindeo 
	
	}
	private void addChecks(){
		List<Integer> lstDays = new ArrayList<>();
		
		for(int day = 1; day < 32; day++){
			lstDays.add(day);
		}
		
		cmbDay.setLabel("Día Máximo de Pago");
		cmbDay.setItems(lstDays);
		//cmbDay.setValue(5);
		
		//cbGeneradeDebit.setValue(true);
		cbUniquePay.getElement().setAttribute("title", "Cuando el pago solo se realizará en una única ocasión. Ej. Inscripción");
		cbUniquePay.addValueChangeListener(event -> enableControls(event));
			
		
		getFormLayout().add(cbUniquePay);
		getFormLayout().add(cmbDay);
		getFormLayout().add(cbApplyScholarship);
		getFormLayout().add(cbGeneradeDebit);
		
		getBinder().forField(cbUniquePay)
			.bind(CursoCostosVO::getCursoCostoPagoUnico, CursoCostosVO::setCursoCostoPagoUnico);
		getBinder().forField(cbApplyScholarship)
			.bind(CursoCostosVO::getCursoCostoAplicaBeca, CursoCostosVO::setCursoCostoAplicaBeca);
		getBinder().forField(cbGeneradeDebit)
			.bind(CursoCostosVO::getCursoCostoGeneraAdeudo, CursoCostosVO::setCursoCostoGeneraAdeudo);
		getBinder().forField(cmbDay)
			.bind(CursoCostosVO::getCursoCostoDiaPago, CursoCostosVO::setCursoCostoDiaPago);
	}
	
	
	private void enableControls(ValueChangeEvent<Boolean> event){
		cbApplyScholarship.setValue(false);
		cbGeneradeDebit.setValue(false);
		if(event.getValue()){
			cbApplyScholarship.setEnabled(false);
			cbGeneradeDebit.setEnabled(false);
			cmbDay.setEnabled(false);
		}else{
			cbApplyScholarship.setEnabled(true);
			cbGeneradeDebit.setEnabled(true);
			cmbDay.setEnabled(true);
		}
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
		boolean isValid = true;
		if(tfCosto.getValue()==null || tfCosto.getValue().equals("")) {
			tfCosto.focus();
			isValid =  false;
		}
		if(tfMonto.getValue()==null || tfCosto.getValue().equals("")) { 
			tfMonto.focus();
			isValid = false;
		}
		try {
			if(Double.valueOf(tfMonto.getValue()).compareTo(0D)<=0) {
				tfMonto.focus();
				isValid = false;
			}
		}catch(NumberFormatException e) {
			tfMonto.focus();
			isValid = false;
		}
		
		if(!isValid) {
			Notification.show("Los campos 'Costo' y 'Monto' son obligatorios",5000,Notification.Position.MIDDLE);
		}
		
		return isValid;
	}

}
