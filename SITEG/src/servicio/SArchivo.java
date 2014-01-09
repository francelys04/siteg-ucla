package servicio;

import java.util.List;

import interfazdao.IArchivoDAO;
import interfazdao.IAreaInvestigacionDAO;
import modelo.Archivo;
import modelo.AreaInvestigacion;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SArchivo {
	
	@Autowired
	private IArchivoDAO interfaceArchivo;
	
	public void guardar(Archivo archivo){
		interfaceArchivo.save(archivo);
	}
	
	public Archivo buscarArchivo(long id){
		return interfaceArchivo.findOne(id);
	}
	// busca todos los archivos sin eliminar en la base de dato
		public List<Archivo> buscarActivos() {
			List<Archivo> archivo;
			archivo = interfaceArchivo.findByEstatusTrue();
			return archivo;
		}
	

}
