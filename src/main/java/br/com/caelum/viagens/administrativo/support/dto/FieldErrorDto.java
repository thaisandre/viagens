package br.com.caelum.viagens.administrativo.support.dto;

public class FieldErrorDto {

	private String campo;
	private String mensagem;

	public FieldErrorDto(String campo, String mensagem) {
		this.campo = campo;
		this.mensagem = mensagem;
	}
	
	public String getCampo() {
		return campo;
	}

	public String getMensagem() {
		return mensagem;
	}
}
