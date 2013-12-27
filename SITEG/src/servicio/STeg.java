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

	

		
		public Teg buscarTeg(long id){
			return interfaceTeg.findOne(id);
		
	}
		public List<Teg> buscarProyectoFactible() {
			List<Teg> tegs;
			tegs = interfaceTeg.buscarProyectoFactible();
			return tegs;	
		
	}
		//Evaluar Revisiones
		public List<Teg> buscarTegRegistrado() {
			List<Teg> tegs;
			tegs = interfaceTeg.buscarTegRegistrado();
			return tegs;	
		
	}
		public List<Teg> buscarActivos() {
			List<Teg> tegs;
			tegs = interfaceTeg.findAll();
			return tegs;
		}

		public void guardar(Teg objetoTeg1) {
			// TODO Auto-generated method stub
			interfaceTeg.save(objetoTeg1);
		}
		
		
		public List<Teg> BuscarProyectoRegistrado() {

			List<Teg> tegs;
			tegs = interfaceTeg.ProyectoRegistrado();
			return tegs;

		}
		
		public List<Teg> BuscarTegSolicitandoRegistro() {

			List<Teg> tegs;
			tegs = interfaceTeg.TegSolicitandoRegistro();
			return tegs;

		}
		


		
}