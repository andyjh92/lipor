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
 *
 */

package cardstuff;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import cardstuffExceptions.ActionIllegalActionException;
import cardstuffExceptions.HandIllegalBigblindException;
import cardstuffExceptions.HandIllegalButtonPositionException;
import cardstuffExceptions.HandIllegalCardCountException;
import cardstuffExceptions.HandIllegalGametypeException;
import cardstuffExceptions.HandIllegalPokerroomException;
import cardstuffExceptions.HandIllegalPotsizeException;
import cardstuffExceptions.HandIllegalRakesizeException;
import cardstuffExceptions.HandIllegalSeatCountException;
import cardstuffExceptions.HandIllegalSmallblindException;
import cardstuffExceptions.HandIllegalTabletypeException;
import cardstuffExceptions.HandMissingNameException;
import cardstuffExceptions.HandToMuchPlayersException;


/**
 * @author Ralf Joswig
 * 
 * Klasse zur Darstellung und Handhabung einer Hand beim Pokern
 * Erweitert die Klasse Table um die Aktionen die an einem Tisch stattfinden 
 * können.
 */

public class Hand extends Table {

	/***************************************************************************
	 * Konstanten
	 **************************************************************************/

	/***************************************************************************
	 * Instanz Eigenschaften
	 **************************************************************************/
	// Array mit den einzelnen Aktionen
	private ArrayList<Action> actionlist = new ArrayList<Action>();

	/***************************************************************************
	 * Konstruktoren
	 **************************************************************************/
	/**
	 * Standartkonstuktor ohne Parameter
	 */
	public Hand() {

	}

	/**
	 * Vollständiger Konstruktor
	 * 
	 * @param titel
	 *            Bezeichnung der Hand
	 * @param buttonPos
	 *            Position des Buttons
	 * @param countSeats
	 *            Anzahl der Plätze am Tisch
	 * @param players
	 *            Array mit den Spielern
	 * @param board
	 *            Array mit den Boardkarten
	 * @param pokerroom
	 *            Der Pokerraum, definiert über Konstanten
	 * @param game
	 *            Nummer des Spiels
	 * @param smallblind
	 *            Höhe des Smallblinds
	 * @param bigblind
	 *            Höhe des Bigblinds
	 * @param date
	 *            Datum an dem das Spiel gespielt wurde
	 * @param gametype
	 *            Spielvariante
	 * @param tabletype
	 *            Tischvariante
	 * @throws HandIllegalButtonPositionException
	 * @throws HandIllegalSeatCountException
	 * @throws HandToMuchPlayersException
	 * @throws HandIllegalCardCountException
	 * @throws HandIllegalBigblindException 
	 * @throws HandIllegalSmallblindException 
	 * @throws HandIllegalPokerroomException 
	 * @throws HandMissingNameException 
	 * @throws HandIllegalGametypeException 
	 * @throws HandIllegalTabletypeException 
	 * @throws HandIllegalPotsizeException 
	 * @throws HandIllegalRakesizeException 
	 */
	public Hand(String titel, int buttonPos, int countSeats, Player players[],
			Card board[], int pokerroom, String game, double smallblind,
			double bigblind, Calendar date, int gametype, int tabletype, double pot, double rake)
			throws HandIllegalButtonPositionException,
			HandIllegalSeatCountException, HandToMuchPlayersException,
			HandIllegalCardCountException, HandIllegalBigblindException, HandIllegalSmallblindException, HandIllegalPokerroomException, HandMissingNameException, HandIllegalGametypeException, HandIllegalTabletypeException, HandIllegalPotsizeException, HandIllegalRakesizeException {
		setTitel(titel);
		setCountSeats(countSeats);
		setPlayers(players);
		setButtonPos(buttonPos);
		setBoard(board);
		setBigblind(bigblind);
		setSmallblind(smallblind);
		setPokerroom(pokerroom);
		setGame(game);
		setDate(date);
		setGametype(gametype);
		setTabletype(tabletype);
		setPot(pot);
		setRake(rake);
		setCountOfPlayers();
	}
	
	/***************************************************************************
	 * Methoden
	 **************************************************************************/
	/**
	 * Gibt die Informationen zur Hand als String aus
	 * 
	 * @return Die Handinformationen
	 */
	public String toString() {
		String ret = "";
		
		// Die einzelnen Aktionen anlisten
		Iterator<Action> iter = actionlist.iterator();
		while(iter.hasNext()) {
			Action next = iter.next();
			ret = ret + next.toString() +  "\n";
		}

		
		return ret;
	}
	
	/**
	 * Fügt eine Aktion am Ende der Aktionsliste an
	 * 
	 * @param action Die anzufügende Aktion
	 * @throws ActionIllegalActionException
	 */
	public void addAction(Action action) throws ActionIllegalActionException {
		// ist die Aktion vollständig?
		if (!action.isActionValid()) {
			throw new ActionIllegalActionException("Die Aktion ist ungültig / unvollständig.");
		}
		
		// alles passt, dann Aktion in der Liste anhängen
		actionlist.add(action);
	}
	
	/**
	 * Gibt eine bestimmte Akton aus der Hand zurück
	 * 
	 * @param actionNumber Die Nummer Aktion in der Hand
	 * @return Die gewünschte Aktion
	 * @throws ActionIllegalActionException 
	 */
	public Action getAction(int actionNumber) throws ActionIllegalActionException {
		// prüfen ob es eine Aktion unter der Nummer gibt
		if (actionNumber < 0 || actionNumber >= actionlist.size()) {
			throw new ActionIllegalActionException("Aktion mit dieser Nummer nicht vorhanden");
		}
		
		// Aktion zurückgeeben
		return actionlist.get(actionNumber);
	}
	
	/**
	 * Gibt die aktuelle Anzahl an Aktionen der Hand zurück
	 * 
	 * @return Die Anzahl der Aktionen
	 */
	public int getCountOfActions() {
		return actionlist.size();
	}
}
