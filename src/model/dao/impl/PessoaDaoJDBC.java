package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import db.DB;
import dbexceptions.DBException;
import model.dao.PessoaDao;
import model.entites.Pessoa;

public class PessoaDaoJDBC implements PessoaDao {

	// PessoaDaoJDBC implementa os contratos de PessoaDao,
	// sendo a camada entre as entidades do sistema e os dados
	// do banco relacional.

	private Connection conn; // atributo que guarda a conexão com o banco.

	private int rowsAffected; // atributo de controle que guarda o número de linhas
	// afetadas em cada operação DML.

	public PessoaDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	// método construtor que inicializa o atributo com um conexão
	// que provém da classe DB por intermédio de DaoFactory.

	@Override
	public void insert(Pessoa obj) {
		// método de inserção de registros no banco.

		PreparedStatement st = null;

		try {

			conn.setAutoCommit(false);

			st = conn.prepareStatement("INSERT INTO PESSOA " + "(nome, idade) VALUES " + "(?, ?)");
			// método prepareStatement da classe Connection que guarda uma instrução SQL.

			List<Pessoa> filteredPessoa = selectAll().stream().filter(p -> p.equals(obj)).collect(Collectors.toList());
			// Lógica que consiste em verificar se já há um registro com o nome e idade
			// passados, através do método que retorna
			// todos os registros através de uma lista e atribuindo esse valor (nulo ou não)
			// em uma lista que serve como filtro.

			if (filteredPessoa.isEmpty()) {
				// se a lista estiver vazia, indica que não nenhum registros com o valor
				// passado.
				// Assim atribuindo esses valores em um novo registro no banco.
				st.setString(1, obj.getNome());
				st.setInt(2, obj.getIdade());

				rowsAffected = st.executeUpdate();

				System.out.println("linhas afetadas: " + rowsAffected);

			} else {
				System.out.println("pessoa já cadastrada no banco de dados.");
			}
			conn.commit();
		}

		catch (SQLException e) {
			try {
				conn.rollback();
				// se ocorrer um erro no meio do processo de inserção
				// essa exception vai estourar e chamar o método rollback(),
				// que vai voltar o banco ao seu estado antes da transação.
				// Assim asegurando a consitência do banco e obdecendo as boas
				// práticas de uma transação segura.
				throw new DBException(e.getMessage());
			} catch (SQLException e1) {
				throw new DBException(e.getMessage());
			}

		}

		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Pessoa obj) {

		// método responsável por atualizar um registro no banco.
		// Possibilitando apenas a alteração do nome.
		PreparedStatement st = null;

		try {
			conn.setAutoCommit(false);
			st = conn.prepareStatement("Update pessoa SET nome = ? WHERE id = ? LIMIT 1");

			st.setString(1, obj.getNome());
			st.setInt(2, obj.getId());

			rowsAffected = st.executeUpdate();
			System.out.println("linhas afetadas: " + rowsAffected);
			conn.commit();
		}

		catch (SQLException e) {
			try {
				conn.rollback();
				throw new DBException(e.getMessage());
			} catch (SQLException e1) {
				System.out.println(e.getMessage());
				e1.printStackTrace();
			}
		}

	}

	@Override
	public void delete(Integer obj) {

		// método responsável por deletar um registro do banco.
		PreparedStatement st = null;

		try {
			conn.setAutoCommit(false);
			st = conn.prepareStatement("DELETE FROM pessoa WHERE id = ? LIMIT 1");

			st.setInt(1, obj);

			rowsAffected = st.executeUpdate();
			System.out.println("linhas afetadas: " + rowsAffected);
			conn.commit();
		}

		catch (SQLException e) {

			try {
				conn.rollback();
				throw new DBException(e.getMessage());
			}

			catch (SQLException e1) {
				throw new DBException(e.getMessage());
			}

		}

	}

	@Override
	public Pessoa selectById(Integer obj) {

		// método que retorna um registro pelo id especificado.
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			Pessoa pessoa = new Pessoa();
			st = conn.prepareStatement("SELECT * FROM pessoa WHERE id = ? ");

			st.setInt(1, obj);

			rs = st.executeQuery();

			if (rs.next()) {
				pessoa = new Pessoa(rs.getInt("id"), rs.getString("nome"), rs.getInt("idade"));
			}

			return pessoa;

		}

		catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public List<Pessoa> selectAll() {
		// método que retorna todos os registros do banco de dados.

		List<Pessoa> pessoas = new ArrayList<>();
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT * FROM PESSOA");
			rs = st.executeQuery();

			while (rs.next()) {
				pessoas.add(new Pessoa(rs.getInt("id"), rs.getString("nome"), rs.getInt("idade")));
			}

			return pessoas;
		}

		catch (SQLException e) {
			throw new DBException(e.getMessage());
		}

		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
