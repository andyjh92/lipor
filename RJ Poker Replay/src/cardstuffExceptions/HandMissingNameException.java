/**
 * Fehlerklasse für fehlenden Spielnamen
 */
package cardstuffExceptions;

public class HandMissingNameException extends HandException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 1897116506571878009L;

	/**
	 * Standartkonstuktor
	 */
	public HandMissingNameException() {
		super("Spielename fehlt.");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandMissingNameException(String text) {
		super(text);
	}
}
