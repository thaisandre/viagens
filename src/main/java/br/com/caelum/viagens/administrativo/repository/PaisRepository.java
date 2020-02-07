package br.com.caelum.viagens.administrativo.repository;

import org.springframework.data.repository.Repository;

import br.com.caelum.viagens.administrativo.model.Pais;

public interface PaisRepository extends Repository<Pais, Long> {

	void save(Pais pais);

}
