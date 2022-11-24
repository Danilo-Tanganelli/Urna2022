import java.util.*;
import static java.lang.Math.toIntExact;

public class Simulador {
	private static String myEnter = "\r\n";

	public static void main(String[] args) {
		Candidatos listaCandidatos = new Candidatos();
		ArrayList<Eleitor> eleitores = new ArrayList<>();
		Eleicao eleicao = new Eleicao();

		boolean fFinaliza = false;

		do {

			int op = showMenuPrincipal();

			switch (op) {
			case 1:
				listaCandidatos.setCandidatosPrefeito(menuCadastraCandidato(Cargo.Prefeito));
				listaCandidatos.setCandidatosGovernador(menuCadastraCandidato(Cargo.Governador));
				listaCandidatos.setCandidatosPresidente(menuCadastraCandidato(Cargo.Presidente));

				break;

			case 2:
				eleitores = menuCadastraEleitores();
				break;

			case 3:
				if (listaCandidatos.isVoid() | eleitores.size() <= 0) {
					System.out.println(
							"Cadastre candidatos e eleitores antes de realizar a eleicao!" + myEnter + myEnter);
				} else {
					eleicao = new Eleicao(listaCandidatos, eleitores);
					eleicao.realizarEleicao();
				}
				break;

			case 4:

				if (!eleicao.isVoid()) {

					System.out.println(eleicao.getUrna().totalizacao());
					System.out.println(eleicao.totalizaBrancoNulo());
				} else {
					System.out.println("A eleicao ainda nao ocorreu. Realize a eleicao antes de realizar a apuracao!"
							+ myEnter + myEnter);
				}
				break;

			case 5:

				System.out.println(listaCandidatos.toString());

				System.out.println();

				String texto = "Listagem de eleitores" + myEnter;
				for (Iterator<Eleitor> iterator = eleitores.iterator(); iterator.hasNext();) {
					texto = texto + iterator.next().toString() + myEnter;
				}

				System.out.println(texto);
				System.out.println();
				System.out.println("Fim Relatorio");

				break;

			case 6:
				fFinaliza = true;
				break;

			default:
				break;
			}

		} while (!fFinaliza);
	}

	static int showMenuPrincipal() {
		System.out.println("+++++++ MENU - SIMULADOR DO SISTEMA DE VOTACAO +++++++");
		System.out.println();
		System.out.println();

		System.out.println("1. Cadastrar Candidatos");
		System.out.println("2. Cadastrar Eleitores");
		System.out.println("3. Votar");
		System.out.println("4. Apurar Resultados");
		System.out.println("5. Relatorio e Estatisticas");
		System.out.println("6. Encerrar");

		int op = 0;
		try {
			Scanner e = new Scanner(System.in);
			op = e.nextInt();
			return op;
		} catch (Exception e) {
			System.out.println("Digite apenas numeros, entre 1 e 6.");
			op = -1;
		}
		return op;
	}

	// Realiza a leitura de um candidato
	static ArrayList<Candidato> menuCadastraCandidato(Cargo cargoCadastro) {

		ArrayList<Candidato> candidatos = new ArrayList<>();

		System.out.println(
				"+++++++ MENU - CADASTRO DE CANDIDATOS A " + cargoCadastro.descricao.toUpperCase() + " +++++++");
		System.out.println("Digite as informacoes necessarias ou NAO para finalizar o cadastro");
		System.out.println();

		do {
			Scanner e = new Scanner(System.in);

			System.out.println("Digite o nome, ou NAO para finalizar: ");
			String nome = e.nextLine();
			try {
				// Interrompe o cadastro
				if (nome.trim().toUpperCase().equals("NAO")) {
					break;
				}

				System.out.println("Digite o numero:  ");
				int numero = e.nextInt();
				System.out.println("Digite o partido: ");
				String partido = e.next();

				Cargo cargo = cargoCadastro;

				Candidato candidato = new Candidato(nome, numero, partido, cargo);

				if (!candidatoCadastrado(candidatos, candidato)) {
					candidatos.add(candidato);
				} else {
					System.out.println("Cada candidato deve possuir um numero unico. Verifique e tente novamente.");
				}

			} catch (InputMismatchException imex) {
				System.out.println(
						"Foram digitados tipo de dados invalidos. Nome, Partido e tipo Texto e Numero e numerico apenas. ");
			} catch (Exception ex) {
				System.out.println("Ocorreu algum erro ao entrar com os dados, tente de novo.");
				System.out.println(ex);
			}

		} while (true);

		return candidatos;
	}

	private static boolean candidatoCadastrado(ArrayList<Candidato> candidatos, Candidato candidato) {

		for (Iterator<Candidato> iterator = candidatos.iterator(); iterator.hasNext();) {
			Candidato temp = (Candidato) iterator.next();

			if (candidato.getNumero() == temp.getNumero()) {
				return true;
			}
		}
		return false;

	}

	// Realiza a leitura dos eleitores
	static ArrayList<Eleitor> menuCadastraEleitores() {

		ArrayList<Eleitor> eleitores = new ArrayList<>();

		System.out.println("+++++++ MENU - CADASTRO DE ELEITORES +++++++");
		System.out.println("Digite as informacoes necessarias ou NAO para finalizar o cadastro");
		System.out.println();

		do {
			Scanner e = new Scanner(System.in);

			System.out.println("Digite o nome, ou NAO para finalizar: ");

			try {

				String nome = e.nextLine();

				// Interrompe o cadastro
				if (nome.trim().toUpperCase().equals("NAO")) {
					break;
				}

				System.out.println("Digite o CPF:  ");
				long cpf = e.nextLong();

				Eleitor eleitor = new Eleitor(nome, cpf);

//			if (eleitor.validaCPF()) {
				eleitores.add(eleitor);

			} catch (InputMismatchException imex) {
				System.out.println("Foram digitados tipo de dados invalidos. Nome e Texto e CPF e numerico apenas. ");
			} catch (Exception ex) {
				System.out.println("Ocorreu algum erro ao entrar com os dados, tente de novo.");
				System.out.println(ex);
			}

//			} else {
//				System.out.println("CPF Invalido, eleitor nao incluido. Tente novamente. ");
//			}

		} while (true);

		return eleitores;
	}

}

class Candidato {

	private String nome;
	private int numero;
	private String partido;
	private Cargo cargo;
	private int votos = 0;

	public Candidato(String nome, int numero, String partido, Cargo cargo) {
		super();
		this.nome = nome;
		this.numero = numero;
		this.partido = partido;
		this.cargo = cargo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getPartido() {
		return partido;
	}

	public void setPartido(String partido) {
		this.partido = partido;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public int getVotos() {
		return votos;
	}

	public void addVotos() {
		votos++;
	}

	@Override
	public String toString() {
		return String.format("%30s %7s %7s", this.nome, this.numero, this.partido);
	}

	public String totalVotos(int iTotal) {

		float percentVotos = 0f;
		if (iTotal <= 0) {
			percentVotos = 0;
		} else {
			float a, b;
			a = Float.valueOf(this.votos);
			b = Float.valueOf(iTotal);
			percentVotos = (a / b) * 100 ;
		}

		return String.format("%30s %7s %7s %.3f%%", this.nome, this.partido, this.votos, percentVotos);
	}

}

class BrancoNulo {
	private static String myEnter = "\r\n";
	private int brancos;
	private int nulos;

	public int getBrancos() {
		return brancos;
	}

	public void addBrancos() {
		this.brancos++;
	}

	public int getNulos() {
		return nulos;
	}

	public void addNulos() {
		this.nulos++;
	}

	public BrancoNulo() {
		brancos = 0;
		nulos = 0;
	}

	public String totalizacao(Cargo cargo) {

		String texto = "Total de brancos e nulos para " + cargo.descricao + myEnter;
		texto = texto + "     Brancos: " + this.getBrancos() + myEnter;
		texto = texto + "     Nulos:   " + this.getNulos() + myEnter;

		return texto;
	}

}

class Candidatos {

	private static String myEnter = "\r\n";

	private ArrayList<Candidato> candidatosPrefeito = new ArrayList<>();
	private ArrayList<Candidato> candidatosGovernador = new ArrayList<>();
	private ArrayList<Candidato> candidatosPresidente = new ArrayList<>();

	public Candidatos() {
		super();
	}

	// Retorna se o objeto tem algum candidato
	public boolean isVoid() {

		if (Objects.isNull(candidatosPrefeito) & Objects.isNull(candidatosGovernador)
				& Objects.isNull(candidatosPresidente)) {
			return true;
		}

		if (candidatosPrefeito.size() <= 0 & candidatosGovernador.size() <= 0 & candidatosPresidente.size() <= 0) {
			return true;
		}

		return false;

	}

	public ArrayList<Candidato> getCandidatosPrefeito() {
		return candidatosPrefeito;
	}

	public void setCandidatosPrefeito(ArrayList<Candidato> candidatosPrefeito) {
		this.candidatosPrefeito = candidatosPrefeito;
	}

	public ArrayList<Candidato> getCandidatosGovernador() {
		return candidatosGovernador;
	}

	public void setCandidatosGovernador(ArrayList<Candidato> candidatosGovernador) {
		this.candidatosGovernador = candidatosGovernador;
	}

	public ArrayList<Candidato> getCandidatosPresidente() {
		return candidatosPresidente;
	}

	public void setCandidatosPresidente(ArrayList<Candidato> candidatosPresidente) {
		this.candidatosPresidente = candidatosPresidente;
	}

	public static String listagemCandidatos(ArrayList<Candidato> candidatos, String cargo) {

		String texto = "Lista de candidatos disponiveis para " + cargo + myEnter;

		for (Iterator iterator = candidatos.iterator(); iterator.hasNext();) {
			texto = texto + iterator.next().toString() + myEnter;
		}

		texto = texto + myEnter + "-1 = branco, -2 = nulo" + myEnter;

		return texto;
	}

	// Busca o candidato, se existir adiciona o voto
	public static boolean votar(ArrayList<Candidato> candidatos, int numero) {
		for (int i = 0; i < candidatos.size(); i++) {
			if (candidatos.get(i).getNumero() == numero) {
				Candidato candidato = candidatos.get(i);
				candidato.addVotos();
				candidatos.set(i, candidato);
				return true;
			}
		}
		return false;
	}

	private int getTotalVotosCandidatos(ArrayList<Candidato> candidatos) {
		int totVotos = 0;
		for (Iterator<Candidato> iterator = candidatos.iterator(); iterator.hasNext();) {
			Candidato candidato = (Candidato) iterator.next();
			totVotos = totVotos + candidato.getVotos();
		}
		return totVotos;
	}

	private String subTotal(ArrayList<Candidato> candidatos, Cargo cargo) {
		int indice = 0;
		int iVencedor = 0;
		String texto = "";

		texto = texto + cargo.descricao + myEnter;

		int totVotos = this.getTotalVotosCandidatos(candidatos);

		for (Iterator<Candidato> iterator = candidatos.iterator(); iterator.hasNext();) {

			int iNumVotosVencedor = 0;

			Candidato candidato = (Candidato) iterator.next();
			texto = texto + candidato.totalVotos(totVotos) + myEnter;

			if (iNumVotosVencedor < candidato.getVotos()) {
				iVencedor = indice;
			}

			indice++;
		}

		if (indice > 0) {
			texto = texto + "Vencedor para " + cargo.descricao + " = " + candidatos.get(iVencedor).toString() + myEnter;
		}

		return texto;
	}

	public String totalizacao() {

		String texto = "+++++++++++++++ TOTAL DE VOTOS +++++++++++++++ " + myEnter;

		texto = texto + subTotal(candidatosPrefeito, Cargo.Prefeito);
		texto = texto + subTotal(candidatosGovernador, Cargo.Governador);
		texto = texto + subTotal(candidatosPresidente, Cargo.Presidente);

		return texto;
	}

	@Override
	public String toString() {

		String texto = "+++++++++++++++ LISTAGEM DE CANDIDATOS +++++++++++++++ " + myEnter;
		texto = texto + "++++++++++++++++++++++++++++++++++++++++++++++++++++++ " + myEnter;
		texto = texto + "++++ PREFEITO ++++++++++++++++++++++++++++++++++++++++ " + myEnter;

		for (Iterator iterator = candidatosPrefeito.iterator(); iterator.hasNext();) {
			texto = texto + iterator.next().toString() + myEnter;
		}
		texto = texto + "++++++++++++++++++++++++++++++++++++++++++++++++++++++ " + myEnter;
		texto = texto + "++++ GOVERNADOR ++++++++++++++++++++++++++++++++++++++ " + myEnter;

		for (Iterator iterator = candidatosGovernador.iterator(); iterator.hasNext();) {
			texto = texto + iterator.next().toString() + myEnter;
		}
		texto = texto + "++++++++++++++++++++++++++++++++++++++++++++++++++++++ " + myEnter;
		texto = texto + "++++ PRESIDENTE ++++++++++++++++++++++++++++++++++++++ " + myEnter;

		for (Iterator iterator = candidatosPresidente.iterator(); iterator.hasNext();) {
			texto = texto + iterator.next().toString() + myEnter;
		}
		texto = texto + "++++++++++++++++++++++++++++++++++++++++++++++++++++++ " + myEnter;

		return texto;
	}

}

class Eleitor {
	private String nome = new String();
	private long cpf = 0L;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public long getCpf() {
		return cpf;
	}

	public void setCpf(long cpf) {

		this.cpf = cpf;
	}

	public Eleitor(String nome, long cpf) {
		super();
		this.nome = nome;
		this.cpf = cpf;

	}

	public boolean validaCPF() {

		long numCPF = this.cpf;

		int[] valCPF = new int[11];
		long num = 10000000000L;
		long resto;
		int j;
		int i;
		int dig1 = 0;
		int dig2 = 0;

		// guarda numCPF digitado em um vetor
		for (j = 0; j < 11; j++) {
			resto = (numCPF % num);
			i = toIntExact(numCPF / num);
			num = num / 10;
			numCPF = resto;
			valCPF[j] = i;
		}

		// soma primeiro digito
		i = 10;
		for (j = 0; j < 9; j++) {
			dig1 = dig1 + (valCPF[j] * i);
			i = i - 1;
		}

		// soma segundo digito
		i = 11;
		for (j = 0; j < 10; j++) {
			dig2 = dig2 + (valCPF[j] * i);
			i = i - 1;
		}

		// calcula digito verificador 1 e 2
		dig1 = dig1 * 10 % 11;
		dig2 = dig2 * 10 % 11;
		// caso resultado seja 10 mudar para 0
		if (dig1 == 10)
			dig1 = 0;
		if (dig2 == 10)
			dig2 = 0;
		// verifica se o digito verificador 1 e 2 obtidos são iguais aos digitos
		// verificadores do numCPF digitado
		if (dig1 == valCPF[9] && (dig2 == valCPF[10]))
			return true;
		else
			return false;

	}

	@Override
	public String toString() {
		return String.format("%35s %11s", this.nome, this.cpf);
	}

}

class Eleicao {
	private static String myEnter = "\r\n";
	private Candidatos urna = new Candidatos();
	private ArrayList<Eleitor> eleitores = new ArrayList<>();

	private BrancoNulo brancosPref = new BrancoNulo();
	private BrancoNulo brancosGov = new BrancoNulo();
	private BrancoNulo brancosPres = new BrancoNulo();

	public Candidatos getUrna() {
		return urna;
	}

	public void setUrna(Candidatos urna) {
		this.urna = urna;
	}

	public ArrayList<Eleitor> getEleitores() {
		return eleitores;
	}

	public void setEleitores(ArrayList<Eleitor> eleitores) {
		this.eleitores = eleitores;
	}

	private String listagemVotos() {

		return "";
	}

	public Eleicao() {
		super();
		this.urna = null;
		this.eleitores = null;
	}

	public Eleicao(Candidatos urna, ArrayList<Eleitor> eleitores) {
		super();
		this.urna = urna;
		this.eleitores = eleitores;
	}

	public boolean isVoid() {
		if (Objects.isNull(this.urna) | Objects.isNull(this.eleitores)) {
			return true;
		}
		if (this.urna.isVoid() | this.eleitores.size() <= 0) {
			return true;
		}
		return false;
	}

	public String totalizaBrancoNulo() {

		String texto = "+++++++++++++++ TOTAL DE BRANCOS E NULOS +++++++++++++++ " + myEnter;

		texto = texto + brancosPref.totalizacao(Cargo.Prefeito);
		texto = texto + brancosGov.totalizacao(Cargo.Governador);
		texto = texto + brancosPres.totalizacao(Cargo.Presidente);

		return texto;

	}

	private void efetuaVotoCandidato(ArrayList<Candidato> candidatos, BrancoNulo branconulo, Cargo cargo) {

		System.out.println(Candidatos.listagemCandidatos(candidatos, cargo.descricao));
		Scanner voto = new Scanner(System.in);

		boolean votoOk = false;
		do {
			System.out.println(String.format("Escolha o %s:", cargo.descricao));

			try {

				int numero = voto.nextInt();
				if (numero == -1) {
					branconulo.addBrancos();
					votoOk = true;
					System.out.println("Voto Computado:");
				} else if (numero == -2) {
					branconulo.addNulos();
					votoOk = true;
					System.out.println("Voto Computado:");
				} else if (Candidatos.votar(candidatos, numero)) {
					votoOk = true;
					System.out.println("Voto Computado com sucesso.");
				} else {
					System.out.println("Numero Invalido, tente de novo");
				}
			} catch (Exception e) {
				System.out.println("Numero Invalido, tente de novo");
			}
		} while (!votoOk);

	}

	public void realizarEleicao() {

		System.out.println("+++++++ MENU - SIMULADOR DO SISTEMA DE VOTAÇÃO +++++++");
		System.out.println("+++++++ INICIO DO PROCESSO DE VOTACAO +++++++");
		System.out.println("");
		int numEleitores = eleitores.size();
		System.out.println("Existem " + String.valueOf(numEleitores) + " eleitores cadastrados.");

		for (int i = 0; i < eleitores.size(); i++) {

			System.out.print("\n\n\n"); // adiciona 3 linhas em branco

			if (urna.getCandidatosPrefeito().size() > 0) {
				this.efetuaVotoCandidato(urna.getCandidatosPrefeito(), brancosPref,  Cargo.Prefeito);
			}

			if (urna.getCandidatosGovernador().size() > 0) {
				this.efetuaVotoCandidato(urna.getCandidatosGovernador(), brancosGov,Cargo.Governador);

			}

			if (urna.getCandidatosPresidente().size() > 0) {
				this.efetuaVotoCandidato(urna.getCandidatosPresidente(), brancosPres, Cargo.Presidente);
			}

			numEleitores--;
			System.out.println("Restam " + String.valueOf(numEleitores) + " eleitores para votar.");

		}

	}

}

// Enumerador de cargos, ajuda a controlar os cargos
enum Cargo {
	Prefeito("Prefeito"), Governador("Governador"), Presidente("Presidente");

	public final String descricao;

	private Cargo(String descricao) {
		this.descricao = descricao;
	}
}