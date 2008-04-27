/**
 * Fehlerklasse für allg. Fehler im Zusammenhang mit einer Hand
 */
package cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class HandPositionNotEmptyException extends HandException{

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = -3614105137611505352L;

	/**
	 * Standartkonstuktor
	 */
	public HandPositionNotEmptyException() {
		super("Allg. Fehler im Zusammenhang mit einer Hand.");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandPositionNotEmptyException(String text) {
		super(text);
	}
}
