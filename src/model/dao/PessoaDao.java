package model.dao;

import java.util.List;

import model.entites.Pessoa;

public interface PessoaDao {
	
	void insert(Pessoa obj);
	void update(Pessoa obj);
	void delete(Integer obj);
	Pessoa selectById(Integer obj);
	List<Pessoa> selectAll(); 
}
