package com.sae.gandhi.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sae.gandhi.spring.dao.UsuariosDAO;
import com.sae.gandhi.spring.entity.Usuarios;
import com.sae.gandhi.spring.service.UsuariosService;
import com.sae.gandhi.spring.utils.builder.UsuariosBuilder;
import com.sae.gandhi.spring.vo.UsuariosVO;

@Transactional
@Service
public class UsuariosServiceImpl implements UsuariosService {
	
	@Autowired
	private UsuariosDAO usuariosDAO;

	@Override
	public void save(UsuariosVO usuariosVO) {
		usuariosDAO.save(UsuariosBuilder.createUsuarios(usuariosVO));
	}

	@Override
	public UsuariosVO find(String usuarioLogin, String usuarioPassword) {
		UsuariosVO vo = null;
		Usuarios usuarios = usuariosDAO.findByUsuarioLoginAndUsuarioPassword(
				usuarioLogin, usuarioPassword);
		
		if(usuarios != null){
			vo = UsuariosBuilder.createUsuariosVO(usuarios);
		}
		
		return vo;
	}

	@Override
	public UsuariosVO findByUsuarioLogin(String usuarioLogin) {
		Usuarios usuario = usuariosDAO.findByUsuarioLogin(usuarioLogin);
		UsuariosVO vo = null;
		
		if(usuario != null)
			vo = UsuariosBuilder.createUsuariosVO(usuario);
		
		return vo;
	}

}
