package com.sae.gandhi.spring.service.impl;

import org.springframework.stereotype.Component;

import com.sae.gandhi.spring.service.SessionService;
import com.sae.gandhi.spring.utils.SaeEnums;
import com.sae.gandhi.spring.vo.UsuariosVO;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

@Component
//@VaadinSessionScope
public class SessionServiceImpl implements SessionService{

	private UsuariosVO usuarioVO;
	
	@Override
	public void setUsuarioVO(UsuariosVO usuarioVO) {
		this.usuarioVO = usuarioVO;
		
	}

	@Override
	public UsuariosVO getUsuarioVO() {
		return usuarioVO;
	}

	@Override
	public boolean isAdmin() {
		if(usuarioVO!=null &&
				usuarioVO.getUsuarioRol()==SaeEnums.Rol.ADMIN.getRolId())
			return true;
		
		return false;
	}

}
