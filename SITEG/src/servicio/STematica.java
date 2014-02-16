package servicio;

import java.util.List;

import modelo.Profesor;
import modelo.Tematica;
import modelo.AreaInvestigacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import interfazdao.ITematicaDAO;
//servicios de la clase 
@Service
public class STematica {
@Autowired
private ITematicaDAO interfaceTematica;
//guarda el tematica en la base de dato	
public void guardar(Tematica tematica){
	interfaceTematica.save(tematica);
}
//busca por nombre
public Tematica buscarTematica(long codigo){
	return interfaceTematica.findById(codigo);
}
//busca todas las tematicas
public List<Tematica> buscarTematicas(){
	return interfaceTematica.findAll();
}
//busca tematicas no eliminadas ok
public List<Tematica> buscarActivos(){
	List<Tematica> tematicas;
	tematicas= interfaceTematica.findByEstatusTrueOrderByAreaInvestigacionAsc();
	return tematicas;
}

/*
 * Busca las tematicas por Areas*/
public List<Tematica> buscarTematicasDeArea(AreaInvestigacion area2) {
	List<Tematica> tematicas;
	tematicas= interfaceTematica.findByAreaInvestigacion(area2);
	return tematicas;
}

public Tematica buscarTematicaPorNombre(String tematica) {
	// TODO Auto-generated method stub
	Tematica tema;
	tema= interfaceTematica.findByNombre(tematica);
	return tema;
}
public List<Tematica> buscarTematicasDelProfesor(Profesor profesor) {
	List<Tematica> tematicas;
	tematicas = interfaceTematica.findByProfesores(profesor);
	return tematicas;
}

public List<Tematica> buscarTematicasSinProfesor(List<Long> ids) {
	List<Tematica> tematicas;
	tematicas = interfaceTematica.findByIdNotIn(ids);
	return tematicas;
}

}

