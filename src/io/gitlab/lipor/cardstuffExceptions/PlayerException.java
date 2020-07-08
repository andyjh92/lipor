/**
 * Fehlerklasse fuer allg. Spielerfehler
 */
package io.gitlab.lipor.cardstuffExceptions;

/**
 * @author Ralf Joswig
 */
public class PlayerException extends Exception {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = -2911908813865483046L;

	/**
	 * Standartkonstuktor
	 */
	public PlayerException() {
		super(Messages.PlayerException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public PlayerException(String text) {
		super(text);
	}
}
