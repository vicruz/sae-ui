package com.sae.gandhi.spring.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sae.gandhi.spring.entity.Usuarios;

public interface UsuariosDAO extends JpaRepository<Usuarios, String> {

}
