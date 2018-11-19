package SistemaRH.model;

import java.sql.Date;

public abstract class Pessoa {
	private String nome;
	private Date data_nascimento;
	private String CPF;
	private String etnia;
	private boolean PCD;
	private String cod_concurso;
	private String status;
	
	public abstract void setStatus(String status);
}
