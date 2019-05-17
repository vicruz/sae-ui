package com.sae.gandhi.spring.security;

import com.sae.gandhi.spring.entity.Usuarios;

@FunctionalInterface
public interface CurrentUser {

	Usuarios getUser();
}
