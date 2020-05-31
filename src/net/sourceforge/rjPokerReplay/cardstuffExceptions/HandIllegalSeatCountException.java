/**
 * Fehlerklasse fuer eine nicht erlaubte Anzahl an Sitzplätzen
 */
package net.sourceforge.rjPokerReplay.cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class HandIllegalSeatCountException extends HandException{
	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = -5727164773602998074L;

	/**
	 * Standartkonstuktor
	 */
	public HandIllegalSeatCountException() {
		super(Messages.HandIllegalSeatCountException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandIllegalSeatCountException(String text) {
		super(text);
	}
}
