package servicio;

import java.util.List;

import modelo.Actividad;
import modelo.Avance;
import modelo.Estudiante;
import modelo.Teg;
import interfazdao.IAvanceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SAvance {
	
	@Autowired
	private IAvanceDAO interfazAvance;
	private String[] Estatus = { "Avance Proyecto", "Revision TEG"};

	public void guardar(Avance avance){
		interfazAvance.save(avance);
	}
	
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
	
	
	
	/*Busca un teg asociado a un estudiante que tengan estatus avances finalizados*/
	public List<Avance> buscarAvancePorTeg(Teg teg) {
		// TODO Auto-generated method stub
		List<Avance> avances;
		avances = interfazAvance.findByEstatusLikeAndTeg(
				Estatus[0], teg);
		return avances;
	}
	
	
	
	
}
