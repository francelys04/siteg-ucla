package servicio;

import interfazdao.ITegEstatusDAO;
import modelo.TegEstatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class STegEstatus {

	@Autowired
	private ITegEstatusDAO interfaceTegEstatus;

	public void guardar(TegEstatus tegEstatus) {
		// TODO Auto-generated method stub
		interfaceTegEstatus.save(tegEstatus);
	}
}
