package med.voll.api.domain.medico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
	
	Page<Medico> findAllByAtivoTrue(Pageable paginacao);
	
}
