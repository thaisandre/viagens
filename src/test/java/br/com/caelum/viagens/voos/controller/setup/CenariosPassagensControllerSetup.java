package br.com.caelum.viagens.voos.controller.setup;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.model.Companhia;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.model.Rota;
import br.com.caelum.viagens.administrativo.repository.AeroportoRepository;
import br.com.caelum.viagens.administrativo.repository.CompanhiaRepository;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.aeronaves.model.Aeronave;
import br.com.caelum.viagens.aeronaves.model.Assento;
import br.com.caelum.viagens.aeronaves.model.Modelo;
import br.com.caelum.viagens.aeronaves.repository.AeronaveRepository;
import br.com.caelum.viagens.aeronaves.repository.AssentoRepository;
import br.com.caelum.viagens.voos.controller.dto.input.NewPassagemInputDto;
import br.com.caelum.viagens.voos.model.RotaSemVoo;
import br.com.caelum.viagens.voos.model.Voo;
import br.com.caelum.viagens.voos.repository.VooRepository;

public class CenariosPassagensControllerSetup {
	
	private PaisRepository paisRepository;
	private CompanhiaRepository companhiaRepository;
	private AeroportoRepository aeroportoRepository;
	private AeronaveRepository aeronaveRepository;
	private RotaRepository rotaRepository;
	private VooRepository vooRepository;
	private AssentoRepository assentoRepository;
	
	private Pais argentina;
	private Pais brasil;
	
	private Aeroporto aeroportoA;
	private Aeroporto aeroportoB;

	private Companhia companhiaA;
	private Aeronave aeronave;
	
	private Voo voo;

	private Assento assento;
	private Assento assentoDisponivel;
	
	public CenariosPassagensControllerSetup(PaisRepository paisRepository, CompanhiaRepository companhiaRepository,
			AeroportoRepository aeroportoRepository, RotaRepository rotaRepository, AeronaveRepository aeronaveRepository,
			VooRepository vooRepository, AssentoRepository assentoRepository) {
		
		this.paisRepository = paisRepository;
		this.companhiaRepository = companhiaRepository;
		this.aeroportoRepository = aeroportoRepository;
		this.rotaRepository = rotaRepository;
		this.aeronaveRepository = aeronaveRepository;
		this.vooRepository = vooRepository;
		this.assentoRepository = assentoRepository;
		
		populaBanco();
		
	}
	
	private void populaBanco() {
		this.argentina = this.paisRepository.save(new Pais("Argentina"));
		this.brasil = this.paisRepository.save(new Pais("Brasil"));
		
		this.aeroportoA = this.aeroportoRepository.save(new Aeroporto("AeroportoA", argentina));
		this.aeroportoB = this.aeroportoRepository.save(new Aeroporto("AeroportoB", brasil));
		
		Rota rota = this.rotaRepository.save(new Rota(this.aeroportoA, this.aeroportoB, 90));
		Set<RotaSemVoo> rotas = new HashSet<>();
		rotas.add(new RotaSemVoo(rota));
		
		this.companhiaA = this.companhiaRepository.save( new Companhia("CompanhiaA", argentina));
		this.aeronave =  this.aeronaveRepository.save(new Aeronave(Modelo.ATR40));
		this.voo = this.vooRepository.save(new Voo(rotas , companhiaA, aeronave));
		
		this.assento =  voo.getAeronave().getAssentos().stream().findFirst().get();
	}
	
	public NewPassagemInputDto passagemValida() {
		NewPassagemInputDto passagemInputDto = new NewPassagemInputDto();
		passagemInputDto.setVooId(this.voo.getId());
		
		LocalDateTime dataEHora = LocalDateTime.now().plusDays(2);
		passagemInputDto.setDataEHoraDePartida(dataEHora);
		passagemInputDto.setValor(BigDecimal.valueOf(900.0).setScale(2, RoundingMode.HALF_UP));
		
		Optional<Assento> assento = voo.getAeronave().getAssentos().stream().findFirst();
		passagemInputDto.setAssentoId(assento.get().getId());
		
		return passagemInputDto;
	}
	

	public NewPassagemInputDto passagemComDataNoPassado() {
		NewPassagemInputDto passagemInputDto = new NewPassagemInputDto();
		passagemInputDto.setVooId(this.voo.getId());
		
		LocalDateTime dataEHoraNoPassado = LocalDateTime.now().minusDays(2);
		passagemInputDto.setDataEHoraDePartida(dataEHoraNoPassado);
		passagemInputDto.setValor(BigDecimal.valueOf(900.0).setScale(2, RoundingMode.HALF_UP));
		
		passagemInputDto.setAssentoId(this.assento.getId());
	    
		return passagemInputDto;
	}
	
	public NewPassagemInputDto passagemComVooNaoExistente() {
		NewPassagemInputDto passagemInputDto = new NewPassagemInputDto();
		passagemInputDto.setVooId(1L);
		
		LocalDateTime dataEHoraNoPassado = LocalDateTime.now().plusDays(2);
		passagemInputDto.setDataEHoraDePartida(dataEHoraNoPassado);
		passagemInputDto.setValor(BigDecimal.valueOf(900.0).setScale(2, RoundingMode.HALF_UP));
		
		passagemInputDto.setAssentoId(this.assento.getId());
	    
		return passagemInputDto;
	}
	
	public NewPassagemInputDto passagemComValorNegativo() {
		NewPassagemInputDto passagemInputDto = new NewPassagemInputDto();
		passagemInputDto.setVooId(this.voo.getId());
		
		LocalDateTime dataEHoraNoPassado = LocalDateTime.now().plusDays(2);
		passagemInputDto.setDataEHoraDePartida(dataEHoraNoPassado);
		passagemInputDto.setValor(BigDecimal.valueOf(-900.0).setScale(2, RoundingMode.HALF_UP));
		
		passagemInputDto.setAssentoId(this.assento.getId());
	    
		return passagemInputDto;
	}

	public NewPassagemInputDto passagemComValorIgualAZero() {
		NewPassagemInputDto passagemInputDto = new NewPassagemInputDto();
		passagemInputDto.setVooId(this.voo.getId());
		
		LocalDateTime dataEHoraNoPassado = LocalDateTime.now().plusDays(2);
		passagemInputDto.setDataEHoraDePartida(dataEHoraNoPassado);
		passagemInputDto.setValor(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.HALF_UP));
		
		passagemInputDto.setAssentoId(this.assento.getId());
	    
		return passagemInputDto;
	}


}
