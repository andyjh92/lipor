/**
 * Fehlerklasse wenn einer Hand eine nicht erlaubter Pokerraum zugewiesen wird.
 */
package cardstuffExceptions;


/**
 * @author Ralf Joswig
 */

public class HandIllegalPokerroomException extends HandException {


	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 3767598859571280155L;


	/**
	 * Standartkonstuktor
	 */
	public HandIllegalPokerroomException() {
		super("Der Pokerraum ist nicht definiert, bitte Konstanten verwenden.");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandIllegalPokerroomException(String text) {
		super(text);
	}
}
