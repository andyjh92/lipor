/**
 * Fehlerklasse fuer allg. Fehler im Zusammenhang mit einer Handhistorie
 */
package cardstuffExceptions;

import language.Messages;

/**
 * @author Ralf Joswig
 */

public class HandhistoryException extends Exception{

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 9002323542633638417L;

	/**
	 * Standartkonstuktor
	 */
	public HandhistoryException() {
		super(Messages.HandhistoryException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public HandhistoryException(String text) {
		super(text);
	}
}
