/**
 * Fehlerklasse den nicht erlaubten Zugriff auf einen leeren Sitzplatz
 */
package net.sourceforge.rjPokerReplay.cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class HandPositionEmptyException extends HandException{

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 4279862038604916365L;

	/**
	 * Standartkonstuktor
	 */
	public HandPositionEmptyException() {
		super(Messages.HandPositionEmptyException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandPositionEmptyException(String text) {
		super(text);
	}
}
