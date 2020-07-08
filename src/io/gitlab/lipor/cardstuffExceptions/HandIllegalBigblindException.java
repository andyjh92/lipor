/**
 * Fehlerklasse wenn einer Hand eine nicht erlaubte Bigblind zugewiesen wird.
 */
package io.gitlab.lipor.cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class HandIllegalBigblindException extends HandException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 6296811528162982163L;

	/**
	 * Standartkonstuktor
	 */
	public HandIllegalBigblindException() {
		super(Messages.HandIllegalBigblindException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandIllegalBigblindException(String text) {
		super(text);
	}
}
