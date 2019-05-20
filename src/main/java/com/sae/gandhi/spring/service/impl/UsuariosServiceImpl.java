package com.sae.gandhi.spring.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sae.gandhi.spring.dao.UsuariosDAO;
import com.sae.gandhi.spring.entity.Usuarios;
import com.sae.gandhi.spring.service.UsuariosService;
import com.sae.gandhi.spring.utils.SaeEnums;
import com.sae.gandhi.spring.utils.builder.UsuariosBuilder;
import com.sae.gandhi.spring.vo.UsuariosVO;

@Transactional
@Service
public class UsuariosServiceImpl implements UsuariosService {
	
	@Autowired
	private UsuariosDAO usuariosDAO;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void save(UsuariosVO usuariosVO) {
		Usuarios usuarios = UsuariosBuilder.createUsuarios(usuariosVO);
		usuarios.setUsuarioPassword(passwordEncoder.encode(usuarios.getUsuarioPassword()));
		usuariosDAO.save(usuarios);
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

	@Override
	public List<UsuariosVO> findAll() {
		return UsuariosBuilder.createListUsuariosVO(usuariosDAO.findAll());
	}

	@Override
	public void update(UsuariosVO vo) {
		Usuarios usuario = usuariosDAO.findByUsuarioLogin(vo.getUsuarioLogin());
		usuario.setUsuarioEmail(vo.getUsuarioEmail());
		usuario.setUsuarioNombre(vo.getUsuarioNombre());
		usuario.setUsuarioRol(SaeEnums.Rol.USER.getRolId());
		if(vo.getUsuarioRol()){
			usuario.setUsuarioRol(SaeEnums.Rol.ADMIN.getRolId());
		}
		
		usuariosDAO.save(usuario);
	}

}
