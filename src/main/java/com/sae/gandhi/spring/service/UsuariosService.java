package com.sae.gandhi.spring.service;

import com.sae.gandhi.spring.vo.UsuariosVO;

public interface UsuariosService {

	public void save(UsuariosVO usuariosVO);
	
	public UsuariosVO find(String usuarioLogin, String usuarioPassword);
	public UsuariosVO findByUsuarioLogin(String usuarioLogin);
		
}
