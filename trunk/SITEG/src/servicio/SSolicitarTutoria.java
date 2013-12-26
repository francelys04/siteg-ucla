package servicio;


import java.util.List;
import modelo.SolicitudTutoria;
import modelo.Teg;
import interfazdao.ISolicitarTutoriaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SSolicitarTutoria {
@Autowired
private ISolicitarTutoriaDAO interfaceSolicitarTutoria;

//Metodo para guardar na solicitud de tutoria
	public void guardar(SolicitudTutoria SolicitudTutoria){
		interfaceSolicitarTutoria.save(SolicitudTutoria);
	}
}