/**
 * Fehlerklasse wenn einer Hand mehr Spieler als Plätze zugewiesen werden
 */
package net.sourceforge.rjPokerReplay.cardstuffExceptions;

/**
 * @author Ralf Joswig
 */


public class HandToMuchPlayersException extends HandException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 6498280908258765470L;

	/**
	 * Standartkonstuktor
	 */
	public HandToMuchPlayersException() {
		super(Messages.HandToMuchPlayersException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandToMuchPlayersException(String text) {
		super(text);
	}
	
}
