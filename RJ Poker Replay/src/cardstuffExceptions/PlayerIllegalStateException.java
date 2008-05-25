/**
 * Fehlerklasse wenn ein unbekannter Spielerstatus verwendet wird
 */
package cardstuffExceptions;

import language.Messages;

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
		super(Messages.PlayerIllegalStateException_0);
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
