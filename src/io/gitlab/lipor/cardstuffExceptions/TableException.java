/**
 * Fehlerklasse fuer allg. Tischfehler
 */

package io.gitlab.lipor.cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class TableException extends Exception{

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 8859523527737662057L;
	
	/**
	 * Standartkonstuktor
	 */
	public TableException() {
		super(Messages.TableException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public TableException(String text) {
		super(text);
	}

}
