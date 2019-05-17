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
		usuario.setUsuarioLogin(vo.getUsuarioLogin());
		
		return usuario;
	}
	
	public static UsuariosVO createUsuariosVO(Usuarios usuario){
		UsuariosVO vo = new UsuariosVO();
		
		vo.setUsuarioEmail(usuario.getUsuarioEmail());
		vo.setUsuarioNombre(usuario.getUsuarioNombre());
		vo.setUsuarioPassword(usuario.getUsuarioPassword());
		vo.setUsuarioRol(usuario.getUsuarioRol());
		vo.setUsuarioLogin(usuario.getUsuarioLogin());
		
		return vo;
	}
	
}
