/**
 * Fehlerklasse für einen nicht erlaubten Wert einer Handaktion
 */
package cardstuffExceptions;

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
		super("Der Wert ist nicht zulässig.");
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
