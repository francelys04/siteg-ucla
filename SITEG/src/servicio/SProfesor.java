package servicio;

import java.util.List;

import interfazdao.IProfesorDAO;

import modelo.AreaInvestigacion;
import modelo.Profesor;
import modelo.Programa;
import modelo.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SProfesor {

	@Autowired
	private IProfesorDAO interfazProfesor;

	public void guardarProfesor(Profesor profesor) {
		// TODO Auto-generated method stub
		interfazProfesor.save(profesor);
	}

	public Profesor buscarProfesorPorCedula(String cedula) {
		// TODO Auto-generated method stub
		Profesor profesor;
		profesor = interfazProfesor.findByCedula(cedula);
		return profesor;
	}

	public List<Profesor> buscarActivos() {
		// TODO Auto-generated method stub
		List<Profesor> profesores;
		profesores = interfazProfesor.findByEstatusTrue();
		return profesores;
	}

	public Profesor buscarProfesorLoggeado(Usuario u) {
		// TODO Auto-generated method stub
		Profesor profesor;
		profesor = interfazProfesor.findByUsuario(u);
		return profesor;
	}
	
	public List<Profesor> buscarProfesorDelPrograma(Programa programa){
		List<Profesor> profesores;
		profesores= interfazProfesor.buscarProfesorporPrograma(programa);
		return profesores;
	}

	
	
	
	
}