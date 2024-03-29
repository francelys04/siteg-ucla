package servicio;

import interfazdao.IProgramaRequisitoDAO;

import java.util.List;

import modelo.Lapso;
import modelo.Programa;
import modelo.Requisito;
import modelo.compuesta.ProgramaRequisito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SProgramaRequisito")
public class SProgramaRequisito {

	@Autowired
	private IProgramaRequisitoDAO interfaceProgramaRequisito;

	public void guardar(List<ProgramaRequisito> programasRequisitos) {
		interfaceProgramaRequisito.save(programasRequisitos);
	}

	public List<Requisito> buscarRequisitosEnPrograma(Programa programa,
			Lapso lapso) {
		List<Requisito> requisitos;
		requisitos = interfaceProgramaRequisito.buscarRequisitosPrograma(
				programa, lapso);
		return requisitos;
	}

	public List<Requisito> buscarRequisitos(Programa programa, Lapso lapso) {
		List<Requisito> requisitos;
		requisitos = interfaceProgramaRequisito
				.buscarRequistos(programa, lapso);
		return requisitos;
	}

	public void limpiar(List<ProgramaRequisito> programasRequisito) {
		// TODO Auto-generated method stub
		interfaceProgramaRequisito.delete(programasRequisito);
	}

}
