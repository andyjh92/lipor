/**
 * Fehlerklasse für fehlenden Spielernamen
 */
package cardstuffExceptions;

public class PlayerMissingNameException extends PlayerException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 5873474286725492901L;

	/**
	 * Standartkonstuktor
	 */
	public PlayerMissingNameException() {
		super("Spielername fehlt.");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public PlayerMissingNameException(String text) {
		super(text);
	}
}
