package servicio;

import interfazdao.IAvanceDAO;

import java.util.List;

import modelo.Avance;
import modelo.Teg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SAvance")
public class SAvance {

	@Autowired
	private IAvanceDAO interfazAvance;
	private String[] Estatus = { "Avance Proyecto", "Revision TEG" };

	public void guardar(Avance avance) {
		interfazAvance.save(avance);
	}

	public Avance buscarAvancePorId(long id) {
		// TODO Auto-generated method stub
		Avance avance;
		avance = interfazAvance.findOne(id);
		return avance;
	}

	public List<Avance> buscarActivos() {
		// TODO Auto-generated method stub
		List<Avance> avance;
		avance = interfazAvance.findByEstatusTrue();
		return avance;
	}

	public List<Avance> buscarAvancePorTeg(Teg teg) {
		// TODO Auto-generated method stub
		List<Avance> avances;
		avances = interfazAvance.findByEstatusLikeAndTegOrderByFechaAsc(Estatus[0], teg);
		return avances;
	}

	public List<Avance> buscarRevisionPorTeg(Teg teg) {
		// TODO Auto-generated method stub
		List<Avance> revisiones;
		revisiones = interfazAvance.findByEstatusLikeAndTegOrderByFechaAsc(Estatus[1], teg);
		return revisiones;
	}

}
