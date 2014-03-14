package servicio;

import interfazdao.ITematicaDAO;

import java.util.List;

import modelo.AreaInvestigacion;
import modelo.Profesor;
import modelo.Tematica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("STematica")
public class STematica {

	@Autowired
	private ITematicaDAO interfaceTematica;

	public void guardar(Tematica tematica) {
		interfaceTematica.save(tematica);
	}

	public Tematica buscarTematica(long codigo) {
		return interfaceTematica.findOne(codigo);
	}

	public List<Tematica> buscarActivos() {
		List<Tematica> tematicas;
		tematicas = interfaceTematica
				.findByEstatusTrueOrderByAreaInvestigacionAsc();
		return tematicas;
	}

	public List<Tematica> buscarTematicasDeArea(AreaInvestigacion area2) {
		List<Tematica> tematicas;
		tematicas = interfaceTematica.findByAreaInvestigacion(area2);
		return tematicas;
	}

	public Tematica buscarTematicaPorNombre(String tematica) {
		// TODO Auto-generated method stub
		Tematica tema;
		tema = interfaceTematica.findByNombre(tematica);
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
