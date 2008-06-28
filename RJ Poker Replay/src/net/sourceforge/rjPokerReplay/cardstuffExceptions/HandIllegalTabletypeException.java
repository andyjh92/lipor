/**
 * Fehlerklasse wenn einer Hand eine nicht erlaubte Tischtyp zugewiesen wird.
 */
package net.sourceforge.rjPokerReplay.cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class HandIllegalTabletypeException extends HandException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 1131852270685083397L;

	/**
	 * Standartkonstuktor
	 */
	public HandIllegalTabletypeException() {
		super(Messages.HandIllegalTabletypeException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandIllegalTabletypeException(String text) {
		super(text);
	}
}
