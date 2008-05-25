/**
 * Fehlerklasse fuer fehlenden Spielernamen
 */
package cardstuffExceptions;

import language.Messages;

public class PlayerMissingNameException extends PlayerException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 5873474286725492901L;

	/**
	 * Standartkonstuktor
	 */
	public PlayerMissingNameException() {
		super(Messages.PlayerMissingNameException_0);
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
