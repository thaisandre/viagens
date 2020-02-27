package br.com.caelum.viagens.voos.validator.setup;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.model.Pais;
import br.com.caelum.viagens.administrativo.model.Rota;

public class ListaDeRotasSetUp {
	
	public static List<Rota> populaRotas() {
		
		Rota rota1 = new Rota(new Aeroporto("AeroportoA", new Pais("Argentina")), 
				new Aeroporto("AeroportoB", new Pais("Brasil")), 120);
		
		Rota rota2 = new Rota(new Aeroporto("AeroportoA", new Pais("Argentina")), 
				new Aeroporto("AeroportoC", new Pais("Chile")), 120);
		
		Rota rota3 = new Rota(new Aeroporto("AeroportoA", new Pais("Argentina")), 
				new Aeroporto("AeroportoU", new Pais("Uruguai")), 120);
		
		Rota rota4 = new Rota(new Aeroporto("AeroportoB", new Pais("Brasil")), 
				new Aeroporto("AeroportoA", new Pais("Argentina")), 120);
		
		Rota rota5 = new Rota(new Aeroporto("AeroportoB", new Pais("Brasil")), 
				new Aeroporto("AeroportoC", new Pais("Chile")), 120);
		
		Rota rota6 = new Rota(new Aeroporto("AeroportoB", new Pais("Brasil")), 
				new Aeroporto("AeroportoU", new Pais("Uruguai")), 120);
		
		Rota rota7 = new Rota(new Aeroporto("AeroportoC", new Pais("Chile")), 
				new Aeroporto("AeroportoA", new Pais("Argentina")), 120);
		
		Rota rota8 = new Rota(new Aeroporto("AeroportoC", new Pais("Chile")), 
				new Aeroporto("AeroportoB", new Pais("Brasil")), 120);
		
		Rota rota9 = new Rota(new Aeroporto("AeroportoC", new Pais("Chile")), 
				new Aeroporto("AeroportoU", new Pais("Uruguai")), 120);
		
		Rota rota10 = new Rota(new Aeroporto("AeroportoU", new Pais("Uruguai")), 
				new Aeroporto("AeroportoA", new Pais("Argentina")), 120);
		
		Rota rota11 = new Rota(new Aeroporto("AeroportoU", new Pais("Uruguai")), 
				new Aeroporto("AeroportoB", new Pais("Brasil")), 120);
		
		Rota rota12 = new Rota(new Aeroporto("AeroportoU", new Pais("Uruguai")), 
				new Aeroporto("AeroportoC", new Pais("Chile")), 120);
		
		List<Rota> rotas = new ArrayList<>();
		rotas.add(rota1);
		rotas.add(rota2);
		rotas.add(rota3);
		rotas.add(rota4);
		rotas.add(rota5);
		rotas.add(rota6);
		rotas.add(rota7);
		rotas.add(rota8);
		rotas.add(rota9);
		rotas.add(rota10);
		rotas.add(rota11);
		rotas.add(rota12);
		
		return rotas;
	}

}
