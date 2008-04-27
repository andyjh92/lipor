/**
 * Fehlerklasse wenn ein Spieler fehlt / nicht übergeben wurde
 */
package cardstuffExceptions;

/**
 * @author Ralf Joswig
 */
public class PlayerMissingException extends PlayerException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = -1645670306616640802L;	

	/**
	 * Standartkonstuktor
	 */
	public PlayerMissingException() {
		super("Fehlender Spieler.");
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public PlayerMissingException(String text) {
		super(text);
	}
}
