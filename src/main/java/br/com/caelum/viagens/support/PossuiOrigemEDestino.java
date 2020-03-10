package br.com.caelum.viagens.support;

import br.com.caelum.viagens.administrativo.model.Aeroporto;

public interface PossuiOrigemEDestino {
	
	Aeroporto getOrigem();
	Aeroporto getDestino();

}
