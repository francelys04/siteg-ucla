package servicio;

import java.util.List;

import modelo.TipoJurado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import interfazdao.ITipoJuradoDAO;
@Service
public class STipoJurado {
@Autowired
private ITipoJuradoDAO interfaceTipoJurado;

public void guardar(TipoJurado tipoJurado){
	interfaceTipoJurado.save(tipoJurado);
}
public TipoJurado buscarTipoJurado(long id){
	return interfaceTipoJurado.findById(id);
}
public List<TipoJurado> buscarTipoJurado(){
	return interfaceTipoJurado.findAll();
}
public List<TipoJurado> buscarActivos(){
	List<TipoJurado> tipoJurados;
	tipoJurados= interfaceTipoJurado.buscarTipoJuradoActivos();
	return tipoJurados;
}

}

