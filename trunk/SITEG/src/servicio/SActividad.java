package servicio;

import java.util.List;

import modelo.Actividad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import interfazdao.IActividadDAO;
@Service
public class SActividad {
@Autowired
private IActividadDAO interfaceActividad;

public void guardar(Actividad actividad){
	interfaceActividad.save(actividad);
}
public Actividad buscarActividad(long id){
	return interfaceActividad.findById(id);
}
public List<Actividad> buscarActividad(){
	return interfaceActividad.findAll();
}
public List<Actividad> buscarActivos(){
	List<Actividad> actividades;
	actividades= interfaceActividad.buscarActividadesActivos();
	return actividades;
}

}

