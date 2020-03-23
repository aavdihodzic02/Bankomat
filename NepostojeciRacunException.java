package bankomat;

public class NepostojeciRacunException extends Exception {
	public NepostojeciRacunException () {
		super("Racun pod brojem koji ste unijeli ne postoji. Pokusajte ponovo");
	}
}
