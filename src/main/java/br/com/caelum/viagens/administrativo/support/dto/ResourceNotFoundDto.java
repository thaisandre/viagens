package br.com.caelum.viagens.administrativo.support.dto;

public class ResourceNotFoundDto {
	
	private String mensagem;
	
	public ResourceNotFoundDto(String mensagem) {
		this.mensagem = mensagem;
	}
	
	public String getMensagem() {
		return mensagem;
	}
}
