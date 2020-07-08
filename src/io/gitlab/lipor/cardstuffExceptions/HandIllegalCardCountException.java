/**
 * Fehlerklasse fuer eine nicht erlaubte Anzahl an Karten in einer Hand
 */
package io.gitlab.lipor.cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class HandIllegalCardCountException extends HandException{

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = -4698319185281962886L;

	/**
	 * Standartkonstuktor
	 */
	public HandIllegalCardCountException() {
		super(Messages.HandIllegalCardCountException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandIllegalCardCountException(String text) {
		super(text);
	}
}
