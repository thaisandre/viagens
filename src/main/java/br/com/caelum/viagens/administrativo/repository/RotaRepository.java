package br.com.caelum.viagens.administrativo.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import br.com.caelum.viagens.administrativo.model.Rota;

public interface RotaRepository extends Repository<Rota, Long>{

	void save(Rota rota);
	
	Optional<Rota> findById(Long id);

}
