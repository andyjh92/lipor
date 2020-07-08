/**
 * Fehlerklasse wenn einer Hand eine nicht erlaubte Smallblind zugewiesen wird.
 */
package io.gitlab.lipor.cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class HandIllegalSmallblindException extends HandException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = -6954006828164164867L;

	/**
	 * Standartkonstuktor
	 */
	public HandIllegalSmallblindException() {
		super(Messages.HandIllegalSmallblindException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandIllegalSmallblindException(String text) {
		super(text);
	}
}
