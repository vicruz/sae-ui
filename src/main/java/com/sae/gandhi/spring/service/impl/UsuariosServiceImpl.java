package com.sae.gandhi.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sae.gandhi.spring.dao.UsuariosDAO;
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

}
