package bankomat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

	static Scanner unos = new Scanner(System.in);

	static int brojacRacuni = -1;

	static int racun1;
	static int racun2;
	static double novacPrebacivanje;

	static ArrayList<String> imenaRacuna = new ArrayList<String>();
	static ArrayList<Double> novacNaRacunima = new ArrayList<Double>();

	static ArrayList<String> racuniExternal = new ArrayList<String>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			File file = new File("RacuniBankomat.txt");
			if (!file.exists())
				file.createNewFile();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		try {
			FileReader fr = new FileReader("RacuniBankomat.txt");
			BufferedReader ReadFileBuffer = new BufferedReader(fr);

			for (String line; (line = ReadFileBuffer.readLine()) != null;) {
				racuniExternal.add(line);
			}

			ReadFileBuffer.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		imenaRacunaLoadFile();
	}

	public static void meni() {
		Scanner unos = new Scanner(System.in);
		int opcija = 0;
		do {
			System.out.println("Unesite broj zeljene opcije:");
			System.out.println("1 - Kreirajte racun;");
			System.out.println("2 - Prebacite novac sa jednog na drugi racun;");
			System.out.println("3 - Informacije o racunima;");
			try {
				opcija = unos.nextInt();
			} catch (Exception e) {
				unos.nextLine();
				System.out.println("Pogresan unos");
				continue;
			}
			break;
		} while (true);

		switch (opcija) {
		case 1:
			kreiranjeRacuna();
			break;
		case 2:
			prebacivanjeNovca();
			break;
		case 3:
			informacije();
			break;
		}
	}

	public static void kreiranjeRacuna() {
		System.out.println("Upisite svoje ime");
		String ime = unos.next();
		if (racuniExternal.size() != 0)
			brojacRacuni = racuniExternal.size();
		else
			brojacRacuni++;
		Double novac = 0.00;
		System.out.println("Unesite broj novca koji zelite depozirati");
		boolean tryCatch1 = false;
		while (!tryCatch1) {
			try {
				novac = unos.nextDouble();
				if (novac < 0.00)
					throw new IznosNaRacunuNegativanException();
				tryCatch1 = true;
			} catch (IznosNaRacunuNegativanException e) {
				System.out.println(e.getMessage());
			} catch (InputMismatchException e) {
				unos.nextLine();
				System.out.println("Molimo unesite broj, ne slova");
			}
		}

		Racun noviRacun = new Racun(brojacRacuni, ime, novac);
		imenaRacuna.add(ime);
		novacNaRacunima.add(novac);

		System.out.println("Napravili ste racun! broj vaseg racuna je " + brojacRacuni
				+ ". Stanje na vasem racunu iznosi " + novac);
		try {
			saveFile1();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void prebacivanjeNovca() {

		do {
			System.out.println(
					"Unesite broj vaseg racuna, broj racuna na koji prebacujete novac, te iznos koji prebacujete.");
			try {
				racun1 = unos.nextInt();
				racun2 = unos.nextInt();
				novacPrebacivanje = unos.nextDouble();
				if (novacPrebacivanje > novacNaRacunima.get(racun1))
					throw new IznosNaRacunuNedovoljanException();
				if (racun1 > novacNaRacunima.size() || racun2 > novacNaRacunima.size())
					throw new NepostojeciRacunException();
			} catch (InputMismatchException e) {
				unos.nextLine();
				System.out.println("Molimo unesite broj, ne slova");
				continue;
			} catch (IznosNaRacunuNedovoljanException e) {
				unos.nextLine();
				System.out.println(e.getMessage());
				continue;
			} catch (NepostojeciRacunException e) {
				unos.nextLine();
				System.out.println(e.getMessage());
				continue;
			}
			break;
		} while (true);

		novacNaRacunima.set(racun1, novacNaRacunima.get(racun1) - novacPrebacivanje);
		novacNaRacunima.set(racun2, novacNaRacunima.get(racun2) + novacPrebacivanje);
		System.out.println("Uspjesno ste prebacili novac");
		saveFile2();
	}

	public static void informacije() {
		int brojRacuna;
		do {
			System.out.println("Unesite broj racuna koji zelite pogledati");
			try {
				brojRacuna = unos.nextInt();

				System.out.println("Broj Racuna: " + brojRacuna + " ime: " + imenaRacuna.get(brojRacuna)
						+ ", stanje na racunu: " + novacNaRacunima.get(brojRacuna));
			} catch (Exception e) {
				unos.nextLine();
				System.out.println("Pogresan unos");
				continue;
			}
			break;
		} while (true);

		meni();
	}

	public static void saveFile1() throws IOException {

		racuniExternal.clear();

		FileReader fr = new FileReader("RacuniBankomat.txt");
		BufferedReader ReadFileBuffer = new BufferedReader(fr);

		for (String line; (line = ReadFileBuffer.readLine()) != null;) {
			racuniExternal.add(line);
		}

		ReadFileBuffer.close();

		FileWriter fw = new FileWriter("RacuniBankomat.txt");
		BufferedWriter WriteFileBuffer = new BufferedWriter(fw);

		for (int i = 0; i < racuniExternal.size(); i++) {
			WriteFileBuffer.write(racuniExternal.get(i));
			WriteFileBuffer.newLine();
		}

		WriteFileBuffer
				.write(brojacRacuni + " " + imenaRacuna.get(brojacRacuni) + " " + novacNaRacunima.get(brojacRacuni));
		WriteFileBuffer.newLine();
		WriteFileBuffer.close();

		meni();
	}

	public static void saveFile2() {                                                                //Ovo je metoda koju trebas pogledati
		try {
			FileReader fr = new FileReader("RacuniBankomat.txt");
			BufferedReader ReadFileBuffer = new BufferedReader(fr);

			String keyword;
			int brojac = 0;
			char racun11 = (char) racun1;                                                          //Ovo su brojevi racuna sa kog prebacujes i kome prebacujes novac
			char racun22 = (char) racun2;

			System.out.println("Krecemo sa radom!");

			for (String line; (line = ReadFileBuffer.readLine()) != null;) {                      //Ideja je da ako procita broj isti kao i racun11, da promijeni iznos novca, ali nece nikako ni da prepozna brojeve
				keyword = "";
				if (line.charAt(0) == racun11) {                                                  //Na kraju, kad sve prode, na ovoj liniji koda izbacuje String IndexOutOfRangeException
					for (int i = 2; i < line.length(); i++) {
						if (line.charAt(i) == '0' || line.charAt(i) == '1' || line.charAt(i) == '2'
								|| line.charAt(i) == '3' || line.charAt(i) == '4' || line.charAt(i) == '5'
								|| line.charAt(i) == '6' || line.charAt(i) == '7' || line.charAt(i) == '8'
								|| line.charAt(i) == '9') {
							if (brojac == 0) {
								keyword += novacNaRacunima.get(racun1) - novacPrebacivanje;
								brojac++;
								System.out.println("Racun 1 uspjesno izmijenjen!");
							}
						} else
							keyword += line.charAt(i);
					}
					racuniExternal.add(keyword);
				}

				else if (line.charAt(0) == racun22) {
					for (int i = 2; i < line.length(); i++) {
						if (line.charAt(i) == '0' || line.charAt(i) == '1' || line.charAt(i) == '2'
								|| line.charAt(i) == '3' || line.charAt(i) == '4' || line.charAt(i) == '5'
								|| line.charAt(i) == '6' || line.charAt(i) == '7' || line.charAt(i) == '8'
								|| line.charAt(i) == '9') {
							if (brojac == 0) {
								keyword += " " + novacPrebacivanje;
								brojac++;
								System.out.println("Racun 2 uspjesno izmijenjen!");
							}
						} else
							keyword += line.charAt(i);
					}
					racuniExternal.add(keyword);
				}

				else {
					racuniExternal.add(line);
					System.out.println("Nismo prepoznali ni jedan racun koji treba izmijeniti, ovaj dio samo prepisujemo i nastavljamo dalje..");
				}
			}

			FileWriter fw = new FileWriter("RacuniBankomat.txt");
			BufferedWriter WriteFileBuffer = new BufferedWriter(fw);


			for (int i = 0; i < racuniExternal.size(); i++) {
				WriteFileBuffer.write(racuniExternal.get(i));
				WriteFileBuffer.newLine();
			}

			WriteFileBuffer.newLine();
			WriteFileBuffer.close();

			meni();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public static void imenaRacunaLoadFile() {
		try {
			FileReader fr = new FileReader("RacuniBankomat.txt");
			BufferedReader ReadFileBuffer = new BufferedReader(fr);

			String keyword;

			for (String line; (line = ReadFileBuffer.readLine()) != null;) {
				keyword = "";
				for (int i = 2; i < line.length(); i++) {

					if (line.charAt(i) != ' ')
						if (line.charAt(i) != '0')
							if (line.charAt(i) != '1')
								if (line.charAt(i) != '2')
									if (line.charAt(i) != '3')
										if (line.charAt(i) != '4')
											if (line.charAt(i) != '5')
												if (line.charAt(i) != '6')
													if (line.charAt(i) != '7')
														if (line.charAt(i) != '8')
															if (line.charAt(i) != '9')
																keyword += line.charAt(i);

				}
				imenaRacuna.add(keyword);
			}

			ReadFileBuffer.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		novacNaRacunimaLoadFile();
	}

	public static void novacNaRacunimaLoadFile() {
		try {
			FileReader fr = new FileReader("RacuniBankomat.txt");
			BufferedReader ReadFileBuffer = new BufferedReader(fr);

			String keyword;
			double keynum;

			for (String line; (line = ReadFileBuffer.readLine()) != null;) {
				keyword = "";
				for (int i = 4; i < line.length(); i++) {
					if (line.charAt(i) == '0' || line.charAt(i) == '1' || line.charAt(i) == '2' || line.charAt(i) == '3'
							|| line.charAt(i) == '4' || line.charAt(i) == '5' || line.charAt(i) == '6'
							|| line.charAt(i) == '7' || line.charAt(i) == '8' || line.charAt(i) == '9'
							|| line.charAt(i) == '.')
						keyword += line.charAt(i);
				}
				keynum = Double.valueOf(keyword);
				novacNaRacunima.add(keynum);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		meni();
	}

}
