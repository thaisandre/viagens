package br.com.caelum.viagens.administrativo.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import br.com.caelum.viagens.administrativo.model.Pais;

public interface PaisRepository extends Repository<Pais, Long> {

	void save(Pais pais);

	Optional<Pais> findByNome(String nome);
	
	Optional<Pais> findById(Long id);

}