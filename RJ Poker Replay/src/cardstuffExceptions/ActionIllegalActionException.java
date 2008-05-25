/**
 * Fehlerklasse fuer eine nicht definierte Handaktion
 */
package cardstuffExceptions;

import language.Messages;

/**
 * @author Ralf Joswig
 */

public class ActionIllegalActionException extends ActionException{

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 76669686147151142L;

	/**
	 * Standartkonstuktor
	 */
	public ActionIllegalActionException() {
		super(Messages.ActionIllegalActionException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public ActionIllegalActionException(String text) {
		super(text);
	}
}
