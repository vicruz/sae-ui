package com.sae.gandhi.spring.ui.students.pagos;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.sae.gandhi.spring.ui.common.AbstractEditorDialog;
import com.sae.gandhi.spring.vo.AlumnoPagoVO;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;

public class StudentPaymentDialog extends AbstractEditorDialog<AlumnoPagoVO> {
	
	private Integer alumnoPagoId;
	private String cursoNombre;
	BigDecimal montoTotal;
	BigDecimal montoPorPagar;
	BigDecimal montoSaldo;
	
	private Label lbCourse;
	private Label lbTotalAmount;
	private Label lbDebthAmount;
	private Checkbox cbUseSaldo;
	private TextField txtPaymentAmount;

	protected StudentPaymentDialog(BiConsumer<AlumnoPagoVO, Operation> itemSaver,
			Consumer<AlumnoPagoVO> itemDeleter, Integer alumnoPagoId, String cursoNombre, BigDecimal montoTotal,
			BigDecimal montoPorPagar, BigDecimal montoSaldo) {
		super("Pago", itemSaver, itemDeleter);

		this.alumnoPagoId = alumnoPagoId;
		this.cursoNombre = cursoNombre;
		this.montoTotal = montoTotal;
		this.montoPorPagar = montoPorPagar;
		this.montoSaldo = montoSaldo;
		
		addDataPaymentFields();
	}
	
	private void addDataPaymentFields(){
		
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		
		lbCourse = new Label(cursoNombre);
		lbCourse.getStyle().set("fontWeight", "bold");
		
		lbTotalAmount = new Label("MontoTotal: " + formatter.format(montoTotal));
		lbDebthAmount = new Label("Monto por pagar: " + formatter.format(montoPorPagar));
		
		cbUseSaldo = new Checkbox("Aplicar saldo a favor (" + formatter.format(montoSaldo) + ")");
		getBinder().forField(cbUseSaldo)
		.bind(AlumnoPagoVO::getUsaSaldo, AlumnoPagoVO::setUsaSaldo);
		
		txtPaymentAmount = new TextField("Monto a pagar:");
		txtPaymentAmount.setRequired(true);
		txtPaymentAmount.setPattern("\\d+(\\.)?(\\d{1,2})?"); //Formato #0.00
		txtPaymentAmount.setPreventInvalidInput(true);
		getBinder().forField(txtPaymentAmount)
			.withConverter(
                new StringToBigDecimalConverter(BigDecimal.ZERO, "Debe ingresar un número."))
			.withNullRepresentation(BigDecimal.ZERO)
			.bind(AlumnoPagoVO::getAlumnoPagoPago, AlumnoPagoVO::setAlumnoPagoPago);
		
		
		getFormLayout().add(lbCourse,lbTotalAmount,lbDebthAmount);
		if(montoSaldo.compareTo(BigDecimal.ZERO) >0){
			getFormLayout().add(cbUseSaldo);
		}
		getFormLayout().add(txtPaymentAmount);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void confirmDelete() {
		// TODO Auto-generated method stub
		
	}


}
