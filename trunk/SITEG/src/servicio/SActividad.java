package servicio;

import java.util.List;

import modelo.Actividad;
import modelo.Lapso;
import modelo.Programa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import interfazdao.IActividadDAO;

@Service
public class SActividad {

	@Autowired
	private IActividadDAO interfaceActividad;

	public void guardar(Actividad actividad) {
		interfaceActividad.save(actividad);
	}

	public Actividad buscarActividad(long id) {
		return interfaceActividad.findById(id);
	}

	public List<Actividad> buscarActivos() {
		List<Actividad> actividades;
		actividades = interfaceActividad.findByEstatusTrueOrderByNombreAsc();
		return actividades;
	}

	public List<Actividad> buscarActividadSinCronograma(Programa programa,
			Lapso lapso) {
		List<Actividad> actividades;
		actividades = interfaceActividad.buscarActividadDisponible(programa,
				lapso);
		return actividades;
	}

	public Actividad buscarActividadPorNombre(String label) {
		Actividad actividad;
		actividad = interfaceActividad.findByNombre(label);
		return actividad;
	}

//	public List<Actividad> ListadoActividades() {
//		List<Actividad> actividades;
//		actividades = interfaceActividad.actividadesPorNombre();
//		return actividades;
//	}

}
