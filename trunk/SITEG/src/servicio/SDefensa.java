package servicio;

import interfazdao.IDefensaDAO;

import modelo.Defensa;
import modelo.Teg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SDefensa {

	@Autowired
	IDefensaDAO interfaceDefensa;

	public Defensa buscarDefensaDadoTeg(Teg teg) {
		// TODO Auto-generated method stub
		Defensa defensa;
		defensa = interfaceDefensa.findByTeg(teg);
		return defensa;
	}

	public void guardarDefensa(Defensa defensa) {
		// TODO Auto-generated method stub
		interfaceDefensa.save(defensa);
	}
}
