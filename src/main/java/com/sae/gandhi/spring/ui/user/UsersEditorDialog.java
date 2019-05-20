package com.sae.gandhi.spring.ui.user;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.sae.gandhi.spring.ui.common.AbstractEditorDialog;
import com.sae.gandhi.spring.vo.AlumnosVO;
import com.sae.gandhi.spring.vo.CostosVO;
import com.sae.gandhi.spring.vo.UsuariosVO;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;

public class UsersEditorDialog extends AbstractEditorDialog<UsuariosVO> {

	
	private final TextField loginField = new TextField("Login");
	private final TextField nameField = new TextField("Nombre");
	private final TextField emailField = new TextField("Email");
	private final PasswordField passwordField = new PasswordField("Password");
	private final PasswordField passwordRepeatField = new PasswordField("Repeat Password");
	private final Checkbox cbAdmin = new Checkbox("Administrador");
	
	protected UsersEditorDialog(BiConsumer<UsuariosVO, Operation> itemSaver,
			Consumer<UsuariosVO> itemDeleter) {
		super("Usuarios", itemSaver, itemDeleter);
		
		addLoginField();
		addNameField();
		addEmailField();
		addPasswordField();
		addCheckbox();
	}
	
	
	private void addLoginField(){
		loginField.setRequired(true);
		loginField.setPreventInvalidInput(true);
		
		getFormLayout().add(loginField);
		
		getBinder().forField(loginField)
		.withConverter(String::trim, String::trim) //Quita espacios a la cadena introducida
		.withNullRepresentation("") //cuando no existe un texto, debe haber una validación para el bindeo
		.withValidator(new StringLengthValidator(
                "El login debe ser por lo menos 3 caracteres",
                3, null))
        .bind(UsuariosVO::getUsuarioLogin, UsuariosVO::setUsuarioLogin); //Establece setter y getter para su bindeo
	}
	
	private void addNameField(){
		nameField.setRequired(true);
		nameField.setPreventInvalidInput(true);
		
		getFormLayout().add(nameField);
		
		getBinder().forField(nameField)
		.withConverter(String::trim, String::trim) //Quita espacios a la cadena introducida
		.withNullRepresentation("") //cuando no existe un texto, debe haber una validación para el bindeo
		.withValidator(new StringLengthValidator(
                "El nombre debe ser por lo menos 3 caracteres",
                3, null))
        .bind(UsuariosVO::getUsuarioNombre, UsuariosVO::setUsuarioNombre); //Establece setter y getter para su bindeo
	}
	
	private void addEmailField(){
		emailField.setRequired(true);
		emailField.setPreventInvalidInput(true);
		
		getFormLayout().add(emailField);
		
		getBinder().forField(emailField)
		.withConverter(String::trim, String::trim) //Quita espacios a la cadena introducida
		.withNullRepresentation("") //cuando no existe un texto, debe haber una validación para el bindeo
		.withValidator(new EmailValidator("Email incorrecto"))
        .bind(UsuariosVO::getUsuarioEmail, UsuariosVO::setUsuarioEmail); //Establece setter y getter para su bindeo		
	}
	
	private void addPasswordField(){
		passwordField.setRequired(true);
		passwordField.setPreventInvalidInput(true);
		
		getFormLayout().add(passwordField);
		
		getBinder().forField(passwordField)
		.withConverter(String::trim, String::trim) //Quita espacios a la cadena introducida
		.withNullRepresentation("") //cuando no existe un texto, debe haber una validación para el bindeo
		.withValidator(new StringLengthValidator(
                "El password debe ser por lo menos 3 caracteres",
                3, null))
        .bind(UsuariosVO::getUsuarioPassword, UsuariosVO::setUsuarioPassword); //Establece setter y getter para su bindeo
	}
	
	private void addCheckbox(){
		cbAdmin.setValue(false);
		
		getFormLayout().add(cbAdmin);
		
		getBinder().forField(cbAdmin)
        .bind(UsuariosVO::getUsuarioRol, UsuariosVO::setUsuarioRol); //Establece setter y getter para su bindeo
	}
	
	
	
	@Override
	protected void confirmDelete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Boolean validateFields() {
		return true;
	}

}
