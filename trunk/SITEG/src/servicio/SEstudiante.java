package servicio;

import java.util.List;

import modelo.Estudiante;
import modelo.Programa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import interfazdao.IEstudianteDAO;
@Service
public class SEstudiante {
@Autowired
private IEstudianteDAO interfaceEstudiante;

public void guardar(Estudiante estudiante){
	interfaceEstudiante.save(estudiante);
}
public Estudiante buscarEstudiante(String cedula){
	return interfaceEstudiante.findOne(cedula);
}
public List<Estudiante> buscarEstudiantes(){
	return interfaceEstudiante.findAll();
}
public List<Estudiante> buscarActivos(){
	List<Estudiante> estudiantes;
	estudiantes= interfaceEstudiante.buscarEstudiantesActivos();
	return estudiantes;
}

}

