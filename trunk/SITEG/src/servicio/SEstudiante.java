package servicio;

import interfazdao.IEstudianteDAO;

import java.util.List;

import modelo.Estudiante;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.seguridad.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SEstudiante")
public class SEstudiante {

	@Autowired
	private IEstudianteDAO interfaceEstudiante;

	public void guardar(Estudiante estudiante) {
		interfaceEstudiante.save(estudiante);
	}

	public Estudiante buscarEstudiante(String cedula) {
		return interfaceEstudiante.findOne(cedula);
	}

	public List<Estudiante> buscarEstudiantes() {
		return interfaceEstudiante.findAll();
	}

	public List<Estudiante> buscarActivos() {
		List<Estudiante> estudiantes;
		estudiantes = interfaceEstudiante.findByEstatusTrueOrderByProgramaAsc();
		return estudiantes;
	}
	
	public List<Estudiante> buscarInactivos() {
		List<Estudiante> estudiantes;
		estudiantes = interfaceEstudiante.findByEstatusFalseOrderByProgramaAsc();
		return estudiantes;
	}
	

	public List<Estudiante> buscarSolicitudesEstudiante(
			SolicitudTutoria solicitud) {
		// TODO Auto-generated method stub
		List<Estudiante> estudiantes;
		estudiantes = interfaceEstudiante.findBySolicitudesTutorias(solicitud);
		return estudiantes;
	}

	public Estudiante buscarEstudianteLoggeado(Usuario u) {
		// TODO Auto-generated method stub
		Estudiante estudiante;
		estudiante = interfaceEstudiante.findByUsuario(u);
		return estudiante;
	}

	public List<Estudiante> buscarEstudianteSinUsuario() {
		// TODO Auto-generated method stub
		List<Estudiante> estudiantes;
		estudiantes = interfaceEstudiante.buscarSinUsuario();
		return estudiantes;
	}

	public List<Estudiante> buscarEstudiantePorTeg(Teg teg) {
		List<Estudiante> estudiantes1;
		estudiantes1 = interfaceEstudiante.findByTegs(teg);
		return estudiantes1;
	}

	public List<Estudiante> buscarEstudiantesDelTeg(Teg teg) {
		List<Estudiante> estudiantes;
		estudiantes = interfaceEstudiante.findByTegs(teg);
		return estudiantes;
	}

	public List<Estudiante> buscarEstudiantesPorPrograma(Programa programa) {
		List<Estudiante> estudiantes;
		estudiantes = interfaceEstudiante.findByPrograma(programa);
		return estudiantes;
	}

}
