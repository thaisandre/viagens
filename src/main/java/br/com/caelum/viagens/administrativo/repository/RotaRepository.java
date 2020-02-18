package br.com.caelum.viagens.administrativo.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import br.com.caelum.viagens.administrativo.model.Aeroporto;
import br.com.caelum.viagens.administrativo.model.Rota;

public interface RotaRepository extends Repository<Rota, Long>{

	Rota save(Rota rota);
	
	Optional<Rota> findById(Long id);
	
	Optional<Rota> findByOrigemAndDestino(Aeroporto origem, Aeroporto destino);

}
