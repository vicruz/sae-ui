package com.sae.gandhi.spring.service;

import com.sae.gandhi.spring.vo.UsuariosVO;

public interface SessionService {

	public void setUsuarioVO(UsuariosVO usuarioVo);
	public UsuariosVO getUsuarioVO();
	public boolean isAdmin();
}
