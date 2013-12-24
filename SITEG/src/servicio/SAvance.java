package servicio;

import java.util.List;
import modelo.Avance;
import interfazdao.IAvanceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SAvance {
	
	@Autowired
	private IAvanceDAO interfazAvance;

	public Avance buscarAvancePorId(long id) {
		// TODO Auto-generated method stub
		Avance avance;
		avance = interfazAvance.findById(id);
		return avance;
	}
	
	public List<Avance> buscarActivos() {
		// TODO Auto-generated method stub
		List<Avance> avance;
		avance = interfazAvance.findByEstatusTrue();
		return avance;
	}
}
