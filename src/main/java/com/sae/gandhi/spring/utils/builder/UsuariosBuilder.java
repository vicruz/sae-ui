package com.sae.gandhi.spring.utils.builder;

import com.sae.gandhi.spring.entity.Usuarios;
import com.sae.gandhi.spring.vo.UsuariosVO;

public class UsuariosBuilder {

	public static Usuarios createUsuarios(UsuariosVO vo){
		Usuarios usuario = new Usuarios();
		
		usuario.setUsuarioEmail(vo.getUsuarioEmail());
		usuario.setUsuarioNombre(vo.getUsuarioNombre());
		usuario.setUsuarioPassword(vo.getUsuarioPassword());
		usuario.setUsuarioRol(vo.getUsuarioRol());
		
		return usuario;
	}
	
}
