/**
 * Fehlerklasse wenn einer Hand ein nicht erlaubter Rake zugewiesen wird.
 */
package net.sourceforge.rjPokerReplay.cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class HandIllegalRakesizeException extends HandException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 2262947399346655231L;

	/**
	 * Standartkonstuktor
	 */
	public HandIllegalRakesizeException() {
		super(Messages.HandIllegalRakesizeException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandIllegalRakesizeException(String text) {
		super(text);
	}
}
