/**
 * Fehlerklasse fuer Fehlende Parameter bei Handaktionen
 */
package cardstuffExceptions;

import language.Messages;

/**
 * @author Ralf Joswig
 */

public class ActionMissingParameterException extends ActionException{

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = 7840761554191815514L;

	/**
	 * Standartkonstuktor
	 */
	public ActionMissingParameterException() {
		super(Messages.ActionMissingParameterException_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public ActionMissingParameterException(String text) {
		super(text);
	}
}
