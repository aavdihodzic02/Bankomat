package bankomat;

public class IznosNaRacunuNegativanException extends Exception {
	public IznosNaRacunuNegativanException() {
		super("Pogresan unos. Negativan broj nije dozvoljen");
	}
}
