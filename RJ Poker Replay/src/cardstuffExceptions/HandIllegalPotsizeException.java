/**
 * Fehlerklasse wenn einer Hand eine nicht erlaubter Pot zugewiesen wird.
 */
package cardstuffExceptions;


/**
 * @author Ralf Joswig
 */

public class HandIllegalPotsizeException extends HandException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 4539865126415577741L;

	/**
	 * Standartkonstuktor
	 */
	public HandIllegalPotsizeException() {
		super("Ein Pot in dieser Höhe ist nicht zulässig.");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandIllegalPotsizeException(String text) {
		super(text);
	}
}
