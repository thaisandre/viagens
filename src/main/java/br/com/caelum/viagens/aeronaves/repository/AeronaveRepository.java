package br.com.caelum.viagens.aeronaves.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.caelum.viagens.aeronaves.model.Aeronave;
import br.com.caelum.viagens.aeronaves.model.Assento;

public interface AeronaveRepository extends CrudRepository<Aeronave, Long>{
	
	Aeronave findByAssentos(Assento assento);
}
