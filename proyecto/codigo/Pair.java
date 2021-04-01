/**
 * Clase para representar parejas.
 *
 * @author Julian Gomez Benitez, Juan Pablo Rincon Usma
 * @version 1
 */
public class Pair<F, S> {
	public final F first; // Primer elemento de la pareja.
	public final S second;// Segundo elemento de la pareja.

	/**
	 * Constructor de pares.
	 *
	 * @param first primer objeto del par.
	 * @param second segundo objeto del par.
	 */
	public Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}

}