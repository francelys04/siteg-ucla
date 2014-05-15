package servicio;

import interfazdao.IDefensaDAO;

import java.util.Date;
import java.util.List;

import modelo.AreaInvestigacion;
import modelo.Defensa;
import modelo.Programa;
import modelo.Teg;
import modelo.Tematica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SDefensa")
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
	
	public List<Defensa> buscardefensasegunareaprogramaareayestatus(String estatus,Programa programa,AreaInvestigacion area,Date fechaInicio,Date fechaFin){
		List<Defensa> defensas;
		defensas=interfaceDefensa.buscarDefensaTegSegunEstatusProgramayarea(estatus, programa, area, fechaInicio, fechaFin);
		return defensas;
	}
	
	public List<Defensa> buscardefensasegunareaprogramaareay(Programa programa,AreaInvestigacion area,Date fechaInicio,Date fechaFin){
		List<Defensa> defensas;
		defensas=interfaceDefensa.buscarDefensaTegSegunProgramayarea( programa, area, fechaInicio, fechaFin);
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

	public List<Defensa> buscarDefensaTeg(Date fechaInicio,Date fechaFin){
		List<Defensa> defensas;
		defensas=interfaceDefensa.buscarDefensaTeg(fechaInicio,fechaFin);
		return defensas;
	}
	
	public List<Defensa> buscarDefensaTegSegunTematica(Programa programa,Tematica tematica,Date fechaInicio,Date fechaFin){
		List<Defensa> defensas;
		defensas=interfaceDefensa.buscarDefensaTegSegunTematica(programa,tematica,fechaInicio,fechaFin);
		return defensas;
	}
	public List<Defensa> buscarDefensaTegSegunPrograma(Programa programa,Date fechaInicio,Date fechaFin){
		List<Defensa> defensas;
		defensas=interfaceDefensa.buscarDefensaTegSegunPrograma(programa,fechaInicio,fechaFin);
		return defensas;
	}
	public List<Defensa> buscarDefensaTegSegunArea(AreaInvestigacion area,Date fechaInicio,Date fechaFin, Programa programa){
		List<Defensa> defensas;
		defensas=interfaceDefensa.buscarDefensaTegSegunArea(area,fechaInicio,fechaFin, programa);
		return defensas;
	}
	
	public List<Defensa> buscarDefensaTegSegunEstatus2(String estatus,Date fechaInicio,Date fechaFin){
		List<Defensa> defensas;
		defensas=interfaceDefensa.buscarDefensaTegSegunEstatus2(estatus,fechaInicio,fechaFin);
		return defensas;
	}

	public List<Defensa> buscarDefensaTegSegunEstatusTematica2(Programa programa,Tematica tematica,Date fechaInicio, Date fechaFin, String estatus){
		List<Defensa> defensas;
		defensas=interfaceDefensa.buscarDefensaTegSegunEstatusTematica2(programa,tematica,fechaInicio,fechaFin,estatus);
		return defensas;
	}
	public List<Defensa> buscarDefensaTegSegunEstatusArea2(AreaInvestigacion area,Date fechaInicio,Date fechaFin, Programa programa, String estatus){
		List<Defensa> defensas;
		defensas=interfaceDefensa.buscarDefensaTegSegunEstatusArea2(area,fechaInicio,fechaFin,programa,estatus);
		return defensas;
	}
	public List<Defensa> buscarDefensaTegSegunEstatusPrograma2(Programa programa,Date fechaInicio,Date fechaFin, String estatus){
		List<Defensa> defensas;
		defensas=interfaceDefensa.buscarDefensaTegSegunEstatusPrograma2(programa,fechaInicio,fechaFin,estatus);
		return defensas;
	}

	
}
