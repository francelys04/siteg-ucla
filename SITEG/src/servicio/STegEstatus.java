package servicio;

import interfazdao.ITegEstatusDAO;

import java.util.List;

import modelo.Teg;
import modelo.TegEstatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("STegEstatus")
public class STegEstatus {

	@Autowired
	private ITegEstatusDAO interfaceTegEstatus;

	public void guardar(TegEstatus tegEstatus) {
		// TODO Auto-generated method stub
		interfaceTegEstatus.save(tegEstatus);
	}

	public List<TegEstatus> buscarEstatusSegunTeg(String nombre, List<Teg> teg) {
		List<TegEstatus> tegsEstatus;
		tegsEstatus = interfaceTegEstatus.buscarEstatusSegunTeg(nombre, teg);
		return tegsEstatus;
	}

	public List<TegEstatus> buscarTegEstatusPorNombre(String nombre) {
		List<TegEstatus> tegEstatus;
		tegEstatus = interfaceTegEstatus.findByNombre(nombre);
		return tegEstatus;
	}

	public TegEstatus buscarTegEstatus(String nombre, Teg teg) {
		TegEstatus tegEstatus;
		tegEstatus = interfaceTegEstatus.findByNombreAndTeg(nombre, teg);
		return tegEstatus;
	}

}
