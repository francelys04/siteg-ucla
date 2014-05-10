package servicio;

import interfazdao.ILapsoDAO;

import java.util.List;

import modelo.Lapso;
import modelo.Programa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SLapso")
public class SLapso {

	@Autowired
	private ILapsoDAO interfaceLapso;

	public void guardar(Lapso lapso) {
		interfaceLapso.save(lapso);
	}

	public Lapso buscarLapso(long codigo) {
		return interfaceLapso.findOne(codigo);
	}

	public List<Lapso> buscarLapsos() {
		return interfaceLapso.findAll();
	}

	public List<Lapso> buscarActivos() {
		List<Lapso> lapsos;
		lapsos = interfaceLapso.findByEstatusTrueOrderByNombreAsc();
		return lapsos;
	}

	public Lapso buscarLapsoVigente() {
		Lapso lapso;
		lapso = interfaceLapso.buscarLapsoVigente();
		return lapso;
	}

	public Lapso BuscarLapsoActual() {
		Lapso lapso;
		lapso = interfaceLapso.findOne(interfaceLapso.lapsoActual());
		return lapso;
	}
	
	public Lapso buscarPorNombreLapso(String nombre) {
		Lapso lapso;
		lapso = interfaceLapso.findByNombre(nombre);
		return lapso;
	}

	
	
	
}
