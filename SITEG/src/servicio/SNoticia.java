package servicio;

import interfazdao.INoticiaDAO;

import java.util.List;

import modelo.Noticia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class SNoticia {
@Autowired
private INoticiaDAO interfaceNoticia;

public void guardar(Noticia noticia){
	interfaceNoticia.save(noticia);
}
public Noticia buscarNoticia(long id){
	return interfaceNoticia.findOne(id);
}

public List<Noticia> buscarActivos(){
	List<Noticia> descargas;
	descargas= interfaceNoticia.buscarNoticiasActivas();
	return descargas;
}

}
