/**
 * Fehlerklasse für allg. Fehler im Zusammenhang mit einer Hand
 */
package cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class HandException extends Exception{

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = -3614105137611505352L;

	/**
	 * Standartkonstuktor
	 */
	public HandException() {
		super("Allg. Fehler im Zusammenhang mit einer Hand.");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandException(String text) {
		super(text);
	}
}
