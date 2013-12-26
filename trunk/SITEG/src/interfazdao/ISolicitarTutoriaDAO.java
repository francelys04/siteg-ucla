package interfazdao;
import modelo.SolicitudTutoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ISolicitarTutoriaDAO extends JpaRepository<SolicitudTutoria, String> {

}