package interfazdao;

import java.util.List;

import modelo.Archivo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IArchivoDAO extends JpaRepository<Archivo, Long> {
	public List<Archivo> findByEstatusTrue();

	public List<Archivo> findByEstatusAndTipoArchivo(boolean b, String h);

//	public Archivo findOne(Archivo archivo);

}
