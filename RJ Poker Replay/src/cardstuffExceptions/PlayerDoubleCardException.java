/**
 * Fehlerklasse wenn einem Spieler zwei gleiche Karten zugewiesen werden
 */
package cardstuffExceptions;

import language.Messages;

/**
 * @author Ralf Joswig
 */
public class PlayerDoubleCardException extends PlayerException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 4819098832027705887L;

	/**
	 * Standartkonstuktor
	 */
	public PlayerDoubleCardException() {
		super(Messages.PlayerDoubleCardException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public PlayerDoubleCardException(String text) {
		super(text);
	}
}
