/**
 * Fehlerklasse wenn ein Spieler fehlt / nicht uebergeben wurde
 */
package io.gitlab.lipor.cardstuffExceptions;

/**
 * @author Ralf Joswig
 */
public class PlayerMissingException extends PlayerException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = -1645670306616640802L;	

	/**
	 * Standartkonstuktor
	 */
	public PlayerMissingException() {
		super(Messages.PlayerMissingException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public PlayerMissingException(String text) {
		super(text);
	}
}
