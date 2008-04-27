/**
 * Fehlerklasse für Fehlende Parameter bei Handaktionen
 */
package cardstuffExceptions;

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
		super("Fehlende Paramter beim Anlegen einer Aktion.");
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
