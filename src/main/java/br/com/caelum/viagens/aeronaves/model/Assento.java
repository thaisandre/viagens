package br.com.caelum.viagens.aeronaves.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.util.Assert;

@Entity
public class Assento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Positive
	@NotNull
	private Integer fileira;
	
	@NotNull
	private Character posicao;
	
	@NotNull
	@ManyToOne
	private Aeronave aeronave;
	
	@Deprecated
	public Assento() {}
	
	public Assento(@NotNull @Positive Integer fileira, @NotNull Character posicao, Aeronave aeronave) {
		Assert.notNull(fileira, "fileira não pode ser nulo.");
		Assert.isTrue(fileira > 0, "fileira deve ser positivo.");
		Assert.notNull(posicao, "posicao não pode ser nulo.");
		Assert.isTrue(Character.isLetter(posicao), "posicao deve ser uma letra.");
		Assert.notNull(aeronave, "aeronave não pode ser nulo.");
		this.fileira = fileira;
		this.posicao = posicao;
		this.aeronave = aeronave;
	}
	
	public Integer getFileira() {
		return fileira;
	}
	
	public Character getPosicao() {
		return posicao;
	}
	
	@Override
	public String toString() {
		return fileira+posicao.toString();
	}
}
