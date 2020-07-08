/**
 * Fehlerklasse fuer fehlenden Spielnamen
 */
package io.gitlab.lipor.cardstuffExceptions;

public class HandMissingNameException extends HandException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 1897116506571878009L;

	/**
	 * Standartkonstuktor
	 */
	public HandMissingNameException() {
		super(Messages.HandMissingNameException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandMissingNameException(String text) {
		super(text);
	}
}
