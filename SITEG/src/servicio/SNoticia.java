package servicio;

import java.util.List;

import modelo.Noticia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import interfazdao.INoticiaDAO;
@Service
public class SNoticia {
@Autowired
private INoticiaDAO interfaceNoticia;

public void guardar(Noticia noticia){
	interfaceNoticia.save(noticia);
}
public Noticia buscarNoticia(long id){
	return interfaceNoticia.findById(id);
}

public List<Noticia> buscarActivos(){
	List<Noticia> descargas;
	descargas= interfaceNoticia.buscarNoticiasActivas();
	return descargas;
}

}
