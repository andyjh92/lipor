/**
 * Fehlerklasse für allg. Spielerfehler
 */
package cardstuffExceptions;

/**
 * @author Ralf Joswig
 */
public class PlayerException extends Exception {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = -2911908813865483046L;

	/**
	 * Standartkonstuktor
	 */
	public PlayerException() {
		super("Allg. Fehler bei Spielerbeitung");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public PlayerException(String text) {
		super(text);
	}
}
