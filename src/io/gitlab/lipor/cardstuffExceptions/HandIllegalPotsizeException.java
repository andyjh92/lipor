/**
 * Fehlerklasse wenn einer Hand eine nicht erlaubter Pot zugewiesen wird.
 */
package io.gitlab.lipor.cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class HandIllegalPotsizeException extends HandException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 4539865126415577741L;

	/**
	 * Standartkonstuktor
	 */
	public HandIllegalPotsizeException() {
		super(Messages.HandIllegalPotsizeException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandIllegalPotsizeException(String text) {
		super(text);
	}
}
