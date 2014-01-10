package servicio;

import java.util.List;

import interfazdao.IJuradoDAO;

import modelo.Jurado;
import modelo.Profesor;
import modelo.Teg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SJurado {

	@Autowired
	IJuradoDAO interfaceJurado;
	
	public List<Jurado> buscarJuradoDeTeg(Teg teg) {
		// TODO Auto-generated method stub
		List<Jurado> jurado;
		jurado = interfaceJurado.findByTeg(teg);
		return jurado;
	}

	public void guardar(List<Jurado> jurados) {
		// TODO Auto-generated method stub
		interfaceJurado.save(jurados);
	}
	
	public List<Jurado> buscarTegDeProfesor(Profesor profesor)
	{
		List<Jurado> j;
		j = interfaceJurado.findByProfesor( profesor);
		return j;
		
	}
}
