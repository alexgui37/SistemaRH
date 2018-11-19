package SistemaRH.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Concurso {
	private String num_processo;
	private String descricao;
	private boolean prorrogavel;
	private ArrayList<Especialidade> espec_conc;
	private Set<Candidato> candidatos_conc;
	private Date DT_realizacao;
	private Date DT_validade;
	private HashMap<Especialidade,Vaga[]> vagas_conc;
	
	public Concurso() {
		
	}
	
	public boolean addVaga(Especialidade esp,Vaga[] vagas_add) {
		//Lógica do caso de Uso RH2
		for(int i = 0; i< vagas_add.length;i++) {
			if(!esp.equals(vagas_add[i])) return false;
		}
		for(Especialidade e: espec_conc) {
			if(e.equals(esp)) {
				vagas_conc.put(esp, vagas_add);
				break;
			}
		}
		return true;
	}
	
	public Candidato chamaCandidatos(String CPF) {
		Candidato cand = new Candidato();
		return cand;
	}
}
