package servicio;

import java.util.List;

import modelo.Lapso;
import modelo.Programa;
import modelo.Requisito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import interfazdao.IRequisitoDAO;;

@Service
public class SRequisito {
@Autowired
private IRequisitoDAO interfaceRequisito;

public void guardar(Requisito requisito){
	interfaceRequisito.save(requisito);
}
public Requisito buscarRequisito(long id){
	return interfaceRequisito.findOne(id);
}
public List<Requisito> buscarRequisito(){
	return interfaceRequisito.findAll();
}

public List<Requisito> buscarActivos(){
	List<Requisito> requisitos;
	requisitos = interfaceRequisito.findByEstatusTrueOrderByNombreAsc();
	return requisitos;
}

public List<Requisito> buscarRequisitosDisponibles(Programa programa, Lapso lapso){
	List<Requisito> requisitos;
	requisitos = interfaceRequisito.buscarDisponibles(programa, lapso);
	return requisitos;
}

}

