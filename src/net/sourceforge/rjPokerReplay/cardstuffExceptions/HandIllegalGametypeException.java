/**
 * Fehlerklasse wenn einer Hand eine nicht erlaubte Spieltyp zugewiesen wird.
 */
package net.sourceforge.rjPokerReplay.cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class HandIllegalGametypeException extends HandException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = -1814701904800561692L;

	/**
	 * Standartkonstuktor
	 */
	public HandIllegalGametypeException() {
		super(Messages.HandIllegalGametypeException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandIllegalGametypeException(String text) {
		super(text);
	}
}
