/**
 * Fehlerklasse wenn ein unbekannter Spielerstatus verwendet wird
 */
package cardstuffExceptions;

/**
 * @author Ralf Joswig
 */
public class PlayerIllegalStateException extends PlayerException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 2792035752814629777L;

	/**
	 * Standartkonstuktor
	 */
	public PlayerIllegalStateException() {
		super("Der Status ist nicht definiert.");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public PlayerIllegalStateException(String text) {
		super(text);
	}
}
