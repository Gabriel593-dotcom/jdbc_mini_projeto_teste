package model.dao;

import db.DB;
import model.dao.impl.PessoaDaoJDBC;

public class DaoFactory {

	public static PessoaDao createPessoaDao() {
		return new PessoaDaoJDBC(DB.getConnection());
	}

	// O método createPessoaDao simplismente retorna uma nova instancia de
	// PessoaDaoJDBC.
	// Dessa forma, sendo uma maneira de encapsular a instância de um objeto
	// PessoaDaoJDBC,
	// de modo que o programador não tem acesso direto ao métodos da classe.
}
