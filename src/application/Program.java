package application;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.PessoaDao;
import model.entites.Pessoa;

public class Program {

	private static Scanner sc = new Scanner(System.in);
	private static int x = 0;

	public static void main(String[] args) {

		PessoaDao pessoaDao = DaoFactory.createPessoaDao();
		String nome;
		int idade;
		int id;
		char esc;

		try {
			menu();

			while (x != 6) {
				switch (x) {
				case 1:
					System.out.println();
					System.out.println("<TODOS OS REGISTROS>");
					List<Pessoa> pessoas = pessoaDao.selectAll();
					pessoas.forEach(System.out::println);
					System.out.println();
					System.out.print("algo mais? [s/n] ");
					choiceService(esc = sc.next().charAt(0));
					break;

				case 2:
					System.out.println();
					System.out.println("<PESQUISA>");
					System.out.print("insira o id: ");
					Pessoa pessoa = pessoaDao.selectById(sc.nextInt());
					System.out.println(pessoa.toString());
					System.out.println();
					System.out.print("algo mais? [s/n] ");
					choiceService(esc = sc.next().charAt(0));
					break;

				case 3:
					System.out.println();
					System.out.print("Insira a idade: ");
					idade = sc.nextInt();
					System.out.print("Insira o nome: ");
					sc.nextLine();
					nome = sc.nextLine();
					pessoaDao.insert(new Pessoa(idade, nome));
					System.out.println();
					System.out.print("algo mais? [s/n] ");
					choiceService(esc = sc.next().charAt(0));
					break;

				case 4:
					System.out.println();
					System.out.print("digite o id: ");
					id = sc.nextInt();
					System.out.print("Digite o nome: ");
					sc.nextLine();
					nome = sc.nextLine();
					pessoaDao.update(new Pessoa(nome, id));
					System.out.println();
					System.out.print("algo mais? [s/n] ");
					choiceService(esc = sc.next().charAt(0));
					break;

				case 5:
					System.out.println();
					System.out.print("Digite o id: ");
					id = sc.nextInt();
					pessoaDao.delete(id);
					System.out.println();
					System.out.print("algo mais? [s/n] ");
					choiceService(esc = sc.next().charAt(0));
					break;

				default:

					x = 6;
					break;
				}

			}

		}

		catch (InputMismatchException e) {
			System.out.println("Erro:\nvalor inválido para o campo.");
		}

		finally {
			System.out.println("Até mais!");
			sc.close();
		}

	}

	public static void menu() {
		System.out.println();
		System.out.println("1 - Consultar todos os registros");
		System.out.println("2 - Consultar registro por id");
		System.out.println("3 - Inserir registro");
		System.out.println("4 - Atualizar registro (nome)");
		System.out.println("5 - Deletar registro");
		System.out.println("6 - Sair");
		System.out.println();
		System.out.print("escolha um serviço: ");
		x = sc.nextInt();
	}

	public static int choiceService(char esc) {
		if (esc == 's' || esc == 'S') {
			menu();
		}

		else if (esc == 'n' || esc == 'N') {
			x = 6;
		}
		
		else {
			System.out.println("valor inválido!");
			x = 0;
		}

		System.out.println();

		return x;
	}

}
