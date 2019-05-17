package com.sae.gandhi.spring.security;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sae.gandhi.spring.dao.UsuariosDAO;
import com.sae.gandhi.spring.entity.Usuarios;
import com.sae.gandhi.spring.utils.SaeEnums;

/**
 * Implements the {@link UserDetailsService}.
 * 
 * This implementation searches for {@link User} entities by the e-mail address
 * supplied in the login screen.
 */
@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UsuariosDAO usuariosDAO;

	@Autowired
	public UserDetailsServiceImpl(UsuariosDAO usuariosDAO) {
		this.usuariosDAO = usuariosDAO;
	}

	/**
	 *
	 * Recovers the {@link User} from the database using the e-mail address supplied
	 * in the login screen. If the user is found, returns a
	 * {@link org.springframework.security.core.userdetails.User}.
	 *
	 * @param username User's e-mail address
	 * 
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuarios user = usuariosDAO.findByUsuarioLogin(username);
		if (null == user) {
			throw new UsernameNotFoundException("No se encuentra el usuario: " + username);
		} else {
			return new org.springframework.security.core.userdetails.User(user.getUsuarioLogin(), user.getUsuarioPassword(),
					Collections.singletonList(new SimpleGrantedAuthority(SaeEnums.Rol.getRol(user.getUsuarioRol()).getRolName())));
		}
	}
}