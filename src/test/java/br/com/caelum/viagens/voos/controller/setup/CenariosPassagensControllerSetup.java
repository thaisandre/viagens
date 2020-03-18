package br.com.caelum.viagens.voos.controller.setup;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashSet;
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
import br.com.caelum.viagens.aeronaves.model.Modelo;
import br.com.caelum.viagens.aeronaves.repository.AeronaveRepository;
import br.com.caelum.viagens.voos.controller.dto.input.UpdateInputPassagemDto;
import br.com.caelum.viagens.voos.model.Passagem;
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
	
	private Pais argentina;
	private Pais brasil;
	
	private Aeroporto aeroportoA;
	private Aeroporto aeroportoB;

	private Companhia companhiaA;
	private Aeronave aeronave;
	
	private Voo voo;
	
	public CenariosPassagensControllerSetup(PaisRepository paisRepository, CompanhiaRepository companhiaRepository,
			AeroportoRepository aeroportoRepository, RotaRepository rotaRepository, AeronaveRepository aeronaveRepository,
			VooRepository vooRepository) {
		
		this.paisRepository = paisRepository;
		this.companhiaRepository = companhiaRepository;
		this.aeroportoRepository = aeroportoRepository;
		this.rotaRepository = rotaRepository;
		this.aeronaveRepository = aeronaveRepository;
		this.vooRepository = vooRepository;
		
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
		
		LocalDateTime dataDePartida = LocalDateTime.now().plusDays(2);
		this.voo.getAeronave().getAssentos().forEach(assento -> {
			this.voo.getPassagens().add(new Passagem(voo, dataDePartida, new BigDecimal(500.0), assento));
		});
	}
	
	
	public UpdateInputPassagemDto passagemComValorValido() {
		UpdateInputPassagemDto updatePassagemDto = new UpdateInputPassagemDto();
		updatePassagemDto.setValor(BigDecimal.valueOf(500.0).setScale(1, RoundingMode.HALF_UP));
		
		return updatePassagemDto;
	}
	
	public UpdateInputPassagemDto passagemComValorNulo() {
		UpdateInputPassagemDto updatePassagemDto = new UpdateInputPassagemDto();
		return updatePassagemDto;
	}
	
	public UpdateInputPassagemDto passagemComValorIgualAZero() {
		UpdateInputPassagemDto updatePassagemDto = new UpdateInputPassagemDto();
		updatePassagemDto.setValor(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.HALF_UP));
		return updatePassagemDto;
	}
	
	public UpdateInputPassagemDto passagemComValorNegativo() {
		UpdateInputPassagemDto updatePassagemDto = new UpdateInputPassagemDto();
		updatePassagemDto.setValor(BigDecimal.valueOf(0.0).setScale(2, RoundingMode.HALF_UP));
		return updatePassagemDto;
	}
}
