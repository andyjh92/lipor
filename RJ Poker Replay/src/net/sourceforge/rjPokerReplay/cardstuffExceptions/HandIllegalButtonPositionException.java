/**
 * Fehlerklasse wenn einer Hand eine nicht erlaubte Button-Position zugewiesen wird.
 */
package net.sourceforge.rjPokerReplay.cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class HandIllegalButtonPositionException extends HandException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = -3145346222241194184L;

	/**
	 * Standartkonstuktor
	 */
	public HandIllegalButtonPositionException() {
		super(Messages.HandIllegalButtonPositionException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandIllegalButtonPositionException(String text) {
		super(text);
	}
}
