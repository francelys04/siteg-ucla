package servicio;

import interfazdao.IProgramaAreaDAO;

import java.util.List;

import modelo.AreaInvestigacion;
import modelo.Lapso;
import modelo.Programa;
import modelo.compuesta.ProgramaArea;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SProgramaArea")
public class SProgramaArea {

	@Autowired
	private IProgramaAreaDAO interfaceProgramaArea;

	public void guardar(List<ProgramaArea> programasAreas) {
		interfaceProgramaArea.save(programasAreas);
	}

	public List<AreaInvestigacion> buscarAreasDePrograma(Programa programa,
			Lapso lapso) {
		List<AreaInvestigacion> areas;
		areas = interfaceProgramaArea.buscarAreasPrograma(programa, lapso);
		return areas;
	}

	public List<AreaInvestigacion> buscarAreasDePrograma(Programa programa) {
		List<AreaInvestigacion> areas;
		areas = interfaceProgramaArea.buscarAreasPrograma(programa);
		return areas;
	}

	public void limpiar(List<ProgramaArea> programasAreas) {
		// TODO Auto-generated method stub
		interfaceProgramaArea.delete(programasAreas);
	}
}
