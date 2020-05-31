/**
 * Fehlerklasse wenn einer Hand eine nicht erlaubter Pokerraum zugewiesen wird.
 */
package net.sourceforge.rjPokerReplay.cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class HandIllegalPokerroomException extends HandException {


	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 3767598859571280155L;


	/**
	 * Standartkonstuktor
	 */
	public HandIllegalPokerroomException() {
		super(Messages.HandIllegalPokerroomException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandIllegalPokerroomException(String text) {
		super(text);
	}
}
