/**
 * Fehlerklasse fuer fehlerhafte Chipsbeträge
 */

package net.sourceforge.rjPokerReplay.cardstuffExceptions;

public class PlayerIllegalChipsException extends PlayerException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 2719905990115641517L;

	/**
	 * Standartkonstuktor
	 */
	public PlayerIllegalChipsException() {
		super(Messages.PlayerIllegalChipsException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public PlayerIllegalChipsException(String text) {
		super(text);
	}
}
