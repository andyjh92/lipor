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

package io.gitlab.lipor.views;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

import io.gitlab.lipor.Activator;
import io.gitlab.lipor.ApplicationWorkbenchAdvisor;
import io.gitlab.lipor.Constants;
import io.gitlab.lipor.cardstuff.Action;
import io.gitlab.lipor.cardstuff.Card;
import io.gitlab.lipor.cardstuff.Hand;
import io.gitlab.lipor.cardstuff.Handhistory;
import io.gitlab.lipor.cardstuff.Player;
import io.gitlab.lipor.cardstuff.Table;
import io.gitlab.lipor.cardstuffExceptions.ActionIllegalActionException;
import io.gitlab.lipor.cardstuffExceptions.CardException;
import io.gitlab.lipor.cardstuffExceptions.PlayerException;
import io.gitlab.lipor.cardstuffExceptions.TableException;
import io.gitlab.lipor.preferences.PreferenceConstants;
import io.gitlab.lipor.util.ErrorHandler;

public class ViewTable extends ViewPart {
	public static final String ID = "RjPokerReplay.ViewTable"; //$NON-NLS-1$

	// Zeichenflaeche
	private Canvas canvas;
	
	// Hintergrundbild
	private Image background;
	
	// Vorlage fuer original Hintergrundbild
	private ImageData background_org;
	
	// Vorlage fuer groessenangepasstes Hintergundbild
	private ImageData background_scal;
	
	// Daten fuer Bildumwanldung
	private ImageData imgData;
	
	// gemerkte Breite des Zeichenfensters 
	private int canvasWidht;
	
	// gemerkte Hoehe des Zeichenfensters
	private int canvasHeight;
	
	// Zeichenflaeche fuer den tisch
	private GC gcTable;
	
	// Kennzeichen fuer Preflop, Flop, Turn oder River
	private int status = 0;
	
	// aktueller Tisch
	private Table table = null;
	
	// Der aktuelle Display
	private Display display;
	
	// Feld mit den Positionen der Spieler am Tisch
	private ArrayList<Rectangle> playerPos;
	
	// Feld mit der Position des Buttons am Tisch
	private ArrayList<Rectangle> buttonPos;
	
	// Feld mit den Positionen der Boardkarten am Tisch
	private ArrayList<Rectangle> boardCardPos;
	
	// Bild des Buttons
	private Image button;
	
	// Thread fuer den Autoplay
	private static Thread thread;
	
	// Hoehe der Pokerkarten
	private final static int HOEHE_KARTE = 96;
	
	// Breite der Pokerkarte
	private final static int BREITE_KARTE = 72;

	// Hoehe und Breite der Spielerfelder
	private final static int PLAYER_FIELD_HEIGHT = 130;
	private final static int PLAYER_FLIED_WIDTH = 90;
	
	// Karten die im Board gezeigt werden
	private Card boardcards[] = new Card[5];
	
	// Formatierer für Betraege
	private NumberFormat numberFormat;
	
	/**
	 * Zeichnet den Pokertsich
	 */
	public void createPartControl(Composite parent) {

		// Display besorgen
		display = Display.getCurrent();

		// Zeichenflaeche mit Layout
		canvas = new Canvas(parent, SWT.BACKGROUND);
		canvas.setLayout(null);

		// Hintergundbild mit Pokertisch in der original Groesse
		background_org = ApplicationWorkbenchAdvisor.getImageStore().get(
				Constants.IMG_BACKGROUND).getImageData();

		// Groesse der Zeichenflaeche
		int width = canvas.getParent().getShell().getBounds().width;
		int height = canvas.getParent().getShell().getBounds().height;

		// Hintergrund auf passende Groesse bringen
		background_scal = background_org.scaledTo(width, height);

		// und Bild als Hintergrund verwenden
		background = new Image(display, background_scal);
		gcTable = new GC(background);
		canvas.setBackgroundImage(background);

		// Wenn das Bild neu gezeichnet werden soll
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				if (canvasHeight != canvas.getBounds().height
						|| canvasWidht != canvas.getBounds().width) {
					// Bei Groessenaenderung den Tisch neu zeichnen
					drawTable();
				}
			}
		});
	}

	/**
	 * Zeichnete den Tisch
	 */
	public void drawTable() {
		
		// Groesse der Zeichenflaeche ermitteln
		Rectangle client = canvas.getClientArea();
		int width = client.width;
		int height = client.height;

		// Bestimmte Dinge muessen nur bei einer Aenderung der Groesse durchgefuehrt
		// werden
		if (canvasHeight != height || canvasWidht != width) {
			// aktuelle Groesse der Zeichenflaeche merken
			canvasWidht = canvas.getBounds().width;
			canvasHeight = canvas.getBounds().height;

			// Bild auf die passende groesse bringen und als Hintergund verwenden
			background_scal = background_org.scaledTo(width, height);

			// Position der einzelnen Spieler am Tisch ermitteln
			playerPos = getPlayPosition(9, height, width);

			// Position der Boardkarten ermitteln
			boardCardPos = getBoardCardPos(height, width);
			
			// Position des Buttons bestimmen
			buttonPos = getButtonPosition(9, height, width);

			// Bild des Buttons laden
			imgData = ApplicationWorkbenchAdvisor.getImageStore().get(
					Constants.IMG_BUTTON_BUTTON).getImageData();
			imgData = imgData.scaledTo(buttonPos.get(0).width,
					buttonPos.get(0).height);
			button = new Image(display, imgData);
		}

		// aktuellen Tisch holen
		if (ApplicationWorkbenchAdvisor.getTable() != null
				&& ApplicationWorkbenchAdvisor.getHandStep() == 0) {
			table = ApplicationWorkbenchAdvisor.getTable().copy();
		}

		// wenn noch keine Intanz von Numberformat vorliegt
		if ( numberFormat == null && table != null) {
			// Echtgeldtisch?
			if (table.getTabletype() == Table.REALMONEY){
				// ja, dann Zahlenformat für Dollar
				numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
			} else {
				// sonst einfache Zahlen
				numberFormat = NumberFormat.getInstance();
			}
		}
		
		// Hintergrund neue anzeigen
		gcTable.dispose();
		background.dispose();
		background = new Image(display, background_scal);
		gcTable = new GC(background);
		canvas.setBackgroundImage(background);

		// Pruefen ob der Tisch Daten enthaelt
		if (table != null && table.getPokerroom() != 0) {
			// Button anzeigen
			int shiftButton = 1;
			if (table.getCountSeats() == 10) {
				shiftButton = 2;
			}
			int posOfButton = 0;
			if (table != null) {
				posOfButton = table.getButtonposition();
			}
			if (posOfButton != 0) {
				gcTable.drawImage(button, buttonPos.get(posOfButton - shiftButton).x,
						buttonPos.get(posOfButton - shiftButton).y);
			}

			// Spieler an Tisch setzen
			int shiftPlace = 1;
			if (table.getCountSeats() == 10) {
				shiftPlace = 0;
			}
			for (int i = 0; i < 10; i++) {
				if (table.getPlayers()[i] != null) {
					Player player = table.getPlayers()[i];
					int textX = playerPos.get(i + shiftPlace).x;
					int textY = playerPos.get(i + shiftPlace).y;
					int lineSpacing2 = 15;
					// an dieser Postion sitzt auch wirklich ein Spieler

					// Name des Spielers
					if (table.getLastActionsPlayer() != null && table.getLastActionsPlayer().getName().equals(player.getName())) {
						gcTable.setForeground(display.getSystemColor(SWT.COLOR_RED));						
					}
					gcTable.drawText(player.getName(), textX, textY, true);
					gcTable.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
					textY = textY + lineSpacing2;

					// Stack
					double stack = player.getChips();
					String text;
					if (stack == 0) {
						// Spieler ist All-In, dann Text statt Betrag anzeigen
						text = Messages.ViewTable_0;
					} else {
						// Spieler hat noch was, dann das anzeigen
//						text = String.valueOf(stack);
						text = numberFormat.format(stack);
					}
					gcTable.drawText(text, textX,
							textY, true);
					textY = textY + lineSpacing2;

					// Hinweis wenn der Spieler nicht aktiv ist
					if (player.getState() == Player.SITTINGOUT) {
						gcTable.drawText(Messages.ViewTable_1, textX, textY, true);
						textY = textY + lineSpacing2;

					}

					// der aktuelle Einsatz
					String einsatz = Messages.ViewTable_2;
//					einsatz = einsatz + String.valueOf(table.getBetOfPlayer(i));
					einsatz = einsatz + numberFormat.format(table.getBetOfPlayer(i));
					gcTable.drawText(einsatz, textX, textY, true);
					textY = textY + lineSpacing2;

					// Karten der Spieler
					if (player.getPocketCards() != null) {
						// erste Karte
						Card card = player.getPocketCards()[0];
						if (card != null) {
							showCard(card, textX, textY);
						}

						// zweite Karte
						card = player.getPocketCards()[1];
						if (card != null) {
							showCard(card, textX + 40, textY);
						}
					}

				}
			}

			// Boardkarten
			switch (status) {
			case 1:
				showFlop();
				break;
			case 2:
				showTurn();
				break;
			case 3:
				showRiver();
				break;
			}

			// Der Pot
			int potY = (int) (canvasHeight / 2.0 - 20.0) - HOEHE_KARTE / 2;
			int potX = (int) (canvasWidht / 2.0 - 10.0);
//			String potText = Messages.ViewTable_3 + String.valueOf(table.getPot());
			String potText = Messages.ViewTable_3 + numberFormat.format(table.getPot());
			gcTable.drawText(potText, potX, potY, true);
			
			// Zum Schluss alle die es interresiert informieren
			Activator.getDefault().inform();
		}
		
//		testGetAction();
	}

	/**
	 * Zeigt den River, zusaetzlich werden Flop und Turn angezeigt
	 */
	private void showRiver() {
		showFlop();
		showTurn();
		if (table.getBoard()[4] != null) {
			showCard(table.getBoard()[4], boardCardPos.get(4).x, boardCardPos.get(4).y);
		}
	}

	/**
	 * Zeigt den Turn, zusaetzlich wird der Flop angezeigt
	 * 
	 * @param table
	 *            Der Tisch zu dem der Turn gezeigt wird
	 */
	private void showTurn() {
		showFlop();
		if (table.getBoard()[3] != null) {
			showCard(table.getBoard()[3], boardCardPos.get(3).x, boardCardPos.get(3).y);
		}
	}

	/**
	 * Zeigt den Flop
	 */
	private void showFlop() {
		if (table.getBoard()[0] != null && table.getBoard()[1] != null
				&& table.getBoard()[2] != null) {
			showCard(table.getBoard()[0], boardCardPos.get(0).x, boardCardPos.get(0).y);
			showCard(table.getBoard()[1], boardCardPos.get(1).x, boardCardPos.get(1).y);
			showCard(table.getBoard()[2], boardCardPos.get(2).x, boardCardPos.get(2).y);
		}
	}

	/**
	 * Zeit eine Karte am Tisch
	 * 
	 * @param card
	 *            Die Karte
	 * @param x
	 *            Die X-Koordinate an der die Karte gezeigt werden soll
	 * @param y
	 *            Die Y-Koordinate an der die Karte gezeigt werden soll
	 */
	private void showCard(Card card, int x, int y) {
		if (card != null) {
			try {
				// Bild der Karte holen
				ImageData imgData = card.getImage().getImageData();

				// Karte auf passende Groesse bringen
				imgData = imgData.scaledTo(54, 72);
				Image card2 = new Image(Display.getCurrent(), imgData);

				// und anzeigen
				gcTable.drawImage(card2, x, y);
			} catch (CardException e) {
				ErrorHandler.handleError(e,
						Messages.ViewTable_4, false);
			}
		}
	}

	/**
	 * Position der einzelnen Spieler festlegen
	 * 
	 * @param countPlayer
	 *            Anzahl der Spieler
	 * @param height
	 *            Hoehe der Grafik
	 * @param width
	 *            Breite der Grafik
	 * @return Eine Liste mit den Positionen der Spieler
	 */
	private static ArrayList<Rectangle> getPlayPosition(int countPlayer,
			int height, int width) {
		// Position und Groesse der Felder fuer die Spieler
		ArrayList<Rectangle> playerPos = new ArrayList<Rectangle>();
		
		// Abstand der Spielerfelder zum Tisch
		final int abstand = 5;

		// Dealer
		int posHi = (int) (height * 0.29) - PLAYER_FIELD_HEIGHT - abstand;
		if (posHi < 0) {
			posHi = 0;
		}
		int posRi =(int) (width / 2 - PLAYER_FLIED_WIDTH / 2); 
		playerPos.add(new Rectangle(posRi, posHi, PLAYER_FLIED_WIDTH, PLAYER_FIELD_HEIGHT));

		// Spieler 1
		posHi = (int) (height * 0.3 - PLAYER_FIELD_HEIGHT) - abstand;
		if (posHi < 0) {
			posHi = 0;
		}
		posRi = (int) (width * 0.6125);
		playerPos.add(new Rectangle(posRi, posHi, PLAYER_FLIED_WIDTH, PLAYER_FIELD_HEIGHT));

		// Spieler 2
		posHi = (int) (height * 0.35833 - PLAYER_FIELD_HEIGHT) - abstand;
		if (posHi < 0) {
			posHi = 0;
		}
		posRi = (int) (width * 0.76875);
		playerPos.add(new Rectangle(posRi, posHi, PLAYER_FLIED_WIDTH, PLAYER_FIELD_HEIGHT));

		// Spieler 3
		posHi = (int) (height * 0.63333) + abstand;
		if (posHi < 0) {
			posHi = 0;
		}
		posRi = (int) (width * 0.76875);
		playerPos.add(new Rectangle(posRi, posHi, PLAYER_FLIED_WIDTH, PLAYER_FIELD_HEIGHT));

		// Spieler 4
		posHi = (int) (height * 0.69166) + abstand;
		if (posHi < 0) {
			posHi = 0;
		}
		posRi = (int) (width * 0.6125);
		playerPos.add(new Rectangle(posRi, posHi, PLAYER_FLIED_WIDTH, PLAYER_FIELD_HEIGHT));

		// Spieler 5
		posHi = (int) (height * 0.7) + abstand;
		if (posHi < 0) {
			posHi = 0;
		}
		posRi =(int) (width / 2 - PLAYER_FLIED_WIDTH / 2); 
		playerPos.add(new Rectangle(posRi, posHi, PLAYER_FLIED_WIDTH, PLAYER_FIELD_HEIGHT));

		// Spieler 6
		posHi = (int) (height * 0.69166) + abstand;
		if (posHi < 0) {
			posHi = 0;
		}
		posRi = (int) (width * 0.28125);
		playerPos.add(new Rectangle(posRi, posHi, PLAYER_FLIED_WIDTH, PLAYER_FIELD_HEIGHT));

		// Spieler 7
		posHi = (int) (height * 0.63333) + abstand;
		if (posHi < 0) {
			posHi = 0;
		}
		posRi = (int) (width * 0.1375);
		playerPos.add(new Rectangle(posRi, posHi, PLAYER_FLIED_WIDTH, PLAYER_FIELD_HEIGHT));

		// Spieler 8
		posHi = (int) (height * 0.35833 - PLAYER_FIELD_HEIGHT) - abstand;
		if (posHi < 0) {
			posHi = 0;
		}
		posRi = (int) (width * 0.1375);
		playerPos.add(new Rectangle(posRi, posHi, PLAYER_FLIED_WIDTH, PLAYER_FIELD_HEIGHT));

		// Spieler 9
		posHi = (int) (height * 0.3 - PLAYER_FIELD_HEIGHT) - abstand;
		if (posHi < 0) {
			posHi = 0;
		}
		posRi = (int) (width * 0.28125);
		playerPos.add(new Rectangle(posRi, posHi, PLAYER_FLIED_WIDTH, PLAYER_FIELD_HEIGHT));

		return playerPos;
	}

	/**
	 * Position der Boardkarten ermitteln
	 * 
	 * @param height Hoehe der Grafik
	 * @param width Breite der Grafik
	 * @return Array mit den Positionen fuer die Karten
	 */
	private static ArrayList<Rectangle> getBoardCardPos(int height, int width) {
		// Position und Groesse der Felder fuer die Karten
		ArrayList<Rectangle> cardPos = new ArrayList<Rectangle>();
		
		// 1. Karte Flop
		int posHi = (int) (height / 2 ) - HOEHE_KARTE / 2;
		int posRi = (int) (width / 2 - BREITE_KARTE * 2.5);
		cardPos.add(new Rectangle(posRi, posHi, BREITE_KARTE, HOEHE_KARTE));
		
		// 2. Karte Flop
		posRi = (int) (width / 2 - BREITE_KARTE * 1.5);
		cardPos.add(new Rectangle(posRi, posHi, BREITE_KARTE, HOEHE_KARTE));
		
		// 3. Karte Flop
		posRi = (int) (width / 2 - BREITE_KARTE * 0.5);
		cardPos.add(new Rectangle(posRi, posHi, BREITE_KARTE, HOEHE_KARTE));
		
		// 4. Karte Turn
		posRi = (int) (width / 2 + BREITE_KARTE * 0.5);
		cardPos.add(new Rectangle(posRi, posHi, BREITE_KARTE, HOEHE_KARTE));
		
		// 5. Karte River
		posRi = (int) (width / 2 + BREITE_KARTE * 1.5);
		cardPos.add(new Rectangle(posRi, posHi, BREITE_KARTE, HOEHE_KARTE));
		
		return cardPos;
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		canvas.setFocus();
	}

	/**
	 * Uebertraegt eine Aktion an den aktuellen Tisch
	 * 
	 * @param step
	 * @param hand
	 */
	protected void doAction(Action action) {

		// Spieler zur Aktion ermitteln
		Player p1 = action.getPlayer();
		Player player = null;

		// Position des Spielers am Tisch
		int pos = 0;
		if (p1 != null) {
			try {
				pos = table.getSeatOfPlayerByName(p1.getName()) - 1;
				player = table.getPlayerByName(p1.getName());
			} catch (PlayerException e) {
				ErrorHandler.handleError(e,
						Messages.ViewTable_5, false);
			}
		}

		// Die verschiedenen Aktionen
		switch (action.getAction()) {
		case Action.DEAL:
			// Karten werden verteilt
			try {
				// alle Spieler ablaufen
				for (int i = 0; i < 10; i++) {
					Player aPlayer = table.getPlayers()[i];

					if (aPlayer != null && aPlayer.getPocketCards()[1] == null) {
						table.getPlayers()[i].addPocketCard(new Card(Card.TWO, Card.BACK));
					}
				}
			} catch (PlayerException e) {
				ErrorHandler.handleError(e,
						Messages.ViewTable_6, false);
			} catch (CardException e) {
				ErrorHandler.handleError(e,
						Messages.ViewTable_7, false);
			}
			break;
		case Action.SMALLBLIND:
			// Spieler zahl Smallblind
			
			// Zur Sicherheit, falls vorher Antes bezahlt wurden, so tun als ob neue Runde
			table.endOfRound();
			
			try {
					table.playerPayed(player, table.getSmallblind());
				} catch (PlayerException e) {
					ErrorHandler.handleError(e,
							Messages.ViewTable_8, false);
				}
			break;
		case Action.BIGBLIND:
			// Spieler zahlt Bigblind
			try {
					table.playerPayed(player, table.getBigblind());
				} catch (PlayerException e) {
					ErrorHandler.handleError(e,
							Messages.ViewTable_9, false);
				}
			break;
		case Action.ANTE:
			// Spieler zahl ante
			try {
					table.playerPayed(player, (Double) action.getValue());
				} catch (PlayerException e) {
					ErrorHandler.handleError(e,
							Messages.ViewTable_10, false);
				}
			break;
		case Action.BET:
			try {
					table.playerPayed(player, (Double) action.getValue());
				} catch (PlayerException e) {
					ErrorHandler.handleError(e,
							Messages.ViewTable_11, false);
				}
			break;
		case Action.CALL:
			// ein Spieler called den Einsatz
			try {
					table.playerPayed(player, (Double) action.getValue());
				} catch (PlayerException e) {
					ErrorHandler.handleError(e,
							Messages.ViewTable_12, false);
				}
			break;
		case Action.CHECK:
			// do nothing
			break;
		case Action.FOLD:
			// Spieler foldet
			if (player != null) {
				// Karten nicht mehr anzeigen
				player.fold();
			}
			break;
		case Action.RAISE:
			// Spieler erhoeht
			try {
					table.playerPayed(player, (Double) action.getValue() - table.getBetOfPlayer(pos));
				} catch (IllegalArgumentException e) {
					ErrorHandler.handleError(e,
							Messages.ViewTable_13, false);
				} catch (PlayerException e) {
					ErrorHandler.handleError(e,
							Messages.ViewTable_14, false);
				}
			break;
		case Action.FLOP:
			// merken das der Flop gezeitgt wird
			status = 1;
			
			// Karten fuer die Bewertung der Hand in das Zwischenboard uebertragen
			for (int i = 0; i < 3; i++) {
				boardcards[i] = table.getBoard()[i];
			}

			// es beginnt die naechste Setzrunde
			table.endOfRound();
			break;
		case Action.TURN:
			// merken das der Turn gezeitgt wird
			status = 2;

			// Karten fuer die Bewertung der Hand in das Zwischenboard uebertragen
			boardcards[3] = table.getBoard()[3];
			
			// es beginnt die naechste Setzrunde
			table.endOfRound();
			break;
		case Action.RIVER:
			// merken das der River gezeitgt wird
			status = 3;
			
			// Karten fuer die Bewertung der Hand in das Zwischenboard uebertragen
			boardcards[4] = table.getBoard()[4];

			// es beginnt die naechste Setzrunde
			table.endOfRound();
			break;
		case Action.COLLECT:
			// Spieler bekommt den Pot
			
			// zur Sicherheit die aktuelle Setzrunde beenden
			table.endOfRound();
			
			// Betraege auszahlen
			try {
				// Spieler seinen Anteil am Pot zuweisen
				player.deposit(table.payOut((Double) action.getValue()));
				
			} catch (PlayerException e) {
				ErrorHandler.handleError(e,
						Messages.ViewTable_15, false);
			} catch (TableException e) {
				ErrorHandler.handleError(e,
						Messages.ViewTable_16, false);
			}
			break;
		case Action.SHOW:
			// ein Spieler zeigt seine Karten
			Card card1 = (Card) action.getValue();
			// Zeiger auf naechste Aktion stellen
			ApplicationWorkbenchAdvisor.nextHandStep();

			// zweite Karte ermitteln
			Hand hand = null;
			int activeHand = ApplicationWorkbenchAdvisor.getActiveHand();
			if (activeHand > -1) {
				hand = ApplicationWorkbenchAdvisor.getHands().get(activeHand);
			}
			int handStep = ApplicationWorkbenchAdvisor.getHandStep();
			
			// Deckblaetter gegen echte Karten tauschen
			try {
				Card card2 = (Card) hand.getAction(handStep - 1).getValue();
				player.replacePoketCards(card1, card2);
			} catch (PlayerException e) {
				ErrorHandler.handleError(e,	Messages.ViewTable_17, false);
			} catch (ActionIllegalActionException e) {
				ErrorHandler.handleError(e,	Messages.ViewTable_18, false);
			}
			break;
		case Action.MUCK:
			// Spieler zeigt seine Karten am Roundenende nicht
			if (player != null) {
				// Karten nicht mehr anzeigen
				player.fold();
			}
			break;
		case Action.SHOWDOWN:
			// der grosse Showdown
			
			// Setzrunde beenden
			table.endOfRound();
			break;
		case Action.UNCALLED_BET:
			// Spieler bekommt einen nicht gecallten Einsatz zurueck
			
			// Setzrunde beenden
			// Dabei wird ein ueberzaehliger Einsatz autom. zurueckgezahlt
			table.endOfRound();
			break;
		case Action.SMALL_AND_BIGBLIND:
			// Spieler zahlt Small- und Bigblind
			try {
					table.playerPayed(player, (Double) action.getValue(), table.getSmallblind());
				} catch (PlayerException e) {
					ErrorHandler.handleError(e,
							Messages.ViewTable_9, false);
				}
			break;
		}
		if (action.getPlayer() != null) {
			table.setLastActionsPlayer(action.getPlayer());
		}
	}

	/**
	 * Versetzt den Tisch in den Anfangszustand
	 */
	public void resetTable() {
		// aktuellen Pot zuruecksetzen
		if (table != null) {
			table.clearPot();
		}

		// Status fuer Flop/Turn/River auf Angang setzen
		status = 0;

		// Boardkarten loeschen
		if (table != null) {
			table.removeBord();
		}
		
		// den Thread fuer Autoplay loeschen
		thread = null;
		
		// den Modus fuer Autoplay zuruecksetzen
		ApplicationWorkbenchAdvisor.setAutoplay(false);
		
		// gezeigte Boardkarten loeschen
		boardcards = new Card[5];
		
		// Formatierer für Zahlen rücksetzen
		numberFormat = null;
	}

	/**
	 * Position der einzelnen Spieler festlegen
	 * 
	 * @param countPlayer
	 *            Anzahl der Spieler
	 * @param height
	 *            Hoehe der Grafik
	 * @param width
	 *            Breite der Grafik
	 * @return Eine Liste mit den Positionen der Spieler
	 */
	private static ArrayList<Rectangle> getButtonPosition(int countPlayer,
			int height, int width) {
		// Position und Groesse der Felder fuer die Spieler
		ArrayList<Rectangle> buttonPos = new ArrayList<Rectangle>();

		// Hoehe und Breite des Button
		final int hoehe = 15;
		final int breite = 15;
		
		// Versatz für den Button zum Tisch
		final int versatz = 30;

		// Dealer
		int posHi = (int) (height * 0.25) + versatz;
		int posRi = (int) (width / 2 - breite / 2);
		buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));
		  
		// Spieler 1
		posHi = (int) (height * 0.3 - hoehe) + versatz;
		posRi = (int) (width * 0.6125);
		buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 2
		posHi = (int) (height * 0.35833 - hoehe) + versatz;
		posRi = (int) (width * 0.76875);
		buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 3
		posHi = (int) (height * 0.63333) - versatz;
		posRi = (int) (width * 0.76875);
		buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 4
		posHi = (int) (height * 0.69166) - versatz;
		posRi = (int) (width * 0.6125);
		buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 5
		posHi = (int) (height * 0.7) - versatz;
		posRi =(int) (width / 2 - breite / 2);
		buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 6
		posHi = (int) (height * 0.69166) -  versatz;
		posRi = (int) (width * 0.28125) + PLAYER_FLIED_WIDTH;
		buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 7
		posHi = (int) (height * 0.63333) - versatz;
		posRi = (int) (width * 0.1375) + PLAYER_FLIED_WIDTH;
		buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 8
		posHi = (int) (height * 0.35833 - hoehe) + versatz;
		posRi = (int) (width * 0.1375) + PLAYER_FLIED_WIDTH;;
		buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 9
		posHi = (int) (height * 0.3 - hoehe) + versatz;
		posRi = (int) (width * 0.28125) + PLAYER_FLIED_WIDTH;;
		buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		return buttonPos;
	}

	/**
	 * Sprigt zum Anfang der Hand wenn moeglich, sonst wenn moeglich zur
	 * vorherigen
	 */
	public void doHandBack() {
		// aktive Hand und Handstep holen
		int activeHand = ApplicationWorkbenchAdvisor.getActiveHand();
		if (activeHand >= 0) {
			int handStep = ApplicationWorkbenchAdvisor.getHandStep();
			// befinden wir uns am Anfang der Hand
			if (handStep < 1) {
				// Ja pruefen ob es sich um die erste Hand handelt
				if (activeHand > 0) {
					// nein, dann zum Anfang der vorherigen Hand
					activeHand--;
					handStep = 0;
				}
			} else {
				// nein, dann zum Anfang dieser Hand
				handStep = 0;
			}

			// ermittelte Werte setzen
			ApplicationWorkbenchAdvisor.setActiveHand(activeHand);
			ApplicationWorkbenchAdvisor.setHandStep(handStep);

			// Tisch zuruecksetzen
			ApplicationWorkbenchAdvisor.setTable((Table) ApplicationWorkbenchAdvisor.getHands().get(activeHand));

			// Tisch auf Anfang stellen
			resetTable();

			// Tisch mit Aenderungen neu anzeigen
			drawTable();
		}
	}

	/**
	 * Geht, wenn moeglich, einen Schritt zurueck
	 */
	public void doStepBack() {
		// Anzahl der bisher gemachten Schritte holen
		int step = ApplicationWorkbenchAdvisor.getHandStep();

		// Tisch auf Start setzen

		doHandBack();

		// und genau eine Schritt weniger als bisher gemacht ausfuehren
		for (int i = 1; i < step; i++) {
			doNextStep();
		}
	}

	/**
	 * Springt wenn moeglich zur naechsten Hand
	 */
	public void doNextHand() {
		// Zeiger auf naechste Hand setzen
		ApplicationWorkbenchAdvisor.nextHand();

		// Tisch zurueck setzen
		resetTable();

		// und frischen Tisch zeichnen
		drawTable();
	}

	/**
	 * Fuehrt den naechten Schritt aus
	 */
	public void doNextStep() {
		// aktive Hand holen
		Hand hand = null;
		int activeHand = ApplicationWorkbenchAdvisor.getActiveHand();
		if (activeHand > -1) {
			hand = ApplicationWorkbenchAdvisor.getHands().get(activeHand);
		}
		
		// wenn es keine aktive Hand gibt hier abbrechen
		if (hand ==null) {
			return;
		}
		
		// letzen ausgefuehrten Step merken
		int oldHandStep = ApplicationWorkbenchAdvisor.getHandStep();
		
		// Zeiger auf naechste Aktion stellen
		ApplicationWorkbenchAdvisor.nextHandStep();

		// neuen Step holen
		int handStep = ApplicationWorkbenchAdvisor.getHandStep();
		
		// Aktion in Tisch uebernehmen
		if (handStep > 0 && oldHandStep != handStep) {
			try {
				doAction(hand.getAction(handStep - 1));
			} catch (ActionIllegalActionException e) {
				doNextHand();
			}
		}

		// Tisch mit Aenderungen neu anzeigen
		drawTable();
	}

	/**
	 * Spielt die Hand bis zur naechten Aktion des Spielers ab
	 */
	public void doNextOwnAction() {
		new Thread("rjPokerAutoPlay") { //$NON-NLS-1$)
			public void run() {
				int delay = 1000 * Activator.getDefault().getPreferenceStore()
						.getInt(PreferenceConstants.P_GENERAL_TIME_DELAY);
				String name = ""; //$NON-NLS-1$
				String ownName = ""; //$NON-NLS-1$

				if (ApplicationWorkbenchAdvisor.getTable() != null) {
					switch (ApplicationWorkbenchAdvisor.getTable().getPokerroom()) {
					case Handhistory.WIN2DAY:
						ownName = Activator.getDefault().getPreferenceStore()
								.getString(PreferenceConstants.P_WIN2DAY_PLAYER);
						break;
					default:
						break;
					}
					int handStepOld = 0;
					int handStep = 0;
					do {
						handStepOld = ApplicationWorkbenchAdvisor.getHandStep();
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								doNextStep();
							}
						});
						Hand hand = null;
						int activeHand = ApplicationWorkbenchAdvisor.getActiveHand();
						if (activeHand > -1) {
							hand = ApplicationWorkbenchAdvisor.getHands().get(activeHand);
						}
						handStep = ApplicationWorkbenchAdvisor.getHandStep();
						if (handStep > 0 && hand != null) {
							try {
								Player player = hand.getAction(handStep - 1).getPlayer();
								if (player != null) {
									name = player.getName();
								} else {
									name = ""; //$NON-NLS-1$
								}
							} catch (ActionIllegalActionException e) {
								ErrorHandler.handleError(e, Messages.ViewTable_21, false);
							}
						}
						try {
							int action = ApplicationWorkbenchAdvisor.getHands()
									.get(ApplicationWorkbenchAdvisor.getActiveHand()).getAction(handStep).getAction();
							if (action != Action.ANTE) {
								Thread.sleep(delay);
							}
						} catch (InterruptedException e) {
							ErrorHandler.handleError(e, Messages.ViewTable_22, false);
						} catch (ActionIllegalActionException e) {
							ErrorHandler.handleError(e, Messages.ViewTable_23, false);
						}
					} while (!name.equals(ownName) && handStepOld != handStep);
				}
			}
		}.start();

	}

	/**
	 * Spielt eine Hand bis zu deren Ende ab oder unterbricht die Ausfuehrung
	 */
	public void doAutoplay() {
		// einen Thraed anlegen fuer den Autoplay
		if (thread == null) {
			thread = new Thread("rjPokerAutoPlay") { //$NON-NLS-1$
				public void run() {
					// Wartezeit zwischen zwei Schritten ermitteln
					int delay = 1000 * Activator.getDefault().getPreferenceStore()
							.getInt(PreferenceConstants.P_GENERAL_TIME_DELAY);
					
					int handStepOld = 0;
					int handStep = 0;
					
					do {
						// aktuellen Schritt in der Hand ermitteln
						handStepOld = ApplicationWorkbenchAdvisor.getHandStep();
						
						// und synchron wegen der Anzeige den naechsten Schritt ausfuehren
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								doNextStep();
							}
						});
						
						// den jetzt aktuelln Schritt merken
						handStep = ApplicationWorkbenchAdvisor.getHandStep();
						
						// die vorgegebene Zeit warten
						try {
							int action = ApplicationWorkbenchAdvisor.getHands().get(ApplicationWorkbenchAdvisor.getActiveHand()).getAction(handStep).getAction();
							if (action != Action.ANTE) {
								Thread.sleep(delay);
							}
						} catch (InterruptedException e) {
							ErrorHandler.handleError(e,
									Messages.ViewTable_24, false);
						} catch (ActionIllegalActionException e) {
							ErrorHandler.handleError(e,
									Messages.ViewTable_25, false);
						}
						
						// und solange ein neuer Schritt ausgefuehrt wurde, den naechsten ausfuehren
					} while (handStepOld != handStep && ApplicationWorkbenchAdvisor.isAutoplay());
				}
			};
		}
		
		// wenn kein Autoplay laeuft und eine Hand ausgewaehlt wurde
		if (thread != null && !ApplicationWorkbenchAdvisor.isAutoplay() && ApplicationWorkbenchAdvisor.getActiveHand() != -1) {
		
			// Status fuer Autoplay passend setzen
			ApplicationWorkbenchAdvisor.setAutoplay(true);
			
			// den Autoplay in einem neuen Thread starten
			thread.start();
		} else {
			// Thread fuer Autoplay stoppen
			ApplicationWorkbenchAdvisor.setAutoplay(false);
			
			// Thread loeschen, noetig falls dieser neu gestartet wird
			thread = null;
		}
	}
	
	/**
	 * Gibt die aktuell im Board liegenden Karten zurueck
	 * 
	 * @return Die im Board bereits gezeigten Karten
	 */
	public Card[] getActualBoard() {
		return boardcards;
	}
	
	/**
	 * Gibt den Tisch mit den akuellen Informationen zurueck
	 * 
	 * @return
	 */
	public Table getTable() {
		return table;
	}
	
//	public void dispose() {
//		super.dispose();
////		IMenuManager mgr = getViewSite().getActionBars().getMenuManager();
////		IToolBarManager tool = getViewSite().getActionBars().getToolBarManager();
//		IViewSite view = getViewSite();
//		IActionBars bars = view.getActionBars();
//		IAction action = bars.getGlobalActionHandler(ActionFactory.IMPORT.getId());
//		IMenuManager menu = bars.getMenuManager();
//		IServiceLocator service = bars.getServiceLocator();
//		IStatusLineManager line = bars.getStatusLineManager();
//		IToolBarManager tool = bars.getToolBarManager();
//		Object obj = service.getService(IWorkbenchWindow.class);
//		IWorkbenchWindow win = (IWorkbenchWindow)obj;
////		Object obj2 = win.getService(IActionb.class);
//		IAction myAction = getViewSite().getActionBars().getGlobalActionHandler(ActionFactory.IMPORT.getId()); 
////		bars.set
//		System.out.println("");
//	}
	
//	private void testGetAction() {
//		IAction myAction = getViewSite().getActionBars() 
//        .getGlobalActionHandler(ActionFactory.IMPORT.getId());
//		System.out.println("myAction:" + myAction);
//		IToolBarManager myToolBarManager = getViewSite().getActionBars().getToolBarManager();
////		System.out.println("myToolBarManager:" + myToolBarManager);
//		System.out.println("myToolBarManager.isEmpty()=" + myToolBarManager.isEmpty());
//		IMenuManager myMenuManager = getViewSite().getActionBars().getMenuManager(); 
////		System.out.println("myMenuManager:" + myMenuManager);
//		System.out.println("myMenuManager.isEmpty()=" + myMenuManager.isEmpty());
//	}
}