/**
 * Fehlerklasse wenn einer Hand eine nicht erlaubte Smallblind zugewiesen wird.
 */
package cardstuffExceptions;


/**
 * @author Ralf Joswig
 */

public class HandIllegalSmallblindException extends HandException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = -6954006828164164867L;

	/**
	 * Standartkonstuktor
	 */
	public HandIllegalSmallblindException() {
		super("Der Smallblind ist in dieser Höhe nicht zulässig.");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandIllegalSmallblindException(String text) {
		super(text);
	}
}
