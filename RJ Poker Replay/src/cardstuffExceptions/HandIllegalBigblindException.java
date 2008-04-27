/**
 * Fehlerklasse wenn einer Hand eine nicht erlaubte Bigblind zugewiesen wird.
 */
package cardstuffExceptions;


/**
 * @author Ralf Joswig
 */

public class HandIllegalBigblindException extends HandException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 6296811528162982163L;

	/**
	 * Standartkonstuktor
	 */
	public HandIllegalBigblindException() {
		super("Der Bigblind ist in dieser Höhe nicht zulässig.");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandIllegalBigblindException(String text) {
		super(text);
	}
}
