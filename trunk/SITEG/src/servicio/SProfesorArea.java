package servicio;



import interfazdao.IProfesorAreaDAO;

import java.util.List;


import modelo.AreaInvestigacion;
import modelo.Lapso;
import modelo.Profesor;
import modelo.ProfesorArea;
import modelo.Programa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SProfesorArea {
	
	@Autowired
	private IProfesorAreaDAO interfaceProfesorArea;
	
	public void guardar(List<ProfesorArea> profesorarea){
		interfaceProfesorArea.save(profesorarea);
	}
	
	public List<AreaInvestigacion> buscarAreasDeProfesor(Profesor profesor){
		List<AreaInvestigacion> areas;
		areas = interfaceProfesorArea.buscarAreasProfesor(profesor);
		return areas;
	}
	

}
