package servicio;

import java.util.List;

import interfazdao.IActividadDAO;
import interfazdao.ITegDAO;

import modelo.Actividad;
import modelo.Requisito;
import modelo.Teg;
import modelo.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class STeg {
	@Autowired
	private ITegDAO interfaceTeg;

	
	public List<Teg> buscarSolicitudRegistroTeg(){
		List<Teg> tegs;
		tegs = interfaceTeg.buscarSolicitudRegistroTeg();
		return tegs;
	}
		
		public Teg buscarTeg(long id){
			return interfaceTeg.findOne(id);
		
	}
		public List<Teg> buscarProyectoFactible() {
			List<Teg> tegs;
			tegs = interfaceTeg.findAll();
			return tegs;	
		
	}
		
		public List<Teg> buscarActivos() {
			List<Teg> tegs;
			tegs = interfaceTeg.findAll();
			return tegs;
		}
}
