package br.com.caelum.viagens.voos.controller.setup;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.model.Companhia;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.model.Rota;
import br.com.caelum.viagens.administrativo.repository.AeroportoRepository;
import br.com.caelum.viagens.administrativo.repository.CompanhiaRepository;
import br.com.caelum.viagens.administrativo.repository.PaisRepository;
import br.com.caelum.viagens.administrativo.repository.RotaRepository;
import br.com.caelum.viagens.voos.controller.dto.input.NewParadaDaRotaInputDto;
import br.com.caelum.viagens.voos.controller.dto.input.NewRotaDoVooInputDto;
import br.com.caelum.viagens.voos.controller.dto.input.NewVooInputDto;

public class CenariosVoosControllerSetUp {
	
	private PaisRepository paisRepository;
	private CompanhiaRepository companhiaRepository;
	private AeroportoRepository aeroportoRepository;
	private RotaRepository rotaRepository;
	
	private Pais argentina;
	private Pais brasil;
	private Pais chile;
	private Pais paraguai;
	private Pais uruguai;

	private Companhia companhiaA;

	private Aeroporto aeroportoA;
	private Aeroporto aeroportoB;
	private Aeroporto aeroportoC;
	private Aeroporto aeroportoP;
	private Aeroporto aeroportoU;

	private Rota rotaAtoB;
	private Rota rotaBtoC;
	private Rota rotaBtoA;
	private Rota rotaCtoA;
	private Rota rotaUtoP;
	
	private NewParadaDaRotaInputDto paradaValida;
	
	public CenariosVoosControllerSetUp(PaisRepository paisRepository, CompanhiaRepository companhiaRepository,
			AeroportoRepository aeroportoRepository, RotaRepository rotaRepository) {
		this.paisRepository = paisRepository;
		this.companhiaRepository = companhiaRepository;
		this.aeroportoRepository = aeroportoRepository;
		this.rotaRepository = rotaRepository;
		
		this.paradaValida = new NewParadaDaRotaInputDto();
		this.paradaValida.setTempo(40);
		this.paradaValida.setTipo("ESCALA");
		
		this.populaBanco();
		
	}
	
	private void populaBanco() {
		this.argentina = this.paisRepository.save(new Pais("Argentina"));
		this.brasil = this.paisRepository.save(new Pais("Brasil"));
		this.chile = this.paisRepository.save(new Pais("Chile"));
		this.paraguai = this.paisRepository.save(new Pais("Paraguai"));
		this.uruguai = this.paisRepository.save(new Pais("Uruguai"));
		
		this.companhiaA = this.companhiaRepository.save(new Companhia("CompanhiaA", this.argentina));
		
		this.aeroportoA = this.aeroportoRepository.save(new Aeroporto("AeroportoA", this.argentina));
		this.aeroportoB = this.aeroportoRepository.save(new Aeroporto("AeroportoB", this.brasil));
		this.aeroportoC = this.aeroportoRepository.save(new Aeroporto("AeroportoC", this.chile));
		this.aeroportoP = this.aeroportoRepository.save(new Aeroporto("AeroportoP", this.paraguai));
		this.aeroportoU = this.aeroportoRepository.save(new Aeroporto("AeroportoU", this.uruguai));
		
		
		this.rotaAtoB = this.rotaRepository.save(new Rota(this.aeroportoA, this.aeroportoB, 110));
		this.rotaBtoC = this.rotaRepository.save(new Rota(this.aeroportoB, this.aeroportoC, 110));
		this.rotaBtoA = this.rotaRepository.save(new Rota(this.aeroportoB, this.aeroportoA, 110));
		this.rotaCtoA = this.rotaRepository.save(new Rota(this.aeroportoC, this.aeroportoA, 110));
		this.rotaUtoP = this.rotaRepository.save(new Rota(this.aeroportoU, this.aeroportoP, 110));
	}
	
	public NewVooInputDto vooComRotaArgentinaParaBrasilSemParada() {
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(this.rotaAtoB.getId());

		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaAtoB);

		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(this.companhiaA.getId());
		newVooDto.setLugaresDisponiveis(100);
		newVooDto.setRotas(rotas);
		
		return newVooDto;
	}

	public NewVooInputDto vooComRotasArgentinaParaBrasilComParadaEBrasilParaChileSemParada() {
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(this.rotaAtoB.getId());
		rotaAtoB.setParada(this.paradaValida);

		NewRotaDoVooInputDto rotaBtoC = new NewRotaDoVooInputDto();
		rotaBtoC.setRotaId(this.rotaBtoC.getId());

		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaAtoB);
		rotas.add(rotaBtoC);

		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(this.companhiaA.getId());
		newVooDto.setLugaresDisponiveis(100);
		newVooDto.setRotas(rotas);
		
		return newVooDto;

	}
	
	public NewVooInputDto vooComRotaArgentinaParaBrasilComCompanhiaInexistente() {
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(this.rotaAtoB.getId());
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaAtoB);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(1L);
		newVooDto.setLugaresDisponiveis(100);
		newVooDto.setRotas(rotas);
		
		return newVooDto;
	}
	
	public NewVooInputDto vooComRotaArgentinaParaBrasilComLugaresDisponiveisIgualAZero() {
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(this.rotaAtoB.getId());
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaAtoB);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(this.companhiaA.getId());
		newVooDto.setLugaresDisponiveis(0);
		newVooDto.setRotas(rotas);
		
		return newVooDto;
	}
	
	public NewVooInputDto vooComRotaArgentinaParaBrasilComLugaresDisponiveisNegativo() {
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(this.rotaAtoB.getId());
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaAtoB);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(this.companhiaA.getId());
		newVooDto.setLugaresDisponiveis(-10);
		newVooDto.setRotas(rotas);
		
		return newVooDto;
	}
	
	public NewVooInputDto vooSemRotas() {
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(this.companhiaA.getId());
		newVooDto.setLugaresDisponiveis(100);
		
		return newVooDto;

	}
	
	public NewVooInputDto vooComRotaArgentinaParaBrasilComParadaSemTipoEBrasilParaChile() {
		NewParadaDaRotaInputDto parada = new NewParadaDaRotaInputDto();
		parada.setTempo(40);
		
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(this.rotaAtoB.getId());
		rotaAtoB.setParada(parada);
		
		NewRotaDoVooInputDto rotaBtoC = new NewRotaDoVooInputDto();
		rotaBtoC.setRotaId(this.rotaBtoC.getId());
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaAtoB);
		rotas.add(rotaBtoC);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(this.companhiaA.getId());
		newVooDto.setLugaresDisponiveis(100);
		newVooDto.setRotas(rotas);
		
		return newVooDto;
	}
	
	public NewVooInputDto vooComRotaArgentinaParaBrasilComTipoDeParadaInvalidoERotaBrasilParaChile() {
		NewParadaDaRotaInputDto parada = new NewParadaDaRotaInputDto();
		parada.setTempo(40);
		parada.setTipo("ESCALAA");
		
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(this.rotaAtoB.getId());
		rotaAtoB.setParada(parada);
		
		NewRotaDoVooInputDto rotaBtoC = new NewRotaDoVooInputDto();
		rotaBtoC.setRotaId(this.rotaBtoC.getId());
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaAtoB);
		rotas.add(rotaBtoC);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(this.companhiaA.getId());
		newVooDto.setLugaresDisponiveis(100);
		newVooDto.setRotas(rotas);
		
		return newVooDto;
	}
	
	public NewVooInputDto vooComRotaArgentinaParaBrasilComParadaSemTempoERotaBrasilParaChile() {
		NewParadaDaRotaInputDto parada = new NewParadaDaRotaInputDto();
		parada.setTipo("ESCALA");
		
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(this.rotaAtoB.getId());
		rotaAtoB.setParada(parada);
		
		NewRotaDoVooInputDto rotaBtoC = new NewRotaDoVooInputDto();
		rotaBtoC.setRotaId(this.rotaBtoC.getId());
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaAtoB);
		rotas.add(rotaBtoC);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(this.companhiaA.getId());
		newVooDto.setLugaresDisponiveis(100);
		newVooDto.setRotas(rotas);
		
		return newVooDto;
	}
	
	public NewVooInputDto vooComRotaArgentinaParaBrasilComParadaComTempoIgualAZeroERotaBrasilParaChile() {
		NewParadaDaRotaInputDto parada = new NewParadaDaRotaInputDto();
		parada.setTempo(0);
		parada.setTipo("ESCALA");
		
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(this.rotaAtoB.getId());
		rotaAtoB.setParada(parada);
		
		NewRotaDoVooInputDto rotaBtoC = new NewRotaDoVooInputDto();
		rotaBtoC.setRotaId(this.rotaBtoC.getId());
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaAtoB);
		rotas.add(rotaBtoC);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(this.companhiaA.getId());
		newVooDto.setLugaresDisponiveis(100);
		newVooDto.setRotas(rotas);
		
		return newVooDto;
	}
	
	public NewVooInputDto vooComRotaArgentinaParaBrasilComParadaComTempoNegativoERotaBrasilParaChile() {
		NewParadaDaRotaInputDto parada = new NewParadaDaRotaInputDto();
		parada.setTempo(-10);
		parada.setTipo("ESCALA");
		
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(this.rotaAtoB.getId());
		rotaAtoB.setParada(parada);
		
		NewRotaDoVooInputDto rotaBtoC = new NewRotaDoVooInputDto();
		rotaBtoC.setRotaId(this.rotaBtoC.getId());
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaAtoB);
		rotas.add(rotaBtoC);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(this.companhiaA.getId());
		newVooDto.setLugaresDisponiveis(100);
		newVooDto.setRotas(rotas);
		
		return newVooDto;
	}
	
	public NewVooInputDto vooComRotaInexistente() {
		NewRotaDoVooInputDto rota = new NewRotaDoVooInputDto();
		rota.setRotaId(1L);
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rota);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(this.companhiaA.getId());
		newVooDto.setLugaresDisponiveis(100);
		newVooDto.setRotas(rotas);
		
		return newVooDto;
	}
	
	public NewVooInputDto vooComRotasDeArgentinaParaBrasilRepetidas() {
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(this.rotaAtoB.getId());
		rotaAtoB.setParada(this.paradaValida);
		
		NewRotaDoVooInputDto rotaAtoB2 = new NewRotaDoVooInputDto();
		rotaAtoB2.setRotaId(this.rotaAtoB.getId());
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaAtoB);
		rotas.add(rotaAtoB2);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(this.companhiaA.getId());
		newVooDto.setLugaresDisponiveis(100);
		newVooDto.setRotas(rotas);
		
		return newVooDto;
	}
	
	public NewVooInputDto vooComRotaArgentinaParaBrasilComParada() {
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(this.rotaAtoB.getId());
		rotaAtoB.setParada(this.paradaValida);
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaAtoB);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(this.companhiaA.getId());
		newVooDto.setLugaresDisponiveis(100);
		newVooDto.setRotas(rotas);
		
		return newVooDto;
	}
	
	public NewVooInputDto vooComRotaArgentinaParaBrasilComParadaERotaBrasilParaChileComParada() {
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(this.rotaAtoB.getId());
		rotaAtoB.setParada(this.paradaValida);
		
		NewRotaDoVooInputDto rotaBtoC = new NewRotaDoVooInputDto();
		rotaBtoC.setRotaId(this.rotaBtoC.getId());
		rotaBtoC.setParada(this.paradaValida);
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaAtoB);
		rotas.add(rotaBtoC);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(this.companhiaA.getId());
		newVooDto.setLugaresDisponiveis(100);
		newVooDto.setRotas(rotas);
		
		return newVooDto;
	}
	
	public NewVooInputDto vooComRotaArgentinaParaBrasilSemParadaERotaBrasilParaChileSemParada() {
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(this.rotaAtoB.getId());
		
		NewRotaDoVooInputDto rotaBtoC = new NewRotaDoVooInputDto();
		rotaBtoC.setRotaId(this.rotaBtoC.getId());
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaAtoB);
		rotas.add(rotaBtoC);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(this.companhiaA.getId());
		newVooDto.setLugaresDisponiveis(100);
		newVooDto.setRotas(rotas);
		
		return newVooDto;
	}
	
	public NewVooInputDto vooComRotaArgentinaParaBrasilComParadaERotaBrasilParaArgentinaSemParada() {
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(this.rotaAtoB.getId());
		rotaAtoB.setParada(this.paradaValida);
		
		NewRotaDoVooInputDto rotaBtoA = new NewRotaDoVooInputDto();
		rotaBtoA.setRotaId(this.rotaBtoA.getId());
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaAtoB);
		rotas.add(rotaBtoA);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(this.companhiaA.getId());
		newVooDto.setLugaresDisponiveis(100);
		newVooDto.setRotas(rotas);
		
		return newVooDto;
	}
	
	public NewVooInputDto vooComRotaBrasilParaArgentinaComParadaERotaBrasilParaChileComParadaERotaChileParaArgentinaSemParada() {
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(this.rotaAtoB.getId());
		rotaAtoB.setParada(this.paradaValida);
		
		NewRotaDoVooInputDto rotaBtoC = new NewRotaDoVooInputDto();
		rotaBtoC.setRotaId(this.rotaBtoC.getId());
		rotaBtoC.setParada(this.paradaValida);
		
		NewRotaDoVooInputDto rotaCtoA = new NewRotaDoVooInputDto();
		rotaCtoA.setRotaId(this.rotaCtoA.getId());
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaAtoB);
		rotas.add(rotaBtoC);
		rotas.add(rotaCtoA);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(this.companhiaA.getId());
		newVooDto.setLugaresDisponiveis(100);
		newVooDto.setRotas(rotas);
		
		return newVooDto;
	}
	
	public NewVooInputDto vooComRotaArgentinaParaBrasilComParadaERotaBrasilParaChileComParadaERotaUruguaiParaParaguaiSemParada() {
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(this.rotaAtoB.getId());
		rotaAtoB.setParada(this.paradaValida);
		
		NewRotaDoVooInputDto rotaBtoC = new NewRotaDoVooInputDto();
		rotaBtoC.setRotaId(this.rotaBtoC.getId());
		rotaBtoC.setParada(this.paradaValida);
		
		NewRotaDoVooInputDto rotaUtoP = new NewRotaDoVooInputDto();
		rotaUtoP.setRotaId(this.rotaUtoP.getId());
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaAtoB);
		rotas.add(rotaBtoC);
		rotas.add(rotaUtoP);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(this.companhiaA.getId());
		newVooDto.setLugaresDisponiveis(100);
		newVooDto.setRotas(rotas);
		
		return newVooDto;
	}
	
	public NewVooInputDto vooComRotaBrasilParaArgentinaComParadaERotaBrasilParaChileComParadaERotaBrasilParaArgentinaSemParada() {
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(this.rotaAtoB.getId());
		rotaAtoB.setParada(this.paradaValida);
		
		NewRotaDoVooInputDto rotaBtoC = new NewRotaDoVooInputDto();
		rotaBtoC.setRotaId(this.rotaBtoC.getId());
		rotaBtoC.setParada(this.paradaValida);
		
		NewRotaDoVooInputDto rotaBtoA = new NewRotaDoVooInputDto();
		rotaBtoA.setRotaId(this.rotaBtoA.getId());
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaAtoB);
		rotas.add(rotaBtoC);
		rotas.add(rotaBtoA);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(this.companhiaA.getId());
		newVooDto.setLugaresDisponiveis(100);
		newVooDto.setRotas(rotas);
		
		return newVooDto;
	}
	
	public NewVooInputDto vooComRotaArgentinaParaBrasilSemParadaERotaBrasilParaChileComParada() {
		NewRotaDoVooInputDto rotaAtoB = new NewRotaDoVooInputDto();
		rotaAtoB.setRotaId(this.rotaAtoB.getId());
		
		NewRotaDoVooInputDto rotaBtoC = new NewRotaDoVooInputDto();
		rotaBtoC.setRotaId(this.rotaBtoC.getId());
		rotaBtoC.setParada(this.paradaValida);
		
		List<NewRotaDoVooInputDto> rotas = new ArrayList<>();
		rotas.add(rotaAtoB);
		rotas.add(rotaBtoC);
		
		NewVooInputDto newVooDto = new NewVooInputDto();
		newVooDto.setCompanhiaId(this.companhiaA.getId());
		newVooDto.setLugaresDisponiveis(100);
		newVooDto.setRotas(rotas);
		
		return newVooDto;
	}
	
}
