package med.voll.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import med.voll.api.domain.medico.DadosAtualizacaoMedico;
import med.voll.api.domain.medico.DadosCadastroMedico;
import med.voll.api.domain.medico.DadosDetalhamentoMedico;
import med.voll.api.domain.medico.DadosListagemMedico;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

	@Autowired
	private MedicoRepository medicoRepository;

	@PostMapping
	@Transactional
	public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {
		var medico = new Medico(dados);
		medicoRepository.save(medico);

		var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

		return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));

	}
	
//	@GetMapping
//	public List<Medico> findAll(){
//		return medicoRepository.findAll();
//	}
	
//	@GetMapping
//	public List<Medico> BuscarPorGeralComDetalhe() {  // sem paginacao
//		return medicoRepository.findAll();
//	}
	
	@GetMapping
	public ResponseEntity<Page<DadosDetalhamentoMedico>>BuscarPorGeralPorDetalhe(@PageableDefault(size = 10, page = 0, sort = { "id" }) Pageable paginacao) {
		var page = medicoRepository.findAllByAtivoTrue(paginacao).map(DadosDetalhamentoMedico::new);
		return ResponseEntity.ok(page);
	}

	@GetMapping("/resumida")
	public ResponseEntity<Page<DadosListagemMedico>> listar(@PageableDefault(size = 10, page = 0, sort = { "nome" }) Pageable paginacao) {
		var page = medicoRepository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
		return ResponseEntity.ok(page);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity BuscarPorId(@PathVariable Long id) {
		var medico = medicoRepository.getReferenceById(id);
		return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
	}	

	@PutMapping
	@Transactional
	public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {
	        var medico = medicoRepository.getReferenceById(dados.id());
	        medico.atualizarInformacoes(dados);

	        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
	    }

//	@DeleteMapping("/{id}")
//	@Transactional
//	public void deletar (@PathVariable Long id) {
//		medicoRepository.deleteById(id);
//	}

//	@DeleteMapping("/{id}")
//	@Transactional
//	public void deletar (@PathVariable Long id) {
//		var medico = medicoRepository.getReferenceById(id);
//		medico.excluir();
//	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity deletar(@PathVariable Long id) {
		var medico = medicoRepository.getReferenceById(id);
		medico.excluir();

		return ResponseEntity.noContent().build();
	}

}
