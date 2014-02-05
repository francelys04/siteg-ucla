package servicio.seguridad;

import interfazdao.seguridad.IUsuarioDAO;

import java.util.List;

import modelo.seguridad.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SUsuario {

	@Autowired
	private IUsuarioDAO interfazUsuario;

	public List<Usuario> buscarActivos() {
		List<Usuario> usuarios;
		usuarios = interfazUsuario.findByEstatusTrue();
		return usuarios;
	}

	public Usuario buscarUsuarioPorId(long codigo) {
		// TODO Auto-generated method stub
		Usuario usuario;
		usuario = interfazUsuario.findOne(codigo);
		return usuario;
	}

	public void guardar(Usuario usuario) {
		// TODO Auto-generated method stub
		interfazUsuario.save(usuario);
	}
	
	public Usuario buscarUsuarioPorNombre(String name) {
		// TODO Auto-generated method stub
		Usuario usuario;
		usuario = interfazUsuario.findByNombre(name);
		return usuario;
	}
}
