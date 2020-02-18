package br.com.caelum.viagens.administrativo.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import br.com.caelum.viagens.administrativo.model.Aeroporto;

public interface AeroportoRepository extends Repository<Aeroporto, Long>{

	Aeroporto save(Aeroporto aeroporto);
	
	Optional<Aeroporto> findById(Long id);

	Optional<Aeroporto> findByNome(String nome);

}
