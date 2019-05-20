package com.sae.gandhi.spring.utils.builder;

import java.util.ArrayList;
import java.util.List;

import com.sae.gandhi.spring.entity.Usuarios;
import com.sae.gandhi.spring.utils.SaeEnums;
import com.sae.gandhi.spring.vo.UsuariosVO;

public class UsuariosBuilder {

	public static Usuarios createUsuarios(UsuariosVO vo){
		Usuarios usuario = new Usuarios();
		
		usuario.setUsuarioEmail(vo.getUsuarioEmail());
		usuario.setUsuarioNombre(vo.getUsuarioNombre());
		usuario.setUsuarioPassword(vo.getUsuarioPassword());
		usuario.setUsuarioRol(vo.getUsuarioRol()?SaeEnums.Rol.ADMIN.getRolId():SaeEnums.Rol.USER.getRolId());
		usuario.setUsuarioLogin(vo.getUsuarioLogin());
		
		return usuario;
	}
	
	public static UsuariosVO createUsuariosVO(Usuarios usuario){
		UsuariosVO vo = new UsuariosVO();
		
		vo.setUsuarioEmail(usuario.getUsuarioEmail());
		vo.setUsuarioNombre(usuario.getUsuarioNombre());
		vo.setUsuarioPassword(usuario.getUsuarioPassword());
		vo.setUsuarioRol(usuario.getUsuarioRol()==SaeEnums.Rol.ADMIN.getRolId()?true:false);
		vo.setUsuarioLogin(usuario.getUsuarioLogin());
		
		return vo;
	}
	
	public static List<UsuariosVO> createListUsuariosVO(List<Usuarios> lst){
		List<UsuariosVO> lstUsuarios = new ArrayList<>();
		
		for(Usuarios usr : lst){
			lstUsuarios.add(createUsuariosVO(usr));
		}
		
		return lstUsuarios;
		
	}
	
}
