/**
 * Copyright (C) 2008  Ralf Joswig
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */

package cardstuff;

import cardstuffExceptions.ActionException;
import cardstuffExceptions.ActionIllegalActionException;
import cardstuffExceptions.ActionMissingParameterException;

/**
 * @author ralf
 *
 * Klasse die eine Aktion während eines Pokerspielsdarstellt
 */

public class Action {

	/***************************************************************************
	 * Konstanten
	 **************************************************************************/
	// Call
	public static final int CALL = 1;
	
	// Fold
	public static final int FOLD = 2;
	
	// Bet
	public static final int BET = 3;
	
	// Raise
	public static final int RAISE = 4;
	
	// Smallblind
	public static final int SMALLBLIND = 5;
	
	// Bigblind
	public static final int BIGBLIND = 6;
	
	// Verteilung einer Karte
	public static final int DEAL = 7;
	
	// Spieler streicht den Gewinn ein
	public static final int COLLECT = 8;
	
	// Der Flop wird gegeben
	public static final int FLOP = 9;
	
	// Der Turn wird gegeben
	public static final int TURN = 10;
	
	// Der River wird gegeben
	public static final int RIVER = 11;
	
	// Check
	public static final int CHECK = 12;
	
	// Hand wird gezeigt
	public static final int SHOW = 13;
	
	// Spieler passt die Hand
	public static final int MUCK = 14;
	
	// Spieler macht einen Kommentar
	public static final int SAY = 15;
	
	// Spieler zahl Small- und Bigblind
	public static final int SMALL_AND_BIGBLIND = 16;
	
	// Showdown
	public static final int SHOWDOWN = 17;
	
	// Ante
	public static final int ANTE = 18;
	
	/***************************************************************************
	 * Instanz Eigenschaften
	 **************************************************************************/
	// welcher Spieler
	private Player player = null;
	
	// welche Aktion
	private int action = 0;
	
	// Wert
	private Object value = null;
	
	/***************************************************************************
	 * Konstruktoren
	 **************************************************************************/
	
	/**
	 * vollständiger Konstuktor
	 *  
	 * @param player Spieler der die Aktion ausführt
	 * @param action Welche Aktion, siehe Konstanten
	 * @param value  ggf. Wert mit der die Aktion durchgeführt wird
	 * @throws ActionIllegalActionException 
	 * @throws ActionMissingParameterException 
	 * @throws ActionException 
	 */
	public Action(int action, Player player, Object value) throws ActionIllegalActionException, ActionMissingParameterException {
		checkDependency(action, player, value);
		setAction(action);
		setPlayer(player);
		setValue(value);
	}

	/***************************************************************************
	 * Getter
	 **************************************************************************/
	/**
	 * Gibt den Spieler der Aktion zurück
	 * 
	 * @return Der Spieler, der die Aktion ausführt
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gibt die Aktion zurück
	 * 
	 * @return Die Atkion, die der Spieler ausführt
	 */
	public int getAction() {
		return action;
	}

	/**
	 * Gibt den Wert der Aktion zurück
	 * 
	 * @return Der Wert zur ausgeführten Aktion
	 */
	public Object getValue() {
		return value;
	}
		
	/***************************************************************************
	 * Setter
	 **************************************************************************/
	/**
	 * Setzt den Spieler der die Aktion ausführt
	 * 
	 * @param Der Spieler zur aktion
	 */
	public void setPlayer(Player player) {		
		this.player = player;
	}
	
	/**
	 * Setzt die Aktion die ausgeführt wird
	 * 
	 * @param action Die auszuführende Aktion
	 * @throws ActionIllegalActionException 
	 */
	private void setAction(int action) throws ActionIllegalActionException {
		// Fehler wenn keine gültige Aktion 
		if (action != CALL &&
		      action != FOLD &&
		      action != BET &&
		      action != RAISE &&
		      action != SMALLBLIND &&
		      action != BIGBLIND &&
		      action != DEAL &&
		      action != COLLECT &&
		      action != TURN &&
		      action != FLOP &&
		      action != RIVER &&
		      action != CHECK &&
		      action != SHOW &&
		      action != MUCK &&
		      action != SAY &&
		      action != SMALL_AND_BIGBLIND &&
		      action != SHOWDOWN &&
		      action != ANTE) {
			throw new ActionIllegalActionException();
		}

		this.action = action;
	}

	/**
	 * Setzt den Wert mit der die Aktion ausgeführt wird
	 * 
	 * @param value Der Wert der Aktion
	 */
	private void setValue(Object value) {
		this.value = value;
	}
	
	/***************************************************************************
	 * Methoden
	 **************************************************************************/
	/**
	 * Prüft ob die einzelnen Angaben der Aktion zusammen passen
	 * 
	 * @return True wenn die Angaben zur Aktion schlüssig sind.
	 */
	public boolean isActionValid() {
		return true;
	}
	
	/**
	 * Wandelt eine Aktion in einen Text um
	 * 
	 * @return Die Aktion als Text
	 */
	public String toString() {
		String ret = "";
		
		// Text in Abhänigkeit für die verschiedenen Aktionen
		switch (action) {
		case SMALLBLIND:
			ret = player.getName() + ": posts small blind " + String.valueOf(value); 
			break;
		case BIGBLIND:
			ret = player.getName() + ": posts big blind " + String.valueOf(value); 
			break;
		case ANTE:
			ret = player.getName() + ": posts ante " + String.valueOf(value); 
			break;			
		case SMALL_AND_BIGBLIND:
			ret = player.getName() + " small & big blinds " + String.valueOf(value);
			break;
		case FOLD:
			ret = player.getName() + ": folds"; 
			break;
		case BET:
			ret = player.getName() + ": bets " + String.valueOf(value); 
			break;
		case CALL:
			ret = player.getName() + ": calls " + String.valueOf(value); 
			break;			
		case RAISE:
			ret = player.getName() + ": raise to " + String.valueOf(value); 
			break;
		case DEAL:
			ret = player.getName() + ": get Card " + value.toString(); 
			break;
		case COLLECT:
			ret = player.getName() + ": collected " + String.valueOf(value) + " from pot"; 
			break;
		case FLOP:
			ret = "*** FLOP ***"; 
			break;			
		case TURN:
			ret = "*** TURN ***"; 
			break;
		case RIVER:
			ret = "*** RIVER ***"; 
			break;
		case CHECK:
			ret = player.getName() + ": checks";
			break;
		case SHOW:
			ret = player.getName() + ": shows hand";
			break;
		case MUCK:
			ret = player.getName() + ": mucks hand";
			break;
		case SAY:
			ret = player.getName() + " said: " + value;
			break;
		case SHOWDOWN:
			ret = "*** SHOWDOWN ***"; 
			break;
		default:
			ret = "Unbekannte Aktion!";
			break;
		}
		return ret;
	}
	
	/**
	 * Prüft ob bei bestimmten Aktiion / Spieler / Wert Kombinationen die nötigen Werte 
	 * gefültl sind
	 * 
	 * @param action Die Aktion
	 * @param player Der Spieler
	 * @param value Der Wert der Aktion
	 * @throws ActionMissingParameterException 
	 */
	private void checkDependency(int action, Player player, Object value) throws ActionMissingParameterException {
		switch (action) {
		case CALL:
		case SMALL_AND_BIGBLIND:
		case BET:
		case RAISE:
		case SMALLBLIND:
		case BIGBLIND:
		case COLLECT:
		case ANTE:
			if (player == null || value == null || !(value instanceof Double)) {
				throw new ActionMissingParameterException();
			}
			break;
		case FOLD:
		case CHECK:
		case MUCK:
			if (player == null) {
				throw new ActionMissingParameterException("Spieler fehlt bei Aktion.");
			}
			break;
		case DEAL:
		case SHOW:
			if (player == null || value == null || !(value instanceof Card)) {
				throw new ActionMissingParameterException();
			}
			break;
		case FLOP:
		case TURN:
		case RIVER:
		case SHOWDOWN:
			// Keine weiteren Angaben nötig
			break;
		case SAY:
			if (player == null || value == null || !(value instanceof String)) {
				throw new ActionMissingParameterException();
			}
			break;	
		}
	}
}
