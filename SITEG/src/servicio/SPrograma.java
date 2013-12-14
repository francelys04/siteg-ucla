package servicio;

import java.util.List;



import modelo.Programa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import interfazdao.IProgramaDAO;
@Service
public class SPrograma {
@Autowired
private IProgramaDAO interfacePrograma;

public void guardar(Programa programa){
	interfacePrograma.save(programa);
}
public Programa buscar(long id){
	
	return interfacePrograma.findOne(id);
}
public List<Programa> listarPrograma(){
	return interfacePrograma.findAll();
}
public List<Programa> buscarActivas(){
	List<Programa> programas;
	programas= interfacePrograma.buscarProgramasActiva();
	return programas;
}
public Programa buscarPorNombrePrograma(String nombre){
	Programa programa;
	programa= interfacePrograma.findByNombre(nombre);
	return programa;
}

}

