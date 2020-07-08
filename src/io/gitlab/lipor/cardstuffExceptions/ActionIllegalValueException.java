/**
 * Fehlerklasse fuer einen nicht erlaubten Wert einer Handaktion
 */
package io.gitlab.lipor.cardstuffExceptions;

/**
 * @author Ralf Joswig
 */

public class ActionIllegalValueException extends ActionException{

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 4296927313930945177L;

	/**
	 * Standartkonstuktor
	 */
	public ActionIllegalValueException() {
		super(Messages.ActionIllegalValueException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public ActionIllegalValueException(String text) {
		super(text);
	}
}
