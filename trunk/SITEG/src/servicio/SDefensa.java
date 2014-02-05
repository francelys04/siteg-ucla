package servicio;

import java.util.Date;
import java.util.List;

import interfazdao.IDefensaDAO;

import modelo.AreaInvestigacion;
import modelo.Defensa;
import modelo.Programa;
import modelo.Teg;
import modelo.Tematica;

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
	public List<Defensa> buscarDefensaTegSegunEstatus(String estatus,Date fechaInicio,Date fechaFin){
		List<Defensa> defensas;
		defensas=interfaceDefensa.buscarDefensaTegSegunEstatus(estatus,fechaInicio,fechaFin);
		return defensas;
	}
	
	public List<Defensa> buscarDefensaTegSegunEstatus2(String estatus,Date fechaInicio,Date fechaFin){
		List<Defensa> defensas;
		defensas=interfaceDefensa.buscarDefensaTegSegunEstatus2(estatus,fechaInicio,fechaFin);
		return defensas;
	}

	
	public List<Defensa> buscarDefensaTegSegunEstatusTematica(String estatus,Tematica tematica,Date fechaInicio,Date fechaFin){
		List<Defensa> defensas;
		defensas=interfaceDefensa.buscarDefensaTegSegunEstatusTematica(estatus,tematica,fechaInicio,fechaFin);
		return defensas;
	}
	public List<Defensa> buscarDefensaTegSegunEstatusArea(String estatus,AreaInvestigacion area,Date fechaInicio,Date fechaFin){
		List<Defensa> defensas;
		defensas=interfaceDefensa.buscarDefensaTegSegunEstatusArea(estatus,area,fechaInicio,fechaFin);
		return defensas;
	}
	public List<Defensa> buscarDefensaTegSegunEstatusPrograma(String estatus,Programa programa,Date fechaInicio,Date fechaFin){
		List<Defensa> defensas;
		defensas=interfaceDefensa.buscarDefensaTegSegunEstatusPrograma(estatus,programa,fechaInicio,fechaFin);
		return defensas;
	}
	
	public List<Defensa> buscardefensaPorDosFechas(
		Date fechaInicio, Date fechaFin) {
		return interfaceDefensa.findByFechaBetween(fechaInicio, fechaFin);
	}
	
	
	
	public List<Defensa> buscarDefensaTegSegunTematica(Tematica tematica,Date fechaInicio,Date fechaFin){
		List<Defensa> defensas;
		defensas=interfaceDefensa.buscarDefensaTegSegunTematica(tematica,fechaInicio,fechaFin);
		return defensas;
	}
	public List<Defensa> buscarDefensaTegSegunPrograma(Programa programa,Date fechaInicio,Date fechaFin){
		List<Defensa> defensas;
		defensas=interfaceDefensa.buscarDefensaTegSegunPrograma(programa,fechaInicio,fechaFin);
		return defensas;
	}
	public List<Defensa> buscarDefensaTegSegunArea(AreaInvestigacion area,Date fechaInicio,Date fechaFin){
		List<Defensa> defensas;
		defensas=interfaceDefensa.buscarDefensaTegSegunArea(area,fechaInicio,fechaFin);
		return defensas;
	}
	public List<Defensa> buscarDefensaTeg2(Date fechaInicio,Date fechaFin){
		List<Defensa> defensas;
		defensas=interfaceDefensa.buscarDefensaTeg2(fechaInicio,fechaFin);
		return defensas;
	}
		
}
