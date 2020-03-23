package bankomat;

public class IznosNaRacunuNedovoljanException extends Exception {
	public IznosNaRacunuNedovoljanException() {
		super("Nedovoljan iznos na racunu. Nemate dovoljno novca za transfer");
	}
}
