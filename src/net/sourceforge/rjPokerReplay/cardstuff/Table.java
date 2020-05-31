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

import java.util.Calendar;


import net.sourceforge.rjPokerReplay.cardstuffExceptions.CardException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalBigblindException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalButtonPositionException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalCardCountException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalGametypeException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalPokerroomException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalPotsizeException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalRakesizeException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalSeatCountException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalSeatException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalSmallblindException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandIllegalTabletypeException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandMissingNameException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandPositionEmptyException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandPositionNotEmptyException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.HandToMuchPlayersException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.PlayerException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.PlayerIllegalChipsException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.PlayerMissingException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.PlayerMissingNameException;
import net.sourceforge.rjPokerReplay.cardstuffExceptions.TableException;
import net.sourceforge.rjPokerReplay.util.ErrorHandler;



/**
 * @author Ralf Joswig
 *
 * Klasse zur Darstellung und Handhabung eines Pokertisches
 * 
 */

public class Table {

	/***************************************************************************
	 * Konstanten
	 **************************************************************************/
	// Pokerraum PokerStars
	public static final int POKERSTARS = 1;
	
	// Spieltyp Hold'em No Limit
	public static final int NO_LIMIT = 1;
	
	// Spieltyp Holdem Potl Limit
	public static final int POT_LIMIT = 2;
	
	// Spieltyp Holdem Limit
	public static final int LIMIT = 3;
	
	// Tischtyp Spielgeld
	public static final int PLAYMONEY = 1;
	
	// Tischtyp Turnier
	public static final int TOURNAMENT = 2;
	
	// Tischtyp Echtgeld
	public static final int REALMONEY = 3;
	

	/***************************************************************************
	 * Instanz Eigenschaften
	 **************************************************************************/
	
	// Anzahl Spieler am Tisch
	private int countOfPlayers = 0;
	
	// Position des Buttons
	private int buttonposition = 0;
	
	// der Pot, ggf. mit Sidepot
	private double pot[] = new double[10];
	
	// ein von aussen vorgegebener Pot
	private double potExtern = 0.0;
	
	// Zeiger fuer Main- und Sidepot
	private int potNumber = 0;
	
	// die Spieler am Tisch
	private Player players[] = new Player[10];
	
	// Bezeichner des Tisches
	private String titel = ""; //$NON-NLS-1$

	// Anzahl an Sitzen
	private int countSeats = 0;

	// Boardkarten
	private Card board[] = new Card[5];

	// Name des Pokerraums
	// Moegliche Werte sind als Konstanten definiert
	private int pokerroom = 0;

	// Nummer des Spiels
	// Muss zusammen mit dem Pokerraum eine eindeutige Zuordnung ergeben
	private String game = "";  //$NON-NLS-1$

	// Hoehe des Smallblinds
	private double smallblind = 0.0;

	// Hoehe des Bigblinds
	private double bigblind = 0.0;

	// Spieldatum
	private Calendar date = null;
	
	// Art des Spiels
	// Die verschiedenen Spielarten sind ueber Konstanten definert
	private int gametype = 0;

	// Art des Tisches
	// Die verschiedenen Spielarten sind ueber Konstanten definert
	private int tabletype = 0;
	
	// Rake der Hand
	private double rake = 0.0;
	
	// Einsaetze in der aktuellen Setzrunde
	private double bet[] = new double[10];
	
	// Die Hoehe des Ante
	private double ante = 0.0;
	
	/***************************************************************************
	 * Konstruktoren 
	 **************************************************************************/
	/**
	 * Standartkonstuktor, erzeugt einen leeren Tisch
	 * 
	 */
	public Table() {
		super();
	}
	
	/**
	 * Ein Tisch an dem direkt einige Spieler Platz nehmen
	 * 
	 * @param player Die Spieler fuer den Tisch
	 */
	public Table(Player players[]) {
		// wurden ueberhaupt Spieler uebergeben?
		if (players != null) {
			// ja, dann diese uebernehmen
			this.players = players;
			
			// und Anzahl der Spieler ermitteln
			int i = 0;
			while (players[i] != null) {
				countOfPlayers++;
			}
		}
	}

	
	/***************************************************************************
	 * Getter
	 **************************************************************************/
	/**
	 * Gibt die Anzahl an Spielern zurueck
	 * 
	 * @return Die Anzahl an Spielern am Tisch
	 */
	public int getCountOfPlayer() {
		return countOfPlayers;
	}

	/**
	 * Gbit die Nummer des Spielers beim dem der Button liegt zurueck
	 * 
	 * @return Die Position des Buttons
	 */
	public int getButtonposition() {
		return buttonposition + 1;
	}

	/**
	 * Gibt die aktuelle Pot-Groesse zurueck
	 * 
	 * @return Die aktuelle Pot-Groesse
	 */
	public double getPot() {
		double potTotal = 0.0;
		
		// die Main- und Sidepots der vorherigen Setzrunden addieren
		for (int i = 0; i < pot.length; i++) {
			potTotal = potTotal + pot[i];
		}
		
		// die Einsaetze der aktuellen Setzrunde hinzuaddieren
		for (int i = 0; i < bet.length; i++) {
			potTotal = potTotal + bet[i];
		}
		
		// Gesamtpot zurueckmelden
		return potTotal;
	}

	/**
	 * Gibt ein Array mit den Spielern am Tisch zurueck
	 * 
	 * @return Ein Array mit den Spielern
	 */
	public Player[] getPlayers() {
		return players;
	}

	/**
	 * Gibt die Bezeichnung des Tisches zurueck
	 * 
	 * @return Die Bezeichnung
	 */
	public String getTitel() {
		return titel;
	}
	

	/**
	 * Gibt die Anzahl der Sitzplaetze zurueck
	 * 
	 * @return Die Anzahl der Sitzplaetze
	 */
	public int getCountSeats() {
		return countSeats;
	}
	
	/**
	 * Gibt das Board zurueck
	 * 
	 * @return Die Boardkarten
	 */
	public Card[] getBoard() {
		return board;
	}
	
	/**
	 * Gibt den Pokerraum zurueck
	 * 
	 * @return Konstante des Pokerraums
	 */
	public int getPokerroom() {
		return pokerroom;
	}

	/**
	 * Gibt die Nummer des Spiels zurueck
	 * 
	 * @return Die Nummer des Spiels
	 */
	public String getGame() {
		return game;
	}

	/**
	 * Gibt die Hoehe des Smallblinds zurueck
	 * 
	 * @return Der Smallblind
	 */
	public double getSmallblind() {
		return smallblind;
	}

	/**
	 * Gibt die Hoehe des Ante zurueck
	 * 
	 * @return Der Ante
	 */
	public double getAnte() {
		return ante;
	}
	
	/**
	 * Gibt die Hoehe des Bigblinds zurueck
	 * 
	 * @return Der Bigblind
	 */
	public double getBigblind() {
		return bigblind;
	}

	/**
	 * Gibt das Spieldatum zurueck
	 * 
	 * @return Das Datum an dem gespielt wurde
	 */
	public Calendar getDate() {
		return date;
	}
	
	/**
	 * Gibt den Spieltyp zurueck.
	 * Genaueres sie Konstanten
	 * 
	 * @return Der Spieltyp
	 */
	public int getGametype() {
		return gametype;
	}
	
	/**
	 * Gibt den Tischtyp zurueck
	 * Bedeutung siehe entsprechende Konstanten
	 * 
	 * @return Der Tischtyp
	 */
	public int getTabletype() {
		return tabletype;
	}
	
	/**
	 * Gibt den Rake der Hand zurueck
	 * 
	 * @return Der Rake
	 */
	public double getRake() {
		return rake;
	}
	
	/***************************************************************************
	 * Setter
	 **************************************************************************/
	/**
	 * Setzt die Bezeichnung der Hand
	 * 
	 * @param titel
	 *            Die Bezeichnung der Hand
	 */
	public void setTitel(String titel) {
		this.titel = titel;
	}

	/**
	 * Setzt die Position des Buttons
	 * 
	 * @param buttonPos
	 *            Die Position an der sich der Button befindet
	 * @throws HandIllegalButtonPositionException
	 */
	public void setButtonPos(int buttonPos)
			throws HandIllegalButtonPositionException {
		// gibt des den Sitzplatz ueberhaupt am Tisch
		if (buttonPos < 1 || buttonPos > countSeats) {
			throw new HandIllegalButtonPositionException(
					Messages.Table_0);
		}

		// sitzt hier ueberhaupt ein Spieler
//		if (players[buttonPos - 1] == null) {
//			throw new HandIllegalButtonPositionException(
//					"An dieser Position sitzt (noch) kein Spieler.");
//		}
		// dann den Button an diesen Platz setzen
		this.buttonposition = buttonPos;
	}

	/**
	 * Legt die Anzahl der Sitzplaetze am Tisch fest
	 * 
	 * @param countSeats
	 *            the countSeats to set
	 * @throws HandIllegalSeatCountException
	 */
	public void setCountSeats(int countSeats)
			throws HandIllegalSeatCountException {
		// Anzahl der Plaetze darf min. 2 und hoechsten 10 betragen
		if (countSeats < 2 || countSeats > 10) {
			throw new HandIllegalSeatCountException();
		}

		// passt, HandToMuchPlayersExceptiondann merken
		this.countSeats = countSeats;
	}

	/**
	 * Setzt die in einem Array uebergebenen Spieler. Die Spieler werden dabei
	 * entsprechend ihrer Position im Array an den Tisch gesetzt (player[0]
	 * kommt an Platz 1, player[1] an Platz 2 usw. Soll ein Platz leer bleiben,
	 * muss an dieser Stelle im Array NULL uebergeben werden.
	 * 
	 * @param players
	 *            Ein Array mit den Spielern
	 * @return Die Anzahl der gesetzten Spieler
	 * @throws HandToMuchPlayersException
	 */
	public int setPlayers(Player[] players) throws HandToMuchPlayersException {
		// hoechsten besetzen Platz ermitteln
		int highSeat = 0;
		for (int i = players.length - 1; i >= 0; i--) {
			// ist dieser Platz besetzt ?
			if (players[i] != null) {
				// ja, dann Position merken
				highSeat = i + 1;
				
				// und fuer Abbruch der Schleife sorgen
				break;
			}
		}
		// sind am Tisch genug Plaetze vorhanden
		if (highSeat > countSeats) {
			throw new HandToMuchPlayersException();
		}

		// Plaetze reichen, dann Spieler an Tisch setzen
		this.players = players;

		// Anzahl der Spieler ermitteln
		setCountOfPlayers();

		// und zurueck geben
		return countOfPlayers;
	}

	/**
	 * Setzt die Boardkarten. Dabei muessen 3 (bis Flop), 4 (bis Turn) oder 5
	 * (bis River) gesetzt werdn
	 * 
	 * @param board
	 *            Die Katen die ins Board sollen
	 * @return Anzahl der eingefuegten Karten
	 * @throws HandIllegalCardCountException
	 */
	public int setBoard(Card[] board) throws HandIllegalCardCountException {
		int cardCount = 0;
		int boardPos = 0;

		// Anzahl der uebergebenen Karten ermitteln
		for (int i = 0; i < board.length; i++) {
			if (board[i] != null) {
				cardCount++;
			}
		}

		// wurde die geforderte Anzahl an Karten uebergeben
		if (cardCount != 0 && cardCount != 3 && cardCount != 4 && cardCount != 5) {
			throw new HandIllegalCardCountException(
					Messages.Table_1);
		}

		// Ja, dann Karten hinzufuegen. Dabei nur die tatsaechlich vorhandenen
		// Karten uebernehmen
		for (int i = 0; i < board.length; i++) {
			if (board[i] != null) {
				this.board[boardPos] = board[i];
				boardPos++;
			}
		}

		// und Anzahl der uebernommen Karten rueckmelden
		return cardCount;
	}

	/**
	 * Setzt den Pokerraum
	 * Dabei sollten die in der Klasse definierten Konstanten verwendet werden.
	 * 
	 * @param pokerroom Der Pokerraum zur Hand
	 * @throws HandIllegalPokerroomException 
	 */
	public void setPokerroom(int pokerroom) throws HandIllegalPokerroomException {
		// pruefen ob der Pokerraum bekannt ist
		if (pokerroom != POKERSTARS) {
			throw new HandIllegalPokerroomException();
		}
		
		// ist bekannt, dann speichern
		this.pokerroom = pokerroom;
	}

	/**
	 * Setzt die Nummer des Spiels
	 * 
	 * @param game Die Nummer des Spiels
	 * @throws HandMissingNameException 
	 */
	public void setGame(String game) throws HandMissingNameException {
		// pruefen ob ueberhaupt was uebergeben wurde
		if (game == null || "".equals(game)) { //$NON-NLS-1$
			throw new HandMissingNameException();
		}
		
		// Spielbezeichnung merken
		this.game = game;
	}

	/**
	 * Setzt die Hoehe des Smallblinds
	 * 
	 * @param smallblind Die Hoehe des Smallblinds in der Hand
	 * @throws HandIllegalSmallblindException 
	 */
	public void setSmallblind(double smallblind) throws HandIllegalSmallblindException {
		// Der Smallblind darf nicht 0 sein
		if (smallblind == 0.0) {
			throw new HandIllegalSmallblindException(Messages.Table_2);
		}
		
		// wenn der Bigblind bereits gesetzt ist, muss der Smallblind dem
		// halben Bigblind entsprechen
		if (bigblind != 0.0 && smallblind != (bigblind / 2.0)) {
			throw new HandIllegalSmallblindException(Messages.Table_3);
		}
		
		// alles passt, dann merken
		this.smallblind = smallblind;
	}

	/**
	 * Setzt die Hoehe des Ante
	 * 
	 * @param ante Die Hoehe des Ante
	 * @throws IllegalArgumentException
	 */
	public void setAnte(double ante) throws IllegalArgumentException {
		// Der Ante darf nicht kleiner 0
		if (ante < 0.0) {
			throw new IllegalArgumentException(Messages.Table_4);
		}
				
		// alles passt, dann merken
		this.ante = ante;
	}
	
	/**
	 * Setzt die Hoehe des Bigblinds
	 * 
	 * @param bigblind Die Hoehe des Bigblinds in der Hand
	 * @throws HandIllegalBigblindException 
	 */
	public void setBigblind(double bigblind) throws HandIllegalBigblindException {
		// Der Bigblind darf nicht 0 sein
		if (bigblind == 0.0) {
			throw new HandIllegalBigblindException(Messages.Table_5);
		}
		
		// wenn der Smalllind bereits gesetzt ist, muss der Bigblind dem
		// doppelten Smallblind entsprechen
		if (smallblind != 0.0 && bigblind != (smallblind * 2.0)) {
			throw new HandIllegalBigblindException(Messages.Table_6);
		}
		
		// alles passt, dann merken
		this.bigblind = bigblind;
	}

	/**
	 * Setzt das Datum an dem das Spiel ausgetragen wurde
	 * 
	 * @param date Das Datum des Spiels
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * Setzt den Spieltyp.
	 * Welche Typen erlaubt sind, sieh entsprechende Konstanten
	 * 
	 * @param gametype Der Spieltyp
	 * @throws HandIllegalGametypeException 
	 */
	public void setGametype(int gametype) throws HandIllegalGametypeException {
		// ist der Spieltyp bekannt
		if (gametype != NO_LIMIT &&
			  gametype != POT_LIMIT &&
			  gametype != LIMIT) {
			throw new HandIllegalGametypeException();
		}
		
		// bekannten Spieltyp merken
		this.gametype = gametype;
	}
	
	/**
	 * Setzt den Tischtyp
	 * Welche Typen erlaubt sind, sieh entsprechende Konstanten
	 * 
	 * @param tabletype Der Tischtyp
	 * @throws HandIllegalTabletypeException 
	 */
	public void setTabletype(int tabletype) throws HandIllegalTabletypeException {
		// ist der Tischtyp bekannt
		if (tabletype != PLAYMONEY  &&
			  tabletype != TOURNAMENT &&
			  tabletype != REALMONEY) {
			throw new HandIllegalTabletypeException();
		}
		this.tabletype = tabletype;
	}


	/**
	 * Setzt den Pot der am Ende der Hand erreicht war
	 * 
	 * @param pot Die zu setzende Pothoehe
	 * @throws HandIllegalPotsizeException 
	 */
	public void setPot(double pot[]) throws HandIllegalPotsizeException {
		// Der Pot muss groesser Null sein
		if (pot[0] < 0.0) {
			throw new HandIllegalPotsizeException();
		}
		
		for (int i = 0; i < this.pot.length && i < pot.length; i++) {
			this.pot[i] = pot[i];
		}
	}

	
	/**
	 * Setzt den Rake der Hand
	 * 
	 * @param rake Der Rake der Hand
	 * @throws HandIllegalRakesizeException 
	 */
	public void setRake(double rake) throws HandIllegalRakesizeException {
		// Der Rake muss groesser gleich Null sein
		if (rake < 0.0) {
			throw new HandIllegalRakesizeException();
		}
		this.rake = rake;
	}
	
	
	/**
	 * Setzt den Pot von aussen mit einem festen Wert
	 * 
	 * @param value Der Betrag der dem Pot hinzugefuegt werden soll
	 */
	public void setPotExtern(double value) throws IllegalArgumentException {
		// pruefen ob der Betrag groesser Null ist
		if (value < 0) {
			// nein, ist aber nicht erlaubt
			throw new IllegalArgumentException(Messages.Table_7);
		}
		
		// den von aussen vorgegebenen Pot setzen
		potExtern = value;
	}
	
	/***************************************************************************
	 * Methoden
	 **************************************************************************/
	/**
	 * Vordert den Big- und den Smalblind von den entsprechenden Spielern
	 * 
	 * @param smallBlind Wert des Smallblinds
	 * @return Die kassiereten Blinds
	 */
	public double collectBlinds(double smallBlind) {
		double blinds = 0.0;
		int posBlind = 0;
		
		// Position des Smallblinds bestimmen
		posBlind = nextPlayerPos(buttonposition);
		
		// Smallblind kassieren
		bet[posBlind] = players[posBlind].pay(smallBlind);
		blinds = bet[posBlind];
		
		// Position des Bigblinds bestimmen
		posBlind = nextPlayerPos(posBlind);
		
		// den Bigblind kassieren
		bet[posBlind] = players[posBlind].pay(smallBlind * 2);
		blinds = blinds + bet[posBlind];
		
		// und die kassierten Blinds zurueckmelden
		return blinds;
	}

	/**
	 * Ermittelt die Position des naechsten Spielers
	 * 
	 * @param startPos Position von der aus der naechste Spieler
	 *                 ermittelt werden soll
	 * @return Die Position des naechsten Spielers
	 */
	private int nextPlayerPos(int startPos) {
		int nextPos = startPos;
		
		// So lange die Position um eins erhoehen
		// bis ein Platz an dem ein Spieler sitzt
		// gefunden wird.
		do {
			nextPos++;
			
			// Keine Plaetze zulassen die groesser sind
			// als die maximale Anzahl an Sitzplaetzen
			if (nextPos >= players.length) {
				// dann beginnen wir von vorne
				nextPos = 0;
			}
		} while (players[nextPos] == null);
		
		// gefundene Position zurueckmelden
		return nextPos;
	}

	/**
	 * Bewegt den Button einen Spieler weiter
	 */
	public void moveButton() {
		// Button eins weiter geben
		buttonposition = nextPlayerPos(buttonposition);
	}
	
	/**
	 * Ermittelt die Anzahl der vorhandenen Spieler
	 */
	protected void setCountOfPlayers() {
		// zur Sicherheit die Anzahl der Spieler loeschen
		countOfPlayers = 0;

		// Alle moeglichen Positionen durchlaufen
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null) {
				// wenn an der Position ein Spieler sitzt die Anzahl erhoehen
				countOfPlayers++;
			}
		}
	}
	
	/**
	 * Setzt einen Spieler an der vorgegebenen Position an den Tisch
	 * 
	 * @param player
	 *            Der Spieler
	 * @param position
	 *            Die Position am Tisch
	 * @throws PlayerMissingException
	 * @throws HandPositionNotEmptyException
	 * @throws HandIllegalSeatException
	 */
	public void addPlayer(Player player, int position)
			throws PlayerMissingException, HandPositionNotEmptyException,
			HandIllegalSeatException {
		// wurde ein Spieler uebergeben?
		if (player == null) {
			throw new PlayerMissingException(Messages.Table_8);
		}

		// ist die Position am Tisch bereits besetzt
		if (players[position - 1] != null) {
			throw new HandPositionNotEmptyException();
		}

		// ist die Position zulaessig
		if (position < 1 || position > countSeats) {
			throw new HandIllegalSeatException();
		}

		// Alles scheint zu passen, dann Spieler an Tisch setzen
		players[position - 1] = player;

		// und die neue Anzahl an Spielern ermitteln
		setCountOfPlayers();
	}

	/**
	 * Entfernt einen Spieler vom Tisch anhand der Sitznummer
	 * 
	 * @param position
	 *            Position von der ein Spieler entfert werden soll
	 * @throws HandIllegalSeatException
	 * @throws HandPositionEmptyException
	 */
	public void removePlayer(int position) throws HandIllegalSeatException,
			HandPositionEmptyException {
		// ist die Position zulaessig
		if (position < 1 || position > countSeats) {
			throw new HandIllegalSeatException();
		}

		// sitz jemand an der Position
		if (players[position] == null) {
			throw new HandPositionEmptyException();
		}

		// dann den Spieler entfernen
		players[position - 1] = null;
	}

	/**
	 * Entfernt einen Spieler mit dem vorgegebenem Namen vom Tisch
	 * 
	 * @param name
	 *            Der Name des Spieler der vom Tisch soll
	 * @throws PlayerMissingNameException
	 * @throws PlayerMissingException
	 */
	public void removePlayer(String name) throws PlayerMissingNameException,
			PlayerMissingException {
		int position = 0;

		// wurde ein Name uebergeben
		if (name == null || "".equals(name)) { //$NON-NLS-1$
			throw new PlayerMissingNameException();
		}

		// sitzt ein Spieler mit diesem Namen am Tisch
		for (int i = 0; i < countSeats; i++) {
			if (name.equalsIgnoreCase(players[i].getName())) {
				// ja, dann Sitzposition merken
				position = i;
			}
		}
		if (position == 0) {
			// kein Spieler mit diesem Namen am Tisch
			throw new PlayerMissingException(
					Messages.Table_9);
		}

		// den gefundenen Spieler vom Tisch entfernen
		players[position] = null;
	}

	/**
	 * Gibt die Informationen zur Hand als String aus
	 * 
	 * @return Die Handinformationen
	 */
	public String toString() {
		String ret = ""; //$NON-NLS-1$

		// Pokerraum
		switch (pokerroom) {
		case POKERSTARS:
			ret = "Pokerstars"; //$NON-NLS-1$
			break;
		default:
			ret = Messages.Table_10;
			break;
		}
		
		// Spielnummer
		ret = ret  + " " + game + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
		
		// Tischbezeichnung
		ret = ret + titel + "\n"; //$NON-NLS-1$
		
		// Variante des Spiels
		switch (gametype) {
		case 1:
			ret = ret + "Hold'em No Limit\n"; //$NON-NLS-1$
			break;
		case 2:
			ret = ret + "Hold'em Put Limit\n"; //$NON-NLS-1$
			break;
		case 3:
			ret = ret + "Hold'em Limit\n"; //$NON-NLS-1$
			break;
		default:
			ret = ret + Messages.Table_11 + "\n"; //$NON-NLS-1$
			break;
		}

		// Blinds
		ret = ret + Messages.Table_12 + 
		      String.valueOf(smallblind) + 
		      "/" + //$NON-NLS-1$ 
		      String.valueOf(bigblind); //$NON-NLS-1$ 
		
		// Ante wenn in der Hand vorhanden
		if (ante != 0.0) {
			ret = ret + "/ Ante:" + String.valueOf(ante); //$NON-NLS-1$; 
		}
		ret = ret + "\n"; //$NON-NLS-1$;
		
		// Datum und Uhrzeit
		
//		SimpleDateFormat df = new SimpleDateFormat( "yyyy/MM/dd - HH:mm:ss" ); CET
//		df.format( dt );
		ret = ret + date.get(Calendar.DATE) + "." + //$NON-NLS-1$ 
		      (date.get(Calendar.MONTH) + 1) + "." + //$NON-NLS-1$ 
		      date.get(Calendar.YEAR) + " "; //$NON-NLS-1$
		if (date.get(Calendar.AM) == 1) {
			ret = ret + (date.get(Calendar.HOUR) + 12);
		} else {
			ret = ret + date.get(Calendar.HOUR);
		}
		ret = ret + ":" + //$NON-NLS-1$ 
		      (date.get(Calendar.MINUTE) + 1) + 
		      ":" + date.get(Calendar.SECOND); //$NON-NLS-1$
		ret = ret + "\n"; //$NON-NLS-1$
		
		// max. Anzahl Plaetze
		ret = ret + String.valueOf(countSeats) + Messages.Table_13;

		// Position des Buttons
		ret = ret + "#" + //$NON-NLS-1$
		     String.valueOf(buttonposition) + 
		     Messages.Table_14 +
		     "\n"; //$NON-NLS-1$

		// Spieler listen
		for (int i = 0; i < countSeats; i++) {
			ret = ret + 
			      Messages.Table_15 + 
			      String.valueOf(i + 1) + 
			      ": "; //$NON-NLS-1$
			if (players[i] == null) {
				ret = ret + Messages.Table_16;
			} else {
				ret = ret + players[i].getName() + " (" //$NON-NLS-1$ 
						+ players[i].getChips() + Messages.Table_17;
				
				// setzt Spieler aus
				if (players[i].getState() == Player.SITTINGOUT) {
					ret = ret + Messages.Table_18;
				}
			}

			ret = ret + "\n"; //$NON-NLS-1$
		}
		
		// Groesse des Pots
		ret = ret + Messages.Table_19 + 
		      String.valueOf(getPot()) + "\n"; //$NON-NLS-1$

		// Groesse des Rake
		ret = ret + Messages.Table_20 +
		      String.valueOf(rake) + "\n"; //$NON-NLS-1$
		
		return ret;
	}
	
	/**
	 * Gibt den von aussen gesetzten Pot zurueck
	 * 
	 * @return Der von aussen gesetzte Pot
	 */
	public double getPotExtern() {
		return potExtern;
	}
	
	/**
	 * Ermittelt die Position eines Spielers am Tisch anhand seines Namens
	 * und gibt seine Sitzposition zurueck
	 * 
	 * @param name Der Name des gesuchten Spielers
	 * @return Seine Position am Tisch
	 * @throws PlayerMissingNameException 
	 */
	public int getSeatOfPlayerByName(String name) throws PlayerMissingNameException {
		int seat = 0;
		
		// Fehler wenn kein Name uebergeben wurde
		if (name == null || "".equals(name)) { //$NON-NLS-1$
			throw new PlayerMissingNameException();
		}
		// Alle Spieler durchlaufen  
		for (int i = 0; i < countSeats; i++) {
			// passt der Name zum Spieler
			if (players[i] != null && name.equals(players[i].getName())) {
				// ja, dann Position merken
				seat = i + 1;
			}
		}
		
		// und Position zurueckmelden
		return seat;
	}
	
	/**
	 * Ermittelt einen Spieler anhand seines Namens und gibt ihn zurueck
	 * 
	 * @param name Der Name des gesuchten Spielers
	 * @return Der Spieler
	 * @throws PlayerMissingNameException
	 */
	public Player getPlayerByName(String name) throws PlayerMissingNameException {
		Player player = null;
		// Fehler wenn kein Name uebergeben wurde
		if (name == null || "".equals(name)) { //$NON-NLS-1$
			throw new PlayerMissingNameException();
		}
		// Alle Spieler durchlaufen  
		for (int i = 0; i < countSeats; i++) {
			// passt der Name zum Spieler
			if (players[i] != null && name.equals(players[i].getName())) {
				// ja, Spieler merken
				player = players[i];
			}
		}
		
		// und Spieler zurueckmelden
		return player;
	}
	
	/**
	 * Fuegt dem Board eine einzelne Karte hinzu
	 * 
	 * @param card Die neue Karte
	 * @return Die Anzahl der Karten im Board mit der neuen
	 * @throws CardException
	 * @throws HandIllegalCardCountException 
	 */
	public int addBoardCard(Card card) throws CardException, HandIllegalCardCountException {
		int cardCount = 0;

		// wurde eine Karte uebergeben
		if (card == null) {
			throw new CardException(Messages.Table_21);
		}
		
		// Anzahl der bereits vorhandenen Karten ermitteln
		for (int i = 0; i < board.length; i++) {
			if (board[i] != null) {
				cardCount++;
			}
		}

		// Ist noch Platz im Board
		if (cardCount > 4) {
			throw new HandIllegalCardCountException(Messages.Table_22);
		}
		
		// Karte an letzer Position einfuegen
		board[cardCount] = card;

		// und Anzahl der uebernommen Karten rueckmelden
		return cardCount + 1;
	}
	
	/**
	 * Gibt die Bezeichnung des Pokerraums zurueck
	 * 
	 * @return Der Pokerraum als Text
	 */
	public String getPokerroomText() {
		String ret = ""; //$NON-NLS-1$
		switch (pokerroom) {
		case POKERSTARS:
			ret = "PokerStars"; //$NON-NLS-1$
			break;
		default:
			ret = Messages.Table_23;
			break;
		}
		return ret;
	}
	
	/**
	 * Gibt den Text zum aktuellen Spieltyp zurueck
	 * 
	 * @return Der Spieltyp als Text
	 */
	public String getGameTypeText() {
		String ret = ""; //$NON-NLS-1$
		switch (gametype) {
		case NO_LIMIT:
			ret = "Hold'em No Limit"; //$NON-NLS-1$
			break;
		case LIMIT:
			ret = "Hold'em Limit"; //$NON-NLS-1$
			break;
		case POT_LIMIT:
			ret = "Hold'em Pot Limit"; //$NON-NLS-1$
			break;
		default:
			ret = Messages.Table_24;
			break;
		}
		return ret;
	}
	
	/**
	 * Gibt Tischtyp als Text zurueck
	 * 
	 * @return Der zum Tischtyp gehoerige Text
	 */
	public String getTabletypeText() {
		String ret = ""; //$NON-NLS-1$
		switch (tabletype) {
		case PLAYMONEY:
			ret = Messages.Table_25;
			break;
		case TOURNAMENT:
			ret = Messages.Table_26;
			break;
		case REALMONEY:
			ret = Messages.Table_27;
			break;
		default:
			ret = Messages.Table_28;
			break;
		}
		return ret;
	}
	
	/**
	 * Kopiert den aktuellen Tisch auf einen neuen
	 * 
	 * @return Eine Kopie des Tisches
	 */
	public Table copy() {
		// neuen Tisch fuer Rueckgabe erzeugen
		Table ret = new Table();
		
		// Werte kopieren
		try {
			ret.setPokerroom(pokerroom);
			ret.setGame(game);
			ret.setDate(date);
			ret.setGametype(gametype);
			ret.setTabletype(tabletype);
			ret.setTitel(titel);
			ret.setSmallblind(smallblind);
			ret.setBigblind(bigblind);
			ret.setCountSeats(countSeats);
			ret.setBoard(board);
			ret.setButtonPos(buttonposition);
			ret.setPot(pot);
			for (int i = 0; i < players.length; i++) {
				if (players[i] != null) {
					ret.addPlayer(players[i].copy(), i + 1);
				}
			}
			ret.setRake(rake);
		} catch (HandException e) {
			ErrorHandler.handleError(e, Messages.Table_29, false);
		} catch (PlayerException e) {
			ErrorHandler.handleError(e, Messages.Table_30, false);
		}

		// und die Kopie zurueck geben
		return ret;
	}
	
	/**
	 * Loescht das Board
	 */
	public void removeBord() {
		board = new Card[5];
	}
	
	public double getBetOfPlayer(int pos) throws IllegalArgumentException {
		double ret = 0.0;
		// Pruefen ob die Position erlaubt ist
		if (pos >= bet.length) {
			// nein, dann Fehler melden
			throw new IllegalArgumentException(Messages.Table_31);
		}
		
		// Position ist erlaubt, dann den aktuellen Einsatz an dieser
		// Position melden, wenn der Spieler noch nicht gefoldet hat
		if (players[pos].getState() != Player.FOLDED){
			ret = bet[pos];
		}
		
		return ret;
	}
	
	/**
	 * Laesst einen Spieler am Tisch einen Betrag bezahlen
	 * 
	 * @param player Der Spieler der zahlen soll
	 * @param amount Der Betrag der gezahl werden soll
	 * @return Der tatsaechlich gezahlte Betrag
	 * @throws PlayerException
	 */
	public double playerPayed(Player player, double amount) throws PlayerException {
		double payed = 0.0;
		
		// pruefen ob ein Spieler uebergeben wurde
		if (player == null) {
			throw new PlayerMissingException();
		}
		
		// pruefen ob ein Betrag zur Zahlung uebergeben wurde
		if (amount <= 0) {
			throw new PlayerIllegalChipsException();
		}
		
		// Alle Angaben vorhanden, dann Betrag kassieren
		payed = player.pay(amount);
		
		// den tatsaechlich gezahlten Betrag merken
		int pos = getSeatOfPlayerByName(player.getName()) - 1; 
		bet[pos] = bet[pos] + payed;
		
		// und zurueckgeben
		return payed;
	}

	/**
	 * Laesst einen Spieler am Tisch einen Betrag bezahlen
	 * 
	 * @param player Der Spieler der zahlen soll
	 * @param amount Der Betrag der gezahl werden soll
	 * @param dontBet Ein Betrag der nicht zum Einsatz des Spieler hinzugefuegt wird, obwohl er vom 
	 *                Guthaben abgezogen und dem Pot zugerechnet wird. Wird benoetigt fuer gleichzeitiges
	 *                Bezahlen von Small- und Bigblind 
	 * @return Der tatsaechlich gezahlte Betrag
	 * @throws PlayerException
	 */
	public double playerPayed(Player player, double amount, double dontBet) throws PlayerException {
		double payed = 0.0;
		
		// pruefen ob ein Spieler uebergeben wurde
		if (player == null) {
			throw new PlayerMissingException();
		}
		
		// pruefen ob ein Betrag zur Zahlung uebergeben wurde
		if (amount <= 0) {
			throw new PlayerIllegalChipsException();
		}
		
		// pruefen ob ein Betrag der nicht als Einsatz gewertet werden soll uebergeben wurde
		if (dontBet <= 0) {
			throw new PlayerIllegalChipsException();
		}
		
		// Alle Angaben vorhanden, dann Betrag kassieren
		payed = player.pay(amount);
		
		// den tatsaechlich gezahlten Betrag merken
		int pos = getSeatOfPlayerByName(player.getName()) - 1; 
		bet[pos] = bet[pos] + payed - dontBet;
		
		// den Betrag der zwar in den Pot, aber nicht in den Einsatz wandern soll
		// dem aktuellen Pot zurechnen
		pot[potNumber] = pot[potNumber] + dontBet;
		
		// und zurueckgeben
		return payed;
	}
	
	/**
	 * Setzt alle Einsaetze und den Pot auf Null
	 */
	public void clearPot() {
		// Main- und Sidepot auf Null setzen
		for (int i = 0; i < pot.length; i++) {
			pot[i] = 0.0;
		}
		
		// Einsaetze der aktuelle Runde auf Null setzen
		for (int i = 0; i < bet.length; i++) {
			bet[i] = 0.0;
		}
	}
	
	/**
	 * Zahlt einen Betrag aus dem Pot aus
	 * 
	 * @param price Der auszuzahlende Betrag
	 * @return Der wirklich ausgezahlte Betrag
	 * @throws TableException 
	 */
	public double payOut(double price) throws TableException {
		
		// pruefen ob der Pot, kann auch ein Sidepot sein, gross
		// genug ist
		if (pot[potNumber] < price) {
			throw new TableException(Messages.Table_32);
		}
		
		// Pot enstprechen verringern
		pot[potNumber] = pot[potNumber] - price;
		
		// Wenn der Pot auf Null gegagen ist und es handelt sich um
		// eine Sidepot, zum naechten Pot wechseln
		if (pot[potNumber] == 0 && potNumber != 0) {
			potNumber--;
		}
		
		// Ausgezahlten Betrag zurueckmelden
		return price;
	}
	
	/**
	 * Ermittelt den kleinsten Einsatz ungleich Null
	 * in der aktuelle Setzrunde
	 * 
	 * @return Der kleineste Einsatz
	 */
	private double findLowestBet() {
		double minBet = Double.MAX_VALUE;
		
		// alle Einsaetze pruefen
		for (int i = 0; i < bet.length; i++) {
			
			// ist der aktuelle kleiner als der bisher kleinste
			// und ungleich Null
			if (bet[i] > 0 && bet[i] < minBet && players[i].getState() != Player.FOLDED && players[i].getState() != Player.SITTINGOUT) {
				// ja, dann diesen merken
				minBet = bet[i];
			}
		}
		
		// wurde ein Einsatz ungleich Null gefunden
		if (minBet == Double.MAX_VALUE) {
			// nein, dann den Rueckgabewert auf Null setzen
			minBet = 0.0;
		}
		
		// den gefundenen Einsatz zurueckgeben
		return minBet;
	}
	
	/**
	 * Ermittelt die Anzahl an Spieler die an der aktuellen
	 * Setzrunde beteiligt sind
	 * 
	 * @return Die Anzahl der beteiligten Spieler
	 */
	private int playerInBet() {
		int ret = 0;
		
		// Anzahl der Spieler mit einem Einsatz groesser Null ermitteln
		for (int i = 0; i < bet.length; i++) {
			if (bet[i] != 0) {
				ret++;
			}
		}
		
		// und zurueckgeben
		return ret;
	}
	
	public void endOfRound() {
		// kleinsten Einsatz in dieser Setzrunde ermitteln
		double minBet = findLowestBet();
		
		// Solange der kleinste Einsatz ungleich Null ist
		// und mehr als ein Spieler noch mehr gesetzt hat
		while (minBet != 0 && playerInBet() > 1) {
			// alle Einsaetze durchlaufen
			for (int i = 0; i < bet.length; i++) {
				// wenn der Spieler was gesetzt hat
				if (bet[i] != 0) {
					// herausfinden ob der Spieler den kleinsten Betrag gesetzt hat
					double aktBet = 0.0;
					if (minBet > bet[i]) {
						aktBet = bet[i];
					} else {
						aktBet = minBet;
					}
					
					// den Pot um den ermittelten Einsatz erhoehen
					pot[potNumber] = pot[potNumber] + aktBet;
					
					// und den jeweiligen Einsatz um den ermittelten verringern
					bet[i] = bet[i] - aktBet;
				}
			}
			
			// wenn die Anazhal an Spielern die jetzt noch einen Einsatz haben
			// groesser eins ist
			if (playerInBet() > 1) {
				// dann den neuen kleinsten Einsatz ermitteln
				minBet = findLowestBet();
				
				// einen neuen Sidepot eroeffnen
				potNumber++;
			}
		}
		
		// wenn noch ein Spieler einen Restbetrag als Einsatz hat
		if (playerInBet() > 0) {
			// bekommt der den Einsatz zurueck
			for (int i = 0; i < bet.length; i++) {
				if (bet[i] != 0) {
					try {
						players[i].deposit(bet[i]);
						bet[i] = 0.0;
					} catch (PlayerIllegalChipsException e) {
						ErrorHandler.handleError(e, Messages.Table_33, false);
					}
				}
			}
		}
	}
}
