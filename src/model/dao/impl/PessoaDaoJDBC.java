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

	private Connection conn;

	private int rowsAffected;

	public PessoaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Pessoa obj) {
		PreparedStatement st = null;

		try {

			conn.setAutoCommit(false);
			st = conn.prepareStatement("INSERT INTO PESSOA " + "(nome, idade) VALUES " + "(?, ?)");

			List<Pessoa> filteredPessoa = selectAll().stream().filter(p -> p.equals(obj)).collect(Collectors.toList());

			if (filteredPessoa.isEmpty()) {
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

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			Pessoa pessoa = new Pessoa();
			st = conn.prepareStatement("SELECT * FROM pessoa WHERE id = ? ");

			st.setInt(1, obj);

			rs = st.executeQuery();

			if (rs.next()) {
				pessoa = new Pessoa(rs.getInt("id"), rs.getString("nome"), rs.getInt("idade"));
				return pessoa;
			}

			return pessoa;

		}

		catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public List<Pessoa> selectAll() {

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
