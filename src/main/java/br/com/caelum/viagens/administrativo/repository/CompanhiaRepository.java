package br.com.caelum.viagens.administrativo.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import br.com.caelum.viagens.administrativo.model.Companhia;

public interface CompanhiaRepository extends Repository<Companhia, Long>{

	void save(Companhia companhia);

	Optional<Companhia> findByNome(String nome);

}
