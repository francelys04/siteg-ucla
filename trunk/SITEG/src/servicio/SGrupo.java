package servicio;

import java.util.List;

import interfazdao.IGrupoDAO;

import modelo.Grupo;
import modelo.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SGrupo {

	@Autowired
	private IGrupoDAO interfazGrupo;
	
	public List<Grupo> buscarActivos(){
		List<Grupo> grupos;
		grupos = interfazGrupo.findByEstatusTrue();
		return grupos;
	}
	
	public List<Grupo> buscarGruposDelUsuario(Usuario usuario){
		List<Grupo> grupos;
		grupos = interfazGrupo.findByUsuarios(usuario);
		return grupos;
	}
	
	public List<Grupo> buscarGruposDisponibles(List<Long> ids){
		List<Grupo> grupos;
		grupos = interfazGrupo.findByIdNotIn(ids);
		return grupos;
	}
}