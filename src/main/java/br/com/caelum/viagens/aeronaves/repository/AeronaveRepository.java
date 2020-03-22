package br.com.caelum.viagens.aeronaves.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.caelum.viagens.aeronaves.model.Aeronave;

public interface AeronaveRepository extends CrudRepository<Aeronave, Long>{
	
}
