/**
 * Fehlerklasse für allg. Fehler im Zusammenhang mit einer Handhistorie
 */
package cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class HandhistoryException extends Exception{

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 9002323542633638417L;

	/**
	 * Standartkonstuktor
	 */
	public HandhistoryException() {
		super("Allg. Fehler im Zusammenhang mit einer Handhistorie.");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandhistoryException(String text) {
		super(text);
	}
}
