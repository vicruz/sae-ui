package com.sae.gandhi.spring.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="USUARIOS")
public class Usuarios {
	
	@Id
	@Column(name="USUARIO_EMAIL")
	@NotNull
	private String usuarioEmail;
	
	@Column(name="USUARIO_NOMBRE")
	@NotNull
	private String usuarioNombre;
	
	@Column(name="USUARIO_PASSWORD")
	@NotNull
	private String usuarioPassword;
	
	@Column(name="USUARIO_ROL")
	@NotNull
	private Integer usuarioRol;

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

	public Integer getUsuarioRol() {
		return usuarioRol;
	}

	public void setUsuarioRol(Integer usuarioRol) {
		this.usuarioRol = usuarioRol;
	}

}
