package model.dao;

import db.DB;
import model.dao.impl.PessoaDaoJDBC;

public class DaoFactory {

	public static PessoaDao createPessoaDao() {
		return new PessoaDaoJDBC(DB.getConnection());
	}

	// O m�todo createPessoaDao simplismente retorna uma nova instancia de
	// PessoaDaoJDBC.
	// Dessa forma, sendo uma maneira de encapsular a inst�ncia de um objeto
	// PessoaDaoJDBC,
	// de modo que o programador n�o tem acesso direto ao m�todos da classe.
}
