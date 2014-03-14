package servicio;

import interfazdao.IFactibilidadDAO;

import java.util.List;

import modelo.Factibilidad;
import modelo.Teg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SFactibilidad")
public class SFactibilidad {
	
	@Autowired
	private IFactibilidadDAO interfacefactibilidad;

	public void guardar(Factibilidad factibilidad) {
		interfacefactibilidad.save(factibilidad);
	}

	public Factibilidad buscarIdTeg(long id) {
		return interfacefactibilidad.findOne(id);
	}

	public List<Factibilidad> buscarActivos() {
		List<Factibilidad> factibilidad;
		factibilidad = interfacefactibilidad.buscarFactibilidadActivos();
		return factibilidad;
	}

	public List<Factibilidad> buscarProfesores(Long id) {
		List<Factibilidad> factibilidad1;
		factibilidad1 = interfacefactibilidad.BuscarProfesor(id);
		return factibilidad1;
	}

	public Factibilidad buscarFactibilidadPorTeg(Teg teg) {

		Factibilidad factibilidad;
		factibilidad = interfacefactibilidad.findByTeg(teg);
		return factibilidad;

	}

}
