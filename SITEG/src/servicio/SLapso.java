package servicio;

import java.util.List;

import modelo.Lapso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import interfazdao.ILapsoDAO;

@Service
public class SLapso {
	@Autowired
	private ILapsoDAO interfaceLapso;

	public void guardar(Lapso lapso) {
		interfaceLapso.save(lapso);
	}

	public Lapso buscarLapso(long codigo) {
		return interfaceLapso.findById(codigo);
	}

	public List<Lapso> buscarLapsos() {
		return interfaceLapso.findAll();
	}

	public List<Lapso> buscarActivos() {
		List<Lapso> lapsos;
		lapsos = interfaceLapso.buscarLapsosActivos();
		return lapsos;
	}

	public Lapso buscarPorNombre(String value) {
		// TODO Auto-generated method stub
		Lapso lapso;
		lapso = interfaceLapso.findByNombre(value);
		return lapso;
	}

	public Lapso buscarLapsoVigente() {
		Lapso lapso;
		lapso = interfaceLapso.findByEstatusTrue();
		return lapso;
	}
}
