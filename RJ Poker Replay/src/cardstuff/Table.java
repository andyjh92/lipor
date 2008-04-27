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

import java.util.Calendar;

import rjPokerReplay.ErrorHandler;

import cardstuffExceptions.CardException;
import cardstuffExceptions.HandException;
import cardstuffExceptions.HandIllegalBigblindException;
import cardstuffExceptions.HandIllegalButtonPositionException;
import cardstuffExceptions.HandIllegalCardCountException;
import cardstuffExceptions.HandIllegalGametypeException;
import cardstuffExceptions.HandIllegalPokerroomException;
import cardstuffExceptions.HandIllegalPotsizeException;
import cardstuffExceptions.HandIllegalRakesizeException;
import cardstuffExceptions.HandIllegalSeatCountException;
import cardstuffExceptions.HandIllegalSeatException;
import cardstuffExceptions.HandIllegalSmallblindException;
import cardstuffExceptions.HandIllegalTabletypeException;
import cardstuffExceptions.HandMissingNameException;
import cardstuffExceptions.HandPositionEmptyException;
import cardstuffExceptions.HandPositionNotEmptyException;
import cardstuffExceptions.HandToMuchPlayersException;
import cardstuffExceptions.PlayerException;
import cardstuffExceptions.PlayerMissingException;
import cardstuffExceptions.PlayerMissingNameException;


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
	
	// der Pot am Ende der Hand
	private double pot = 0.0;
	
	// die Spieler am Tisch
	private Player players[] = new Player[10];
	
	// Bezeichner des Tisches
	private String titel = "";

	// Anzahl an Sitzen
	private int countSeats = 0;

	// Boardkarten
	private Card board[] = new Card[5];

	// Name des Pokerraums
	// Mögliche Werte sind als Konstanten definiert
	private int pokerroom = 0;

	// Nummer des Spiels
	// Muss zusammen mit dem Pokerraum eine eindeutige Zuordnung ergeben
	private String game = "";

	// Höhe des Smallblinds
	private double smallblind = 0.0;

	// Höhe des Bigblinds
	private double bigblind = 0.0;

	// Spieldatum
	private Calendar date = null;
	
	// Art des Spiels
	// Die verschiedenen Spielarten sind über Konstanten definert
	private int gametype = 0;

	// Art des Tisches
	// Die verschiedenen Spielarten sind über Konstanten definert
	private int tabletype = 0;
	
	// Rake der Hand
	private double rake = 0.0;
	
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
	 * @param player Die Spieler für den Tisch
	 */
	public Table(Player players[]) {
		// wurden überhaupt Spieler übergeben?
		if (players != null) {
			// ja, dann diese übernehmen
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
	 * Gibt die Anzahl an Spielern zurück
	 * 
	 * @return Die Anzahl an Spielern am Tisch
	 */
	public int getCountOfPlayer() {
		return countOfPlayers;
	}

	/**
	 * Gbit die Nummer des Spielers beim dem der Button liegt zurück
	 * 
	 * @return Die Position des Buttons
	 */
	public int getButtonposition() {
		return buttonposition + 1;
	}

	/**
	 * Gibt die aktuelle Pot-Größe zurück
	 * 
	 * @return Die aktuelle Pot-Größe
	 */
	public double getPot() {
		return pot;
	}

	/**
	 * Gibt ein Array mit den Spielern am Tisch zurück
	 * 
	 * @return Ein Array mit den Spielern
	 */
	public Player[] getPlayers() {
		return players;
	}

	/**
	 * Gibt die Bezeichnung des Tisches zurück
	 * 
	 * @return Die Bezeichnung
	 */
	public String getTitel() {
		return titel;
	}
	

	/**
	 * Gibt die Anzahl der Sitzplätze zurück
	 * 
	 * @return Die Anzahl der Sitzplätze
	 */
	public int getCountSeats() {
		return countSeats;
	}
	
	/**
	 * Gibt das Board zurück
	 * 
	 * @return Die Boardkarten
	 */
	public Card[] getBoard() {
		return board;
	}
	
	/**
	 * Gibt den Pokerraum zurück
	 * 
	 * @return Konstante des Pokerraums
	 */
	public int getPokerroom() {
		return pokerroom;
	}

	/**
	 * Gibt die Nummer des Spiels zurück
	 * 
	 * @return Die Nummer des Spiels
	 */
	public String getGame() {
		return game;
	}

	/**
	 * Gibt die Höhe des Smallblinds zurück
	 * 
	 * @return Der Smallblind
	 */
	public double getSmallblind() {
		return smallblind;
	}

	/**
	 * Gibt die Höhe des Bigblinds zurück
	 * 
	 * @return Der Bigblind
	 */
	public double getBigblind() {
		return bigblind;
	}

	/**
	 * Gibt das Spieldatum zurück
	 * 
	 * @return Das Datum an dem gespielt wurde
	 */
	public Calendar getDate() {
		return date;
	}
	
	/**
	 * Gibt den Spieltyp zurück.
	 * Genaueres sie Konstanten
	 * 
	 * @return Der Spieltyp
	 */
	public int getGametype() {
		return gametype;
	}
	
	/**
	 * Gibt den Tischtyp zurück
	 * Bedeutung siehe entsprechende Konstanten
	 * 
	 * @return Der Tischtyp
	 */
	public int getTabletype() {
		return tabletype;
	}
	
	/**
	 * Gibt den Rake der Hand zurück
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
		// gibt des den Sitzplatz überhaupt am Tisch
		if (buttonPos < 1 || buttonPos > countSeats) {
			throw new HandIllegalButtonPositionException(
					"Diese Position gibt es am Tisch nicht.");
		}

		// sitzt hier überhaupt ein Spieler
//		if (players[buttonPos - 1] == null) {
//			throw new HandIllegalButtonPositionException(
//					"An dieser Position sitzt (noch) kein Spieler.");
//		}
		// dann den Button an diesen Platz setzen
		this.buttonposition = buttonPos;
	}

	/**
	 * Legt die Anzahl der Sitzplätze am Tisch fest
	 * 
	 * @param countSeats
	 *            the countSeats to set
	 * @throws HandIllegalSeatCountException
	 */
	public void setCountSeats(int countSeats)
			throws HandIllegalSeatCountException {
		// Anzahl der Plätze darf min. 2 und höchsten 10 betragen
		if (countSeats < 2 || countSeats > 10) {
			throw new HandIllegalSeatCountException();
		}

		// passt, HandToMuchPlayersExceptiondann merken
		this.countSeats = countSeats;
	}

	/**
	 * Setzt die in einem Array übergebenen Spieler. Die Spieler werden dabei
	 * entsprechend ihrer Position im Array an den Tisch gesetzt (player[0]
	 * kommt an Platz 1, player[1] an Platz 2 usw. Soll ein Platz leer bleiben,
	 * muss an dieser Stelle im Array NULL übergeben werden.
	 * 
	 * @param players
	 *            Ein Array mit den Spielern
	 * @return Die Anzahl der gesetzten Spieler
	 * @throws HandToMuchPlayersException
	 */
	public int setPlayers(Player[] players) throws HandToMuchPlayersException {
		// höchsten besetzen Platz ermitteln
		int highSeat = 0;
		for (int i = players.length - 1; i >= 0; i--) {
			// ist dieser Platz besetzt ?
			if (players[i] != null) {
				// ja, dann Position merken
				highSeat = i + 1;
				
				// und für Abbruch der Schleife sorgen
				break;
			}
		}
		// sind am Tisch genug Plätze vorhanden
		if (highSeat > countSeats) {
			throw new HandToMuchPlayersException();
		}

		// Plätze reichen, dann Spieler an Tisch setzen
		this.players = players;

		// Anzahl der Spieler ermitteln
		setCountOfPlayers();

		// und zurück geben
		return countOfPlayers;
	}

	/**
	 * Setzt die Boardkarten. Dabei müssen 3 (bis Flop), 4 (bis Turn) oder 5
	 * (bis River) gesetzt werdn
	 * 
	 * @param board
	 *            Die Katen die ins Board sollen
	 * @return Anzahl der eingefügten Karten
	 * @throws HandIllegalCardCountException
	 */
	public int setBoard(Card[] board) throws HandIllegalCardCountException {
		int cardCount = 0;
		int boardPos = 0;

		// Anzahl der übergebenen Karten ermitteln
		for (int i = 0; i < board.length; i++) {
			if (board[i] != null) {
				cardCount++;
			}
		}

		// wurde die geforderte Anzahl an Karten übergeben
		if (cardCount < 3 || cardCount > 5) {
			throw new HandIllegalCardCountException(
					"Dem Board müssen 3, 4 oder 5 Katen hinzugefügt werden.");
		}

		// Ja, dann Karten hinzufügen. Dabei nur die tatsächlich vorhandenen
		// Karten übernehmen
		for (int i = 0; i < board.length; i++) {
			if (board[i] != null) {
				this.board[boardPos] = board[i];
				boardPos++;
			}
		}

		// und Anzahl der übernommen Karten rückmelden
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
		// prüfen ob der Pokerraum bekannt ist
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
		// prüfen ob überhaupt was übergeben wurde
		if (game == null || "".equals(game)) {
			throw new HandMissingNameException();
		}
		
		// Spielbezeichnung merken
		this.game = game;
	}

	/**
	 * Setzt die Höhe des Smallblinds
	 * 
	 * @param smallblind Die Höhe des Smallblinds in der Hand
	 * @throws HandIllegalSmallblindException 
	 */
	public void setSmallblind(double smallblind) throws HandIllegalSmallblindException {
		// Der Smallblind darf nicht 0 sein
		if (smallblind == 0.0) {
			throw new HandIllegalSmallblindException("Der Smallblind darf nicht 0 sein.");
		}
		
		// wenn der Bigblind bereits gesetzt ist, muss der Smallblind dem
		// halben Bigblind entsprechen
		if (bigblind != 0.0 && smallblind != (bigblind / 2.0)) {
			throw new HandIllegalSmallblindException("Der Smallblind muss dem halben Bigblind entsprechen");
		}
		
		// alles passt, dann merken
		this.smallblind = smallblind;
	}

	/**
	 * Setzt die Höhe des Bigblinds
	 * 
	 * @param bigblind Die Höhe des Bigblinds in der Hand
	 * @throws HandIllegalBigblindException 
	 */
	public void setBigblind(double bigblind) throws HandIllegalBigblindException {
		// Der Bigblind darf nicht 0 sein
		if (bigblind == 0.0) {
			throw new HandIllegalBigblindException("Der Bigblind darf nicht 0 sein.");
		}
		
		// wenn der Smalllind bereits gesetzt ist, muss der Bigblind dem
		// doppelten Smallblind entsprechen
		if (smallblind != 0.0 && bigblind != (smallblind * 2.0)) {
			throw new HandIllegalBigblindException("Der Bigblind muss dem doppelten Smallblind entsprechen");
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
	 * @param pot Die zu setzende Pothöhe
	 * @throws HandIllegalPotsizeException 
	 */
	public void setPot(double pot) throws HandIllegalPotsizeException {
		// Der Pot muss größer Null sein
		if (pot < 0.0) {
			throw new HandIllegalPotsizeException();
		}
		
		this.pot = pot;
	}

	
	/**
	 * Setzt den Rake der Hand
	 * 
	 * @param rake Der Rake der Hand
	 * @throws HandIllegalRakesizeException 
	 */
	public void setRake(double rake) throws HandIllegalRakesizeException {
		// Der Rake muss größer gleich Null sein
		if (pot < 0.0) {
			throw new HandIllegalRakesizeException();
		}
		this.rake = rake;
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
		
		// Smallblind vom Spieler nach dem Button kassieren
		blinds = players[buttonposition].pay(smallBlind);
		
		// den Bigblind vom nächsten Spieler kassieren
		blinds = players[buttonposition].pay(smallBlind * 2);
		
		// den Pot um die Blinds erhöhen
		pot = pot + blinds;
		
		// und die kassierten Blinds zurückmelden
		return blinds;
	}
	
	/**
	 * Bewegt den Buttoneinen Spieler weiter
	 */
	public void moveButton() {
		// Button eins weiter geben
		buttonposition++;
		
		// Ist der Button jetzt hinter dem letzen Spieler?
		if (buttonposition > countOfPlayers) {
			// ja, dann zum ersten Spieler mit dem Button
			buttonposition = 1;
		}
	}
	
	public double addToPot(double amount) {
		
		return 0;
	}
	
	/**
	 * Ermittelt die Anzahl der vorhandenen Spieler
	 */
	protected void setCountOfPlayers() {
		// zur Sicherheit die Anzahl der Spieler löschen
		countOfPlayers = 0;

		// Alle möglichen Positionen durchlaufen
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null) {
				// wenn an der Position ein Spieler sitzt die Anzahl erhöhen
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
		// wurde ein Spieler übergeben?
		if (player == null) {
			throw new PlayerMissingException("Es wurde kein Spieler übergeben.");
		}

		// ist die Position am Tisch bereits besetzt
		if (players[position - 1] != null) {
			throw new HandPositionNotEmptyException();
		}

		// ist die Position zulässig
		if (position < 1 || position > countSeats) {
			throw new HandIllegalSeatException();
		}

		// Alles seint zu passen, dann Spieler an Tisch setzen
		players[position - 1] = player;

		// und die neue Anzahl an Spielern ermitteln
		setCountOfPlayers();
	}

	/**
	 * Entfernt einen Spiler vom Tisch anhand der Sitznummer
	 * 
	 * @param position
	 *            Position von der ein Spieler entfert werden soll
	 * @throws HandIllegalSeatException
	 * @throws HandPositionEmptyException
	 */
	public void removePlayer(int position) throws HandIllegalSeatException,
			HandPositionEmptyException {
		// ist die Position zulässig
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

		// wurde ein Name übergeben
		if (name == null || "".equals(name)) {
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
					"Es gibt keinen Spieler mit diesem Namen am Tisch.");
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
		String ret = "";

		// Pokerraum
		switch (pokerroom) {
		case POKERSTARS:
			ret = "Pokerstars"; //$NON-NLS-1$
			break;
		default:
			ret = "Unbekannt";
			break;
		}
		
		// Spielnummer
		ret = ret  + " " + game + "\n"; //$NON-NLS-1$
		
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
			ret = ret + "Unbekante Spielvariante\n";
			break;
		}

		// Blinds
		ret = ret + "Blinds: " + String.valueOf(smallblind) + "/" + String.valueOf(bigblind) + "\n"; //$NON-NLS-1$ 
		
		// Datum und Uhrzeit
		
//		SimpleDateFormat df = new SimpleDateFormat( "yyyy/MM/dd - HH:mm:ss" ); CET
//		df.format( dt );
		ret = ret + date.get(Calendar.DATE) + "." + (date.get(Calendar.MONTH) + 1) + "." + date.get(Calendar.YEAR) + " "; //$NON-NLS-1$
		if (date.get(Calendar.AM) == 1) {
			ret = ret + (date.get(Calendar.HOUR) + 12);
		} else {
			ret = ret + date.get(Calendar.HOUR);
		}
		ret = ret + ":" + (date.get(Calendar.MINUTE) + 1) + ":" + date.get(Calendar.SECOND); //$NON-NLS-1$
		ret = ret + "\n";
		
		// max. Anzahl Plätze
		ret = ret + String.valueOf(countSeats) + "-max Seat ";

		// Position des Buttons
		ret = ret + "#" + String.valueOf(buttonposition) + " is the button\n";

		// Spieler listen
		for (int i = 0; i < countSeats; i++) {
			ret = ret + "Seat " + String.valueOf(i + 1) + ": ";
			if (players[i] == null) {
				ret = ret + "is empty";
			} else {
				ret = ret + players[i].getName() + " (" 
						+ players[i].getChips() + " in chips)";
				
				// setzt Spieler aus
				if (players[i].getState() == Player.SITTINGOUT) {
					ret = ret + " setzt aus";
				}
			}

			ret = ret + "\n";
		}
		
		// Größe des Pots
		ret = ret + "Pot total: " + String.valueOf(pot) + "\n";

		// Größe des Rake
		ret = ret + "Rake: " + String.valueOf(rake) + "\n";
		
		return ret;
	}
	
	/**
	 * Ermittelt die Position eines Spielers am Tisch anhand seines Namens
	 * und gibt seine Sitzposition zurück
	 * 
	 * @param name Der Name des gesuchten Spielers
	 * @return Seine Position am Tisch
	 * @throws PlayerMissingNameException 
	 */
	public int getSeatOfPlayerByName(String name) throws PlayerMissingNameException {
		int seat = 0;
		
		// Fehler wenn kein Name übergeben wurde
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
		
		// und Position zurückmelden
		return seat;
	}
	
	/**
	 * Ermittelt einen Spieler anhand seines Namens und gibt ihn zurück
	 * 
	 * @param name Der Name des gesuchten Spielers
	 * @return Der Spieler
	 * @throws PlayerMissingNameException
	 */
	public Player getPlayerByName(String name) throws PlayerMissingNameException {
		Player player = null;
		// Fehler wenn kein Name übergeben wurde
		if (name == null || "".equals(name)) {
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
		
		// und Spieler zurückmelden
		return player;
	}
	
	/**
	 * Fügt dem Board eine einzelne Karte hinzu
	 * 
	 * @param card Die neue Karte
	 * @return Die Anzahl der Karten im Board mit der neuen
	 * @throws CardException
	 * @throws HandIllegalCardCountException 
	 */
	public int addBoardCard(Card card) throws CardException, HandIllegalCardCountException {
		int cardCount = 0;

		// wurde eine Karte übergeben
		if (card == null) {
			throw new CardException("Es wurde keine Karte übergeben.");
		}
		
		// Anzahl der bereits vorhandenen Karten ermitteln
		for (int i = 0; i < board.length; i++) {
			if (board[i] != null) {
				cardCount++;
			}
		}

		// Ist noch Platz im Board
		if (cardCount > 4) {
			throw new HandIllegalCardCountException("Das Board ist bereits voll.");
		}
		
		// Karte an letzer Position einfügen
		board[cardCount] = card;

		// und Anzahl der übernommen Karten rückmelden
		return cardCount + 1;
	}
	
	/**
	 * Gibt die Bezeichnung des Pokerraums zurück
	 * 
	 * @return Der Pokerraum als Text
	 */
	public String getPokerroomText() {
		String ret = "";
		switch (pokerroom) {
		case POKERSTARS:
			ret = "PokerStars";
			break;
		default:
			ret = "Unbekannt";
			break;
		}
		return ret;
	}
	
	/**
	 * Gibt den Text zum aktuellen Spieltyp zurück
	 * 
	 * @return Der Spieltyp als Text
	 */
	public String getGameTypeText() {
		String ret = "";
		switch (gametype) {
		case NO_LIMIT:
			ret = "Hold'em No Limit";
			break;
		case LIMIT:
			ret = "Hold'em Limit";
			break;
		case POT_LIMIT:
			ret = "Hold'em Pot Limit";
			break;
		default:
			ret = "Unbekannt";
			break;
		}
		return ret;
	}
	
	/**
	 * Gibt Tischtyp als Text zurück
	 * 
	 * @return Der zum Tischtyp gehörige Text
	 */
	public String getTabletypeText() {
		String ret = "";
		switch (tabletype) {
		case PLAYMONEY:
			ret = "Play Money";
			break;
		case TOURNAMENT:
			ret = "Tournament";
			break;
		case REALMONEY:
			ret = "Real Money";
			break;
		default:
			ret = "Unbekannt";
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
		// neuen Tisch für Rückgabe erzeugen
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
			ErrorHandler.handleError(e, "Fehler beim Kopieren eines Tisches.", false);
		} catch (PlayerException e) {
			ErrorHandler.handleError(e, "Fehler beim Kopieren eines Tisches.", false);
		}

		// und die Kopie zurück geben
		return ret;
	}
	
	/**
	 * Löscht das Board
	 */
	public void removeBord() {
		board = new Card[5];
	}
}
