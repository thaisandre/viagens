package br.com.caelum.viagens.administrativo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.caelum.viagens.administrativo.model.Aeroporto;

public interface AeroportoRepository extends CrudRepository<Aeroporto, Long>{

	Optional<Aeroporto> findById(Long id);

	Optional<Aeroporto> findByNome(String nome);

}
