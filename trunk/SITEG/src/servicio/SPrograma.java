package servicio;

import interfazdao.IProgramaDAO;

import java.util.List;

import modelo.Profesor;
import modelo.Programa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SPrograma {
	@Autowired
	private IProgramaDAO interfacePrograma;

	public void guardar(Programa programa) {
		interfacePrograma.save(programa);
	}

	public Programa buscar(long id) {
		return interfacePrograma.findOne(id);
	}
	
	public Programa buscarPorId(long idprograma) {
		// TODO Auto-generated method stub
		Programa programa;
		programa = interfacePrograma.findOne(idprograma);
		return programa;
	}
	public List<Programa> listarPrograma() {
		return interfacePrograma.findAll();
	}

	public List<Programa> buscarActivas() {
		List<Programa> programas;
		programas = interfacePrograma.buscarProgramasActiva();
		return programas;
	}

	public Programa buscarPorNombrePrograma(String nombre) {
		Programa programa;
		programa = interfacePrograma.findByNombre(nombre);
		return programa;
	}

	public Programa buscarProgramaPorNombre(String programas) {
		// TODO Auto-generated method stub
		Programa programa;
		programa = interfacePrograma.findByNombreAndEstatusTrue(programas);
		return programa;
	}
	
	public Programa buscarProgramaDeDirector(Profesor profesor){
		Programa programa;
		programa = interfacePrograma.findByDirectorPrograma(profesor);
		return programa;
	}

	public Programa buscarUltimo() {
		// TODO Auto-generated method stub
		Programa programa;
		programa = interfacePrograma.findOne(interfacePrograma.buscarUltimoPrograma());
		return programa;
	}

}
