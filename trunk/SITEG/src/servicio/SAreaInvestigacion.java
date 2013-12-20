package servicio;

import interfazdao.IAreaInvestigacionDAO;

import java.util.List;
import modelo.AreaInvestigacion;
import modelo.Lapso;
import modelo.Profesor;
import modelo.Programa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//servicios de la clase 
@Service
public class SAreaInvestigacion {
@Autowired

private IAreaInvestigacionDAO interfaceArea;
//guarda el area en la base de dato	
	public void guardar(AreaInvestigacion area){
		interfaceArea.save(area);
	}
	//busca un area por nombre	
	public AreaInvestigacion buscarArea(long codigo){
		return interfaceArea.findById(codigo);
	}
	//busca todos los areas y los mete en una lista
	public List<AreaInvestigacion> buscarArea(){
		return interfaceArea.findAll();
	}
	//busca todos los archivos sin eliminar en la base de dato
	public List<AreaInvestigacion> buscarActivos(){
		List<AreaInvestigacion> area;
		area= interfaceArea.findByEstatusTrue();
		return area;
	}
	public AreaInvestigacion buscarAreaPorNombre(String areas) {
		AreaInvestigacion area;
		area = interfaceArea.findByNombre(areas);
		return area;
	}
	
	public List<AreaInvestigacion> buscarAreasSinPrograma(Programa programa, Lapso lapso){
		List<AreaInvestigacion> areas;
		areas = interfaceArea.buscarDisponibles(programa, lapso);
		return areas;
	}
	
	public List<AreaInvestigacion> buscarAreasDelProfesor(Profesor profesor){
		List<AreaInvestigacion> areas;
		areas = interfaceArea.findByProfesores(profesor);
		return areas;
	}
	
	public List<AreaInvestigacion> buscarAreasSinProfesor(List<Long> ids){
		List<AreaInvestigacion> areas;
		areas = interfaceArea.findByIdNotIn(ids);
		return areas;
	}

}

