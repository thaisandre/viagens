package br.com.caelum.viagens.voos.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.caelum.viagens.administrativo.repository.AeroportoRepository;
import br.com.caelum.viagens.administrativo.repository.CompanhiaRepository;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.aeronaves.repository.AeronaveRepository;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;
import br.com.caelum.viagens.voos.controller.setup.CenariosVoosControllerSetUp;
import br.com.caelum.viagens.voos.model.Rota;
import br.com.caelum.viagens.voos.model.Voo;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class VoosControllerTests {

	private static final String ENDPOINT = "/voos";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PaisRepository paisRepository;

	@Autowired
	private CompanhiaRepository companhiaRepository;

	@Autowired
	private AeroportoRepository aeroportoRepository;
	
	@Autowired
	private AeronaveRepository aeronaveRepository;
	
	@Autowired
	private RotaRepository rotaRepository;

	private CenariosVoosControllerSetUp cenarios;
	
	@BeforeEach
	public void setUp() {
		
		this.cenarios = 
				new CenariosVoosControllerSetUp(paisRepository, companhiaRepository, 
						aeroportoRepository, rotaRepository, aeronaveRepository);
		
	}

	@Test
	public void deveSalvarNovoVooComUmaUnicaRotaValida() throws Exception {
		NewVooInputDto newVooDto = cenarios.vooComRotaArgentinaParaBrasilSemParada();
		
		RequestBuilder request = processaRequest(newVooDto);
		
		Voo voo  = newVooDto.toModel(companhiaRepository, rotaRepository, aeronaveRepository);
		br.com.caelum.viagens.voos.model.Rota rota = voo.getRotasEmSequenciaLogica().iterator().next();
		
		mockMvc.perform(request).andExpect(status().isCreated())
				.andExpect(jsonPath("$.nomeCompanhia").value(voo.getNomeCompanhia()))
				.andExpect(jsonPath("$.lugaresDisponiveis").value(voo.getLugaresDisponiveis()))
				.andExpect(jsonPath("$.rotas[0].pernaFinal").value(rota.isPernaFinal()))
				.andExpect(jsonPath("$.rotas[0].origem.id").value(rota.getOrigem().getId()))
				.andExpect(jsonPath("$.rotas[0].origem.nome").value(rota.getOrigem().getNome()))
				.andExpect(jsonPath("$.rotas[0].origem.pais.id").value(rota.getOrigem().getPais().getId()))
				.andExpect(jsonPath("$.rotas[0].origem.pais.nome").value(rota.getOrigem().getPais().getNome()))
				.andExpect(jsonPath("$.rotas[0].destino.id").value(rota.getDestino().getId()))
				.andExpect(jsonPath("$.rotas[0].destino.nome").value(rota.getDestino().getNome()))
				.andExpect(jsonPath("$.rotas[0].destino.pais.id").value(rota.getDestino().getPais().getId()))
				.andExpect(jsonPath("$.rotas[0].destino.pais.nome").value(rota.getDestino().getPais().getNome()))
				.andExpect(jsonPath("$.origemInicial").value(rota.getOrigem().getNome()))
				.andExpect(jsonPath("$.destinoFinal").value(rota.getDestino().getNome()));
	}

	@Test
	public void deveSalvarNovoVooComMaisDeUmaRotaValida() throws Exception {
		NewVooInputDto newVooDto = 
				cenarios.vooComRotasArgentinaParaBrasilComParadaEBrasilParaChileSemParada();

		RequestBuilder request = processaRequest(newVooDto);
		
		Voo voo = newVooDto.toModel(companhiaRepository, rotaRepository, aeronaveRepository);
		List<Rota> rotas = voo.getRotasEmSequenciaLogica();
			
		br.com.caelum.viagens.voos.model.Rota rota1 = rotas.get(0);
		br.com.caelum.viagens.voos.model.Rota rota2 = rotas.get(1);
		
		mockMvc.perform(request).andExpect(status().isCreated())
				.andExpect(jsonPath("$.nomeCompanhia").value(voo.getNomeCompanhia()))
				.andExpect(jsonPath("$.lugaresDisponiveis").value(voo.getLugaresDisponiveis()))
				.andExpect(jsonPath("$.rotas[0].pernaFinal").value(rota1.isPernaFinal()))
				.andExpect(jsonPath("$.rotas[0].parada.tempo").value(rota1.getParada().getTempo()))
				.andExpect(jsonPath("$.rotas[0].parada.tipo").value(rota1.getParada().getTipo().toString()))
				.andExpect(jsonPath("$.rotas[0].origem.id").value(rota1.getOrigem().getId()))
				.andExpect(jsonPath("$.rotas[0].origem.nome").value(rota1.getOrigem().getNome()))
				.andExpect(jsonPath("$.rotas[0].origem.pais.id").value(rota1.getOrigem().getPais().getId()))
				.andExpect(jsonPath("$.rotas[0].origem.pais.nome").value(rota1.getOrigem().getPais().getNome()))
				.andExpect(jsonPath("$.rotas[0].destino.id").value(rota1.getDestino().getId()))
				.andExpect(jsonPath("$.rotas[0].destino.nome").value(rota1.getDestino().getNome()))
				.andExpect(jsonPath("$.rotas[0].destino.pais.id").value(rota1.getDestino().getPais().getId()))
				.andExpect(jsonPath("$.rotas[0].destino.pais.nome").value(rota1.getDestino().getPais().getNome()))
				.andExpect(jsonPath("$.rotas[1].pernaFinal").value(rota2.isPernaFinal()))
				.andExpect(jsonPath("$.rotas[1].origem.id").value(rota2.getOrigem().getId()))
				.andExpect(jsonPath("$.rotas[1].origem.nome").value(rota2.getOrigem().getNome()))
				.andExpect(jsonPath("$.rotas[1].origem.pais.id").value(rota2.getOrigem().getPais().getId()))
				.andExpect(jsonPath("$.rotas[1].origem.pais.nome").value(rota2.getOrigem().getPais().getNome()))
				.andExpect(jsonPath("$.rotas[1].destino.id").value(rota2.getDestino().getId()))
				.andExpect(jsonPath("$.rotas[1].destino.nome").value(rota2.getDestino().getNome()))
				.andExpect(jsonPath("$.rotas[1].destino.pais.id").value(rota2.getDestino().getPais().getId()))
				.andExpect(jsonPath("$.rotas[1].destino.pais.nome").value(rota2.getDestino().getPais().getNome()))
				.andExpect(jsonPath("$.origemInicial").value(rota1.getOrigem().getNome()))
				.andExpect(jsonPath("$.destinoFinal").value(rota2.getDestino().getNome()));
	}
	
	@Test
	public void naoDeveSalvarNovoVooComCompanhiaQueNaoExiste() throws Exception {
		NewVooInputDto newVooDto = 
				cenarios.vooComRotaArgentinaParaBrasilComCompanhiaInexistente();

		RequestBuilder request = processaRequest(newVooDto);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())		
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("companhiaId"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("companhia não existe no sistema."));		
	}
	
	@Test
	public void naoDeveSalvarNovoVooComAeronaveQueNaoExiste() throws Exception {
		NewVooInputDto newVooDto = 
				cenarios.vooComRotaArgentinaParaBrasilComAeronveInexistente();

		RequestBuilder request = processaRequest(newVooDto);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())		
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("aeronaveId"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("aeronave não existe no sistema."));		
	}
	
	@Test
	public void naoDeveSalvarNovoVooSemListaDeRotas() throws Exception {	
		NewVooInputDto newVooDto = cenarios.vooSemRotas();

		RequestBuilder request = processaRequest(newVooDto);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())		
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("não pode estar vazio"));		
	}
	
	@Test
	public void naoDeveSalvarNovoVooComRotaComParadaSemTipo() throws Exception {	
		
		NewVooInputDto newVooDto = 
				cenarios.vooComRotaArgentinaParaBrasilComParadaSemTipoEBrasilParaChile();
		
		RequestBuilder request = processaRequest(newVooDto);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())	
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas[].parada.tipo"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("não pode ser nulo"));		
	}
	
	@Test
	public void naoDeveSalvarNovoVooComRotaComParadaSemTempo() throws Exception {	
		NewVooInputDto newVooDto = 
				cenarios.vooComRotaArgentinaParaBrasilComParadaSemTempoERotaBrasilParaChile();

		RequestBuilder request = processaRequest(newVooDto);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())		
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas[].parada.tempo"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("não pode ser nulo"));		
	}
	
	@Test
	public void naoDeveSalvarNovoVooComRotaComParadaComTempoIgualAZero() throws Exception {	
		NewVooInputDto newVooDto = 
				cenarios.vooComRotaArgentinaParaBrasilComParadaComTempoIgualAZeroERotaBrasilParaChile();

		RequestBuilder request = processaRequest(newVooDto);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())		
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas[].parada.tempo"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("deve ser maior que 0"));		
	}
	
	@Test
	public void naoDeveSalvarNovoVooComRotaComParadaComTempoNegativo() throws Exception {	
		NewVooInputDto newVooDto = 
				cenarios.vooComRotaArgentinaParaBrasilComParadaComTempoNegativoERotaBrasilParaChile();

		RequestBuilder request = processaRequest(newVooDto);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())		
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas[].parada.tempo"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("deve ser maior que 0"));		
	}
	
	@Test
	public void naoDeveSalvarNovoVooComRotaQueNaoExiste() throws Exception {	
		NewVooInputDto newVooDto = 
				cenarios.vooComRotaInexistente();
		
		RequestBuilder request = processaRequest(newVooDto);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())		
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("rotaId 1 não existe no sistema."));		
	}
	
	@Test
	public void naoDeveSalvarNovoVooComRotasRepetidas() throws Exception {			
		NewVooInputDto newVooDto = 
				cenarios.vooComRotasDeArgentinaParaBrasilRepetidas();

		RequestBuilder request = processaRequest(newVooDto);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())		
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("não é permitido repetir rotas em um voo."));
	}
	
	@Test
	public void naoDeveSalvarNovoVooComUmaUnicaRotaQueNaoEhPernalFinal() throws Exception {
		NewVooInputDto newVooDto = 
				cenarios.vooComRotaArgentinaParaBrasilComParada();

		RequestBuilder request = processaRequest(newVooDto);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("a lista de rotas precisa conter pelo menos uma rota final (sem parada)."));
	}
	
	@Test
	public void naoDeveSalvarNovoVooComRotasQueNaoSaoPernalFinal() throws Exception {
		NewVooInputDto newVooDto = 
				cenarios.vooComRotaArgentinaParaBrasilComParadaERotaBrasilParaChileComParada();

		RequestBuilder request = processaRequest(newVooDto);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("a lista de rotas precisa conter pelo menos uma rota final (sem parada)."));
	}
	
	@Test
	public void naoDeveSalvarNovoVooComMaisDeUmaRotaQueEhPernalFinal() throws Exception {
		NewVooInputDto newVooDto = 
				cenarios.vooComRotaArgentinaParaBrasilSemParadaERotaBrasilParaChileSemParada();

		RequestBuilder request = processaRequest(newVooDto);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("a lista de rotas precisa conter apenas uma única rota final (sem parada)."));
	}
	
	@Test
	public void naoDeveCadastrarVooComRotasComOrigemDeRotaAnteriorIgualAoDestinoDeRotaPosteriorConsecutiva() throws Exception {
		NewVooInputDto newVooDto = 
				cenarios.vooComRotaArgentinaParaBrasilComParadaERotaBrasilParaArgentinaSemParada();

		RequestBuilder request = processaRequest(newVooDto);
		
		mockMvc.perform(request)
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.fieldErrors").isArray())
		.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas"))
		.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("as rotas não possuem uma sequência lógica."));
		
	}
	
	
	@Test
	public void naoDeveSalvarNovoVooEmQueAOrigemDaRotaAnteriorSejaIgualAoDestinoDeUmaRotaPosteriorNaoConsecutiva() throws Exception {
		
		NewVooInputDto newVooDto = 
				cenarios.vooComRotaBrasilParaArgentinaComParadaERotaBrasilParaChileComParadaERotaChileParaArgentinaSemParada();

		RequestBuilder request = processaRequest(newVooDto);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("as rotas não possuem uma sequência lógica."));
	}
	
	@Test
	public void naoDeveSalvarNovoVooEmQueUmRotaNaoPossuiConexaoComAsOutrasDuas() throws Exception {
		NewVooInputDto newVooDto = 
				cenarios.vooComRotaArgentinaParaBrasilComParadaERotaBrasilParaChileComParadaERotaUruguaiParaParaguaiSemParada();

		RequestBuilder request = processaRequest(newVooDto);
		
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("as rotas não possuem uma sequência lógica."));
	}
	
	@Test
	public void naoDeveSalvarNovoVooEmQueAsRotaPossuemRelacaoMasNaoUmaSequenciaLogica() throws Exception {
		NewVooInputDto newVooDto = 
				cenarios.vooComRotaBrasilParaArgentinaComParadaERotaBrasilParaChileComParadaERotaBrasilParaArgentinaSemParada();

		RequestBuilder request = processaRequest(newVooDto);

		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.fieldErrors").isArray())
			.andExpect(jsonPath("$.fieldErrors[*].campo").value("rotas"))
			.andExpect(jsonPath("$.fieldErrors[*].mensagem").value("as rotas não possuem uma sequência lógica."));
	}
	
	private MockHttpServletRequestBuilder processaRequest(NewVooInputDto newVooDto) throws JsonProcessingException {
		return post(ENDPOINT).contentType(MediaType.APPLICATION_JSON_VALUE)
				.header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
				.content(new ObjectMapper().writeValueAsString(newVooDto));
	}
	
	
}
