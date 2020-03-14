package br.com.caelum.viagens.voos.controller.setup;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
import br.com.caelum.viagens.voos.controller.dto.input.NewPassagemInputDto;
import br.com.caelum.viagens.voos.model.Passagem;
import br.com.caelum.viagens.voos.model.RotaSemVoo;
import br.com.caelum.viagens.voos.model.Voo;
import br.com.caelum.viagens.voos.repository.PassagemRepository;
import br.com.caelum.viagens.voos.repository.VooRepository;

public class CenariosPassagensControllerSetup {
	
	private PaisRepository paisRepository;
	private CompanhiaRepository companhiaRepository;
	private AeroportoRepository aeroportoRepository;
	private AeronaveRepository aeronaveRepository;
	private RotaRepository rotaRepository;
	private VooRepository vooRepository;
	private PassagemRepository passagemRepository;
	
	private Pais argentina;
	private Pais brasil;
	
	private Aeroporto aeroportoA;
	private Aeroporto aeroportoB;

	private Companhia companhiaA;
	
	private Aeronave aeronave1;
	private Aeronave aeronave2;
	
	private Voo voo;

	private Assento assento1;
	private Assento assento2;
	private Assento assento3;
	
	public CenariosPassagensControllerSetup(PaisRepository paisRepository, CompanhiaRepository companhiaRepository,
			AeroportoRepository aeroportoRepository, RotaRepository rotaRepository, AeronaveRepository aeronaveRepository,
			VooRepository vooRepository, PassagemRepository passagemRepository) {
		
		this.paisRepository = paisRepository;
		this.companhiaRepository = companhiaRepository;
		this.aeroportoRepository = aeroportoRepository;
		this.rotaRepository = rotaRepository;
		this.aeronaveRepository = aeronaveRepository;
		this.vooRepository = vooRepository;
		this.passagemRepository = passagemRepository;
		
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
		
		this.aeronave1 =  this.aeronaveRepository.save(new Aeronave(Modelo.ATR40));
		this.aeronave2 =  this.aeronaveRepository.save(new Aeronave(Modelo.ATR72));
		
		this.voo = this.vooRepository.save(new Voo(rotas , companhiaA, aeronave1));
		
		this.assento1 =  voo.getAeronave().getAssentos().stream().findFirst().get();
		this.assento2 = voo.getAeronave().getAssentos().stream().filter(a -> !a.equals(assento1))
				.collect(Collectors.toList()).get(0);
		
		this.assento3 =  this.aeronave2.getAssentos().stream().findFirst().get();
		
		this.passagemRepository.save(new Passagem(voo, LocalDateTime.now().plusDays(2), new BigDecimal(500.0), assento2));
	}
	
	public NewPassagemInputDto passagemValida() {
		NewPassagemInputDto passagemInputDto = new NewPassagemInputDto();
		passagemInputDto.setVooId(this.voo.getId());
		
		LocalDateTime dataEHora = LocalDateTime.now().plusDays(2);
		passagemInputDto.setDataEHoraDePartida(dataEHora);
		passagemInputDto.setValor(BigDecimal.valueOf(900.0).setScale(2, RoundingMode.HALF_UP));
		
		passagemInputDto.setAssentoId(assento1.getId());
		
		return passagemInputDto;
	}
	
	public NewPassagemInputDto passagemComVooNulo() {
		NewPassagemInputDto passagemInputDto = new NewPassagemInputDto();
		
		LocalDateTime dataEHoraNoPassado = LocalDateTime.now().plusDays(2);
		passagemInputDto.setDataEHoraDePartida(dataEHoraNoPassado);
		
		passagemInputDto.setValor(BigDecimal.valueOf(900.0).setScale(2, RoundingMode.HALF_UP));
		
		passagemInputDto.setAssentoId(this.assento1.getId());
	    
		return passagemInputDto;
	}
	
	public NewPassagemInputDto passagemComVooNaoExistente() {
		NewPassagemInputDto passagemInputDto = new NewPassagemInputDto();
		passagemInputDto.setVooId(1L);
		
		LocalDateTime dataEHoraNoPassado = LocalDateTime.now().plusDays(2);
		passagemInputDto.setDataEHoraDePartida(dataEHoraNoPassado);
		
		passagemInputDto.setValor(BigDecimal.valueOf(900.0).setScale(2, RoundingMode.HALF_UP));
		
		passagemInputDto.setAssentoId(this.assento1.getId());
	    
		return passagemInputDto;
	}
	
	public NewPassagemInputDto passagemComDataEHoraNula() {
		NewPassagemInputDto passagemInputDto = new NewPassagemInputDto();
		passagemInputDto.setVooId(this.voo.getId());
		passagemInputDto.setValor(BigDecimal.valueOf(900.0).setScale(2, RoundingMode.HALF_UP));
		passagemInputDto.setAssentoId(this.assento1.getId());
	    
		return passagemInputDto;
	}
	
	public NewPassagemInputDto passagemComDataEHoraNoPassado() {
		NewPassagemInputDto passagemInputDto = new NewPassagemInputDto();
		passagemInputDto.setVooId(this.voo.getId());
		
		LocalDateTime dataEHoraNoPassado = LocalDateTime.now().minusDays(2);
		passagemInputDto.setDataEHoraDePartida(dataEHoraNoPassado);
		
		passagemInputDto.setValor(BigDecimal.valueOf(900.0).setScale(2, RoundingMode.HALF_UP));
		
		passagemInputDto.setAssentoId(this.assento1.getId());
	    
		return passagemInputDto;
	}
	
	public NewPassagemInputDto passagemComValorNulo() {
		NewPassagemInputDto passagemInputDto = new NewPassagemInputDto();
		passagemInputDto.setVooId(this.voo.getId());
		
		LocalDateTime dataEHoraNoPassado = LocalDateTime.now().plusDays(2);
		passagemInputDto.setDataEHoraDePartida(dataEHoraNoPassado);
		
		passagemInputDto.setAssentoId(this.assento1.getId());
	    
		return passagemInputDto;
	}
	
	public NewPassagemInputDto passagemComValorNegativo() {
		NewPassagemInputDto passagemInputDto = new NewPassagemInputDto();
		passagemInputDto.setVooId(this.voo.getId());
		
		LocalDateTime dataEHoraNoPassado = LocalDateTime.now().plusDays(2);
		passagemInputDto.setDataEHoraDePartida(dataEHoraNoPassado);
		
		passagemInputDto.setValor(BigDecimal.valueOf(-900.0).setScale(2, RoundingMode.HALF_UP));
		
		passagemInputDto.setAssentoId(this.assento1.getId());
	    
		return passagemInputDto;
	}

	public NewPassagemInputDto passagemComValorIgualAZero() {
		NewPassagemInputDto passagemInputDto = new NewPassagemInputDto();
		passagemInputDto.setVooId(this.voo.getId());
		
		LocalDateTime dataEHoraNoPassado = LocalDateTime.now().plusDays(2);
		passagemInputDto.setDataEHoraDePartida(dataEHoraNoPassado);
		
		passagemInputDto.setValor(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.HALF_UP));
		
		passagemInputDto.setAssentoId(this.assento1.getId());
	    
		return passagemInputDto;
	}
	
	public NewPassagemInputDto passagemComAssentoNulo() {
		NewPassagemInputDto passagemInputDto = new NewPassagemInputDto();
		passagemInputDto.setVooId(this.voo.getId());
		
		LocalDateTime dataEHoraNoPassado = LocalDateTime.now().plusDays(2);
		passagemInputDto.setDataEHoraDePartida(dataEHoraNoPassado);
		
		passagemInputDto.setValor(BigDecimal.valueOf(900.0).setScale(2, RoundingMode.HALF_UP));
		
		return passagemInputDto;
	}
	
	public NewPassagemInputDto passagemComAssentoNaoExistente() {
		NewPassagemInputDto passagemInputDto = new NewPassagemInputDto();
		passagemInputDto.setVooId(this.voo.getId());
		
		LocalDateTime dataEHoraNoPassado = LocalDateTime.now().plusDays(2);
		passagemInputDto.setDataEHoraDePartida(dataEHoraNoPassado);
		
		passagemInputDto.setValor(BigDecimal.valueOf(900.0).setScale(2, RoundingMode.HALF_UP));
		
		passagemInputDto.setAssentoId(1L);
	    
		return passagemInputDto;
	}
	
	public NewPassagemInputDto passagemComAssentoNaoExistenteNoVoo() {
		NewPassagemInputDto passagemInputDto = new NewPassagemInputDto();
		passagemInputDto.setVooId(this.voo.getId());
		
		LocalDateTime dataEHoraNoPassado = LocalDateTime.now().plusDays(2);
		passagemInputDto.setDataEHoraDePartida(dataEHoraNoPassado);
		
		passagemInputDto.setValor(BigDecimal.valueOf(900.0).setScale(2, RoundingMode.HALF_UP));
		
		passagemInputDto.setAssentoId(this.assento3.getId());
	    
		return passagemInputDto;
	}
	
	public NewPassagemInputDto passagemComAssentoNaoDisponivel() {
		NewPassagemInputDto passagemInputDto = new NewPassagemInputDto();
		passagemInputDto.setVooId(this.voo.getId());
		
		LocalDateTime dataEHoraNoPassado = LocalDateTime.now().plusDays(2);
		passagemInputDto.setDataEHoraDePartida(dataEHoraNoPassado);
		
		passagemInputDto.setValor(BigDecimal.valueOf(900.0).setScale(2, RoundingMode.HALF_UP));
		
		passagemInputDto.setAssentoId(this.assento2.getId());
	    
		return passagemInputDto;
	}


}
