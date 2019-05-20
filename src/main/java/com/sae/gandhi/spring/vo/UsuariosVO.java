package com.sae.gandhi.spring.vo;

import java.io.Serializable;

public class UsuariosVO implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7956124304484621114L;
	private String usuarioEmail;
	private String usuarioNombre;
	private String usuarioPassword;
	private Boolean usuarioRol;
	private String usuarioLogin;
	
	public String getUsuarioEmail() {
		return usuarioEmail;
	}
	public void setUsuarioEmail(String usuarioEmail) {
		this.usuarioEmail = usuarioEmail;
	}
	public String getUsuarioNombre() {
		return usuarioNombre;
	}
	public void setUsuarioNombre(String usuarioNombre) {
		this.usuarioNombre = usuarioNombre;
	}
	public String getUsuarioPassword() {
		return usuarioPassword;
	}
	public void setUsuarioPassword(String usuarioPassword) {
		this.usuarioPassword = usuarioPassword;
	}
	public Boolean getUsuarioRol() {
		return usuarioRol;
	}
	public void setUsuarioRol(Boolean usuarioRol) {
		this.usuarioRol = usuarioRol;
	}
	public String getUsuarioLogin() {
		return usuarioLogin;
	}
	public void setUsuarioLogin(String usuarioLogin) {
		this.usuarioLogin = usuarioLogin;
	}
	@Override
	public String toString() {
		return "UsuariosVO [usuarioEmail=" + usuarioEmail + ", usuarioNombre=" + usuarioNombre + ", usuarioPassword="
				+ usuarioPassword + ", usuarioRol=" + usuarioRol + ", usuarioLogin=" + usuarioLogin + "]";
	}
	
}
