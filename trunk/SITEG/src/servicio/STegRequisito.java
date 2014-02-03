package servicio;


import java.util.List;

import interfazdao.ITegRequisitoDAO;

import modelo.Lapso;
import modelo.Programa;
import modelo.Requisito;
import modelo.Teg;
import modelo.TegRequisito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class STegRequisito {
	
	@Autowired
	private ITegRequisitoDAO interfaceTegRequisito;
	
	public void guardar (List<TegRequisito> tegrequisito){
		interfaceTegRequisito.save(tegrequisito);
	}

	
	
	public List<Requisito> buscarTegRequisitoSeleccionados(Teg teg)
	{
			
			List<Requisito> requisitos;
			
			requisitos= interfaceTegRequisito.buscaTegRequisitosSeleccionados(teg);
			return requisitos;
	}
	
	public List<Requisito> buscarTegRequisitoDisponible(Programa programa, Lapso lapso,Teg teg)
	{
			
			List<Requisito> requisitos;
			
			requisitos= interfaceTegRequisito.buscarRequisitosProgramaDisponibles(programa, lapso, teg);
			return requisitos;
	}
}