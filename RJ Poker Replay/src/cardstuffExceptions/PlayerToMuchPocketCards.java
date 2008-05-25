/**
 * Fehlerklasse wenn einem Spieler zuviele Pocketkarten zugeteilt werden
 */
package cardstuffExceptions;

import language.Messages;

public class PlayerToMuchPocketCards extends PlayerException {

	/**
	 * ID der Klasse
	 */
	private static final long serialVersionUID = -1367144567742224479L;

	/**
	 * Standartkonstuktor
	 */
	public PlayerToMuchPocketCards() {
		super(Messages.PlayerToMuchPocketCards_0);
	}

	/**
	 * Konstruktor mit einem Fehlertext
	 *
	 * @param text
	 *            Der Fehlertext
	 */
	public PlayerToMuchPocketCards(String text) {
		super(text);
	}
}
