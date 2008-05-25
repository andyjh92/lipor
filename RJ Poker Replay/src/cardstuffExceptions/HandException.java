/**
 * Fehlerklasse fuer allg. Fehler im Zusammenhang mit einer Hand
 */
package cardstuffExceptions;

import language.Messages;

/**
 * @author Ralf Joswig
 */

public class HandException extends Exception{

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = -3614105137611505352L;

	/**
	 * Standartkonstuktor
	 */
	public HandException() {
		super(Messages.HandException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandException(String text) {
		super(text);
	}
}
