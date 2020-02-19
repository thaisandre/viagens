package br.com.caelum.viagens.administrativo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.caelum.viagens.administrativo.model.Rota;

public interface RotaRepository extends CrudRepository<Rota, Long>{

	Optional<Rota> findById(Long id);
	
	Optional<Rota> findByOrigemIdAndDestinoId(Long origemId, Long destinoId);

}
