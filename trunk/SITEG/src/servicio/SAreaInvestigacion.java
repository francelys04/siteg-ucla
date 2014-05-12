package servicio;

import interfazdao.IAreaInvestigacionDAO;

import java.util.List;

import modelo.AreaInvestigacion;
import modelo.Lapso;
import modelo.Programa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SAreaInvestigacion")
public class SAreaInvestigacion {
	@Autowired
	private IAreaInvestigacionDAO interfaceArea;

	public void guardar(AreaInvestigacion area) {
		interfaceArea.save(area);
	}

	public AreaInvestigacion buscarArea(long codigo) {
		return interfaceArea.findOne(codigo);
	}

	public List<AreaInvestigacion> buscarArea() {
		return interfaceArea.findAll();
	}

	public List<AreaInvestigacion> buscarActivos() {
		List<AreaInvestigacion> area;
		area = interfaceArea.findByEstatusTrueOrderByNombreAsc();
		return area;
	}

	public AreaInvestigacion buscarAreaPorNombre(String areas) {
		AreaInvestigacion area;
		area = interfaceArea.findByNombreAllIgnoreCase(areas);
		return area;
	}

	public List<AreaInvestigacion> buscarAreasSinPrograma(Programa programa,
			Lapso lapso) {
		List<AreaInvestigacion> areas;
		areas = interfaceArea.buscarDisponibles(programa, lapso);
		return areas;
	}

	public List<AreaInvestigacion> areasPrograma(Programa programa, Lapso lapso) {
		List<AreaInvestigacion> areas;
		areas = interfaceArea.buscarAreasPrograma(programa, lapso);
		return areas;
	}

}
