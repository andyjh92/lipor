/**
 * Fehlerklasse für eine nicht erlaubte Anzahl an Sitzplätzen
 */
package cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class HandIllegalSeatCountException extends HandException{
	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = -5727164773602998074L;

	/**
	 * Standartkonstuktor
	 */
	public HandIllegalSeatCountException() {
		super("Die Anzahl der Plätze muss zwischen 2 und 10 liegen.");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandIllegalSeatCountException(String text) {
		super(text);
	}
}
