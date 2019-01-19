package com.sae.gandhi.spring.ui.students.pagos;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.sae.gandhi.spring.ui.common.AbstractEditorDialog;
import com.sae.gandhi.spring.vo.AlumnoPagoVO;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
	private Checkbox cbUseSaldo;
	private TextField txtPaymentAmount;
	private VerticalLayout vlPayment = new VerticalLayout();

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
		StringBuilder sb = new StringBuilder("<ul>");
		
		lbCourse = new Label(cursoNombre);
		lbCourse.getStyle().set("fontWeight", "bold");
		
		sb.append("<li>Monto Total: " + formatter.format(montoTotal) + "</li>");
		sb.append("<li>Monto por pagar: " + formatter.format(montoPorPagar) + "</li>");
		
		lbTotalAmount = new Label();
		lbTotalAmount.getElement().setProperty("innerHTML", sb.toString());
//		lbDebthAmount = new Label("Monto por pagar: " + formatter.format(montoPorPagar));
		
		cbUseSaldo = new Checkbox("Aplicar saldo a favor (" + formatter.format(montoSaldo) + ")");
		getBinder().forField(cbUseSaldo)
		.bind(AlumnoPagoVO::getUsaSaldo, AlumnoPagoVO::setUsaSaldo);
		
		txtPaymentAmount = new TextField("Monto a pagar:");
		txtPaymentAmount.setRequired(true);
		txtPaymentAmount.setPattern("\\d+(\\.)?(\\d{1,2})?"); //Formato #0.00
		txtPaymentAmount.setPreventInvalidInput(true);
		txtPaymentAmount.setPrefixComponent(new Span("$"));
		getBinder().forField(txtPaymentAmount)
			.withConverter(
                new StringToBigDecimalConverter(BigDecimal.ZERO, "Debe ingresar un número."))
			.withNullRepresentation(BigDecimal.ZERO)
			.bind(AlumnoPagoVO::getAlumnoPagoPago, AlumnoPagoVO::setAlumnoPagoPago);
		
		
		
		vlPayment.add(lbCourse,lbTotalAmount);
		if(montoSaldo.compareTo(BigDecimal.ZERO) >0){
			vlPayment.add(cbUseSaldo);
		}
		vlPayment.add(txtPaymentAmount);
		getFormLayout().add(vlPayment);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void confirmDelete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Boolean validateFields() {
		String txtPay = txtPaymentAmount.getValue();
		NumberFormat nfc = NumberFormat.getCurrencyInstance();
		
		if(txtPay==null || txtPay.equals("") || txtPay.equals("0")){
			Notification.show("El monto de pago debe ser mayor a $0.00",5000,Notification.Position.MIDDLE);
			return false;
		}
		
		try{
			BigDecimal payAmount = new BigDecimal(txtPay);
			if(payAmount.compareTo(montoPorPagar)>0){
				String notification = "El monto de pago debe ser menor o igual a " + 
						nfc.format(montoPorPagar);
				Notification.show(notification,5000,Notification.Position.MIDDLE);
				return false;
			}
		}catch(Exception e){
			Notification.show("El monto es inválido",5000,Notification.Position.MIDDLE);
			return false;
		}
		
		return true;
	}


}
