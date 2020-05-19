package dbexceptions;

public class DBException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DBException(String msg) {
		super(msg);
	}
	
	// DBException é uma classe personalizada de Exceptions
	// que trata de exceptions relacionadas ao acesso com o banco.
	// Derivando de RuntimeException, afim de ser flexivel em tempo de
	// compilação.

}
