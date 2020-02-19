package br.com.caelum.viagens.administrativo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.caelum.viagens.administrativo.model.Pais;

public interface PaisRepository extends CrudRepository<Pais, Long> {

	Optional<Pais> findByNome(String nome);

	Optional<Pais> findById(Long id);

}
