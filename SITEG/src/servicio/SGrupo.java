package servicio;

import java.util.List;

import interfazdao.IGrupoDAO;

import modelo.Grupo;

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
}
