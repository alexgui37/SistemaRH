package SistemaRH.BDconfig;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import SistemaRH.model.Candidato;
import SistemaRH.model.Especialidade;
import SistemaRH.model.Funcionario;
import SistemaRH.model.Vaga;

public class DAO_RH4 {
	//Metodo(s) relacionado(s) ao Caso de Uso RH4
		public static synchronized Candidato getCandidato(String cpf) {
			Candidato cand = null;
			try {
				//Informacoes necessarias para popular o objeto candidato
				//Nome...
				String nome,status;
				Especialidade esp = new Especialidade();
				ConexaoBD a = new ConexaoBD();
				a.iniciaBd();
				Connection c = a.getConexao();
				PreparedStatement ps = (PreparedStatement) c.prepareStatement("SELECT nm_nome_completo FROM pessoa WHERE cd_cpf = ?;");
				ps.setString(1, cpf);
				ResultSet rs = ps.executeQuery();
				nome = rs.getString("nm_nome_completo");
				//Status..
				ps = (PreparedStatement) c.prepareStatement("SELECT ct.ds_situacao FROM concurso_candidato_situacao_tipo as ct "
						+ "JOIN concurso_candidato_historico as cc ON cc.id_situacao_nova = ct.id_candidato_situacao_tipo;"
						+ "WHERE cc.cd_cpf = ?");
				ps.setString(1,cpf);
				rs = ps.executeQuery();
				status = rs.getString("ds_situacao");
				//Especialidade ... 
				esp = DAO_Util.getEspCand(cpf);
				
				cand = new Candidato(cpf,esp,status,nome);
				
			}catch(SQLException s) {
				s.getStackTrace();
			}
			return cand;
		}
		public static synchronized Funcionario candEfetivado(Candidato efetivado,int id_vaga) {
			Funcionario novo_func = null;
			Vaga v = DAO_Util.getVaga(efetivado.getCad_esp(), id_vaga);
			try {
				ConexaoBD a = new ConexaoBD();
				a.iniciaBd();
				Connection c = a.getConexao();
				//Alterando o historico de status do candidato alem de seu status atual
				DAO_Util.setStatusCandidato("Efetivado",efetivado.getCPF());
				//Alterando o historico da vaga como ocupada no banco de dados
				PreparedStatement ps = (PreparedStatement) c.prepareStatement("UPDATE concurso_vaga_historico as vh "
						+ "JOIN concurso_candidato as cc ON cc.id_situacao = vh.id_situacao"
						+ "SET vh.fl_vaga_ocupada = 3  "
						+ "WHERE vh.id_nu_vaga = ? and cc.cd_cpf = ?");
				ps.setInt(1,id_vaga);
				ps.setString(2, efetivado.getCPF());
				v.setStatus("Ocupada");
				ps.executeQuery();
				//Alerando os dados da especialidade ao qual o candidato pertence
				ps = (PreparedStatement) c.prepareStatement("UPDATE concurso_especialidade as ce"
						+ "SET ce.nu_nomedados = ?,ce.nu_banco_restante = ?"
						+ "WHERE ce.id_concurso_especialidade = ?");
				ps.setInt(1, efetivado.getCad_esp().getQtd_nomeados() + 1);
				ps.setInt(2, efetivado.getCad_esp().getCand_restantes() - 1);
				ps.setInt(3, efetivado.getCad_esp().getId_especialidade());
				//Agora o candidato efetivado e um funcionario
				//Inserindo seu dados na tabela funcionario
				//OBS: id_matricula auto_increment?
				ps = (PreparedStatement) c.prepareStatement("INSERT INTO funcionario (id_concurso_especialidade,cd_cpf,dt_portaria_nomeacao,ds_codigo_portaria"
						+ "VALUES (?,?,?,?)");
				ps.setInt(1,efetivado.getCad_esp().getId_especialidade());
				ps.setString(2,efetivado.getCPF());
				Calendar ca = Calendar.getInstance();
				Date d = (Date) ca.getTime();
				ps.setDate(3, d);
				//ps.setString(4,"O que colocar aqui ?");
				ps.execute();
				//Construindo o objeto funcionario
				//Obtendo a matricula ...
				ps= (PreparedStatement) c.prepareStatement("SELECT id_matricula FROM funcionario WHERE cd_cpf = ?");
				ps.setString(1, efetivado.getCPF());
				ResultSet rs = ps.executeQuery();
				String mat = Integer.toString(rs.getInt("id_matricula"));
				//Atribuindo os valores determinados nos atributos de funcionario
				novo_func = new Funcionario(mat,efetivado.getCPF(),efetivado.getNome(),true,v.getNum_vaga());
				//Terminou
			}catch(SQLException s) {
				s.getStackTrace();
			}
			return novo_func;
		}
}
