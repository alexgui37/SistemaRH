package SistemaRH.model;

import java.sql.Date;

public class Funcionario extends Pessoa{
	private String matricula;
	private Especialidade esp_func;
	private Date data_admissao;
	private Date data_desligamento;
	private String unidade;
	private Vaga vaga_func;
	
	public Vaga gerar_vaga() {
		Vaga novaVaga = new Vaga();
		
		return novaVaga;
	}
	@Override
	public void setStatus(String status) {
		// TODO Auto-generated method stub
		
	}

}
