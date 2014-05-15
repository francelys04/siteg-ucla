package servicio;

import interfazdao.ITipoJuradoDAO;

import java.util.List;

import modelo.TipoJurado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("STipoJurado")
public class STipoJurado {

	@Autowired
	private ITipoJuradoDAO interfaceTipoJurado;

	public void guardar(TipoJurado tipoJurado) {
		interfaceTipoJurado.save(tipoJurado);
	}

	public TipoJurado buscarTipoJurado(long id) {
		return interfaceTipoJurado.findOne(id);
	}

	public List<TipoJurado> buscarActivos() {
		List<TipoJurado> tipoJurados;
		tipoJurados = interfaceTipoJurado.findByEstatusTrueOrderByNombreAsc();
		return tipoJurados;
	}
	
	
	public List<TipoJurado> buscarInactivos() {
		List<TipoJurado> tipoJurados;
		tipoJurados = interfaceTipoJurado.findByEstatusFalseOrderByNombreAsc();
		return tipoJurados;
	}
	
	

	public TipoJurado buscarPorNombre(String tipojurado) {
		// TODO Auto-generated method stub
		TipoJurado tipo;
		tipo = interfaceTipoJurado.findByNombreAllIgnoreCase(tipojurado);
		return tipo;
	}

}
