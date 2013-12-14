package servicio;

import java.util.List;

import interfazdao.IProgramaAreaDAO;

import modelo.AreaInvestigacion;
import modelo.Lapso;
import modelo.Programa;
import modelo.ProgramaArea;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SProgramaArea {

	@Autowired
	private IProgramaAreaDAO interfaceProgramaArea;
	
	public void guardar (List<ProgramaArea> programasAreas){
		interfaceProgramaArea.save(programasAreas);
	}
	
	public List<AreaInvestigacion> buscarAreasDePrograma(Programa programa, Lapso lapso){
		List<AreaInvestigacion> areas;
		areas = interfaceProgramaArea.buscarAreasPrograma(programa, lapso);
		return areas;
	}
}
