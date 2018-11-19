package SistemaRH.model;

public class Vaga {
	private String num_vaga;
	private String status_vaga;
	private String num_processo_conc;
	private Especialidade vaga_espec;
	
	public void setStatus(String status) {
		this.status_vaga = status;
	}
	
	public void setProcesso(String num_proc) {
		this.num_processo_conc = num_proc;
	}
	public Especialidade getVaga_espec() {
		return vaga_espec;
	}

	public void setVaga_espec(Especialidade vaga_espec) {
		this.vaga_espec = vaga_espec;
	}
}
