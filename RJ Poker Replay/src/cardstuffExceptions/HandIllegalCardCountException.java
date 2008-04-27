/**
 * Fehlerklasse für eine nicht erlaubte Anzahl an Karten in einer Hand
 */
package cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class HandIllegalCardCountException extends HandException{

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = -4698319185281962886L;

	/**
	 * Standartkonstuktor
	 */
	public HandIllegalCardCountException() {
		super("Anzahl der Karten ist nicht erlaubt.");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandIllegalCardCountException(String text) {
		super(text);
	}
}
