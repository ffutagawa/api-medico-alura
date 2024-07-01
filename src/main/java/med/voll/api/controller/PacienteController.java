package med.voll.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.DadosAtualizacaoMedico;
import med.voll.api.domain.medico.DadosDetalhamentoMedico;
import med.voll.api.domain.paciente.DadosAtualizacaoPaciente;
import med.voll.api.domain.paciente.DadosCadastroPaciente;
import med.voll.api.domain.paciente.DadosDetalhamentoPaciente;
import med.voll.api.domain.paciente.DadosListagemPaciente;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;

@RestController
@RequestMapping("pacientes")
public class PacienteController {
	
	@Autowired
	private PacienteRepository pacienteRepository;
	
	@PostMapping
	@Transactional
	public void cadastrarPaciente(@RequestBody @Valid DadosCadastroPaciente dados) {
		pacienteRepository.save(new Paciente(dados));
	}
	
//	@GetMapping
//	public List<Paciente> findAll(){
//		return pacienteRepository.findAll();
//	}
	
    @GetMapping("/resumida")
    public Page<DadosListagemPaciente> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        return pacienteRepository.findAllByAtivoTrue(paginacao).map(DadosListagemPaciente::new);
    }

	@GetMapping
	public ResponseEntity<Page<DadosDetalhamentoPaciente>>BuscarPorGeralPorDetalhePaciente(@PageableDefault(size = 10, page = 0, sort = { "id" }) Pageable paginacao) {
		var page = pacienteRepository.findAllByAtivoTrue(paginacao).map(DadosDetalhamentoPaciente::new);
		return ResponseEntity.ok(page);
	}
    
	@GetMapping("/{id}")
	public ResponseEntity BuscarPorId(@PathVariable Long id) {
		var paciente = pacienteRepository.getReferenceById(id);
		return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
	}	
	
	@PutMapping
	@Transactional
	public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoPaciente dados) {
	        var paciente = pacienteRepository.getReferenceById(dados.id());
	        paciente.atualizarInformacoes(dados);

	        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
	    }

    
	@DeleteMapping
	@Transactional
	public void excluirPaciente (@PathVariable Long id) {
		var paciente = pacienteRepository.getReferenceById(id);
		paciente.excluir();
	}
	
}
