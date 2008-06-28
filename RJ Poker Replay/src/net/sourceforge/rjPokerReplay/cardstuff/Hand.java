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

package net.sourceforge.rjPokerReplay.cardstuff;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;


import net.sourceforge.rjPokerReplay.cardstuffExceptions.ActionIllegalActionException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalBigblindException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalButtonPositionException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalCardCountException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalGametypeException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalPokerroomException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalPotsizeException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalRakesizeException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalSeatCountException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalSmallblindException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalTabletypeException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandMissingNameException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandToMuchPlayersException;

/**
 * @author Ralf Joswig
 * 
 * Klasse zur Darstellung und Handhabung einer Hand beim Pokern
 * Erweitert die Klasse Table um die Aktionen die an einem Tisch stattfinden 
 * koennen.
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
	
	// Array mit den original Zeilen der Handhistorie
	private ArrayList<String> FileLines = new ArrayList<String>();
	
	
	// vermutlicher Nikname des Spielers
	private String nikname;

	/***************************************************************************
	 * Konstruktoren
	 **************************************************************************/
	/**
	 * Standartkonstuktor ohne Parameter
	 */
	public Hand() {

	}

	/**
	 * Vollstaendiger Konstruktor
	 * 
	 * @param titel
	 *            Bezeichnung der Hand
	 * @param buttonPos
	 *            Position des Buttons
	 * @param countSeats
	 *            Anzahl der Plaetze am Tisch
	 * @param players
	 *            Array mit den Spielern
	 * @param board
	 *            Array mit den Boardkarten
	 * @param pokerroom
	 *            Der Pokerraum, definiert ueber Konstanten
	 * @param game
	 *            Nummer des Spiels
	 * @param smallblind
	 *            Hoehe des Smallblinds
	 * @param bigblind
	 *            Hoehe des Bigblinds
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
		setPotExtern(pot);
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
		String ret = ""; //$NON-NLS-1$
		
		// Die einzelnen Aktionen anlisten
		Iterator<Action> iter = actionlist.iterator();
		while(iter.hasNext()) {
			Action next = iter.next();
			ret = ret + next.toString() +  "\n";  //$NON-NLS-1$
		}

		
		return ret;
	}
	
	/**
	 * Fuegt eine Aktion am Ende der Aktionsliste an
	 * 
	 * @param action Die anzufuegende Aktion
	 * @throws ActionIllegalActionException
	 */
	public void addAction(Action action) throws ActionIllegalActionException {
		// ist die Aktion vollstaendig?
		if (!action.isActionValid()) {
			throw new ActionIllegalActionException(Messages.Hand_0);
		}
		
		// alles passt, dann Aktion in der Liste anhaengen
		actionlist.add(action);
	}
	
	/**
	 * Gibt eine bestimmte Akton aus der Hand zurueck
	 * 
	 * @param actionNumber Die Nummer Aktion in der Hand
	 * @return Die gewuenschte Aktion
	 * @throws ActionIllegalActionException 
	 */
	public Action getAction(int actionNumber) throws ActionIllegalActionException {
		// pruefen ob es eine Aktion unter der Nummer gibt
		if (actionNumber < 0 || actionNumber >= actionlist.size()) {
			throw new ActionIllegalActionException(Messages.Hand_1);
		}
		
		// Aktion zurueckgeeben
		return actionlist.get(actionNumber);
	}
	
	/**
	 * Gibt die aktuelle Anzahl an Aktionen der Hand zurueck
	 * 
	 * @return Die Anzahl der Aktionen
	 */
	public int getCountOfActions() {
		return actionlist.size();
	}
	
	/**
	 * Gibt eine ArrayListe mit den gespeicherten original Zeilen der Handhistorie zurueck
	 * 
	 * @return Eine String-Array-Liste mit den Zeilen der Handhistorie
	 */
	public ArrayList<String> getFileLines() {
		return FileLines;
	}
	
	/**
	 * Haengt eine Zeile an die Liste der Zeilen aus der Handhistorie an
	 * 
	 * @param line Die anzuhaengende Zeile
	 */
	public void addLineToFileLines(String line) {
		FileLines.add(line);
	}
	
	/**
	 * Setzt den Spielernamen
	 * 
	 * @param nikname
	 */
	public void setNikname(String nikname) {
		this.nikname = nikname;
	}
	
	/**
	 * Gibt den anhand des Handverlaufes ermittelten Spielernamen zurueck
	 * 
	 * @return Der ermittelte Spielername
	 */
	public String getNikname(){
		return nikname;
	}
}
