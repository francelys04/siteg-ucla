package servicio;

import java.util.List;

import interfazdao.IArchivoDAO;
import interfazdao.IAreaInvestigacionDAO;
import interfazdao.IDescargaDAO;
import modelo.Archivo;
import modelo.AreaInvestigacion;
import modelo.Descarga;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SDescarga {
	
	@Autowired
	private IDescargaDAO interfaceDescarga;
	
	public void guardar(Descarga descarga){
		interfaceDescarga.save(descarga);
	}
	
	public Descarga buscarArchivo(long id){
		return interfaceDescarga.findOne(id);
	}
	// busca todos los archivos sin eliminar en la base de dato
		public List<Descarga> buscarActivos() {
			List<Descarga> descarga;
			descarga= interfaceDescarga.findByEstatusTrue();
			return descarga;
		}
	

}
