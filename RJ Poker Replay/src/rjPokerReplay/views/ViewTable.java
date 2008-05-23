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

package rjPokerReplay.views;

import java.util.ArrayList;

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
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.ViewPart;

import rjPokerReplay.Activator;
import rjPokerReplay.ApplicationWorkbenchAdvisor;
import rjPokerReplay.Constants;
import rjPokerReplay.preferences.PreferenceConstants;
import rjPokerReplay.util.ErrorHandler;
import cardstuff.Action;
import cardstuff.Card;
import cardstuff.Hand;
import cardstuff.Handhistory;
import cardstuff.Player;
import cardstuff.Pot;
import cardstuff.Table;
import cardstuffExceptions.ActionIllegalActionException;
import cardstuffExceptions.CardException;
import cardstuffExceptions.PlayerException;

public class ViewTable extends ViewPart {
	public static final String ID = "RjPokerReplay.ViewTable";

	// Zeichenfläsche
	private Canvas canvas;
	
	// Hintergrundbild
	private Image background;
	
	// Vorlage für original Hintergrundbild
	private ImageData background_org;
	
	// Vorlage für größenangepaßtes Hintergundbild
	private ImageData background_scal;
	
	// Daten für Bildumwanldung
	private ImageData imgData;
	
	// gemerkte Breite des Zeichenfensters 
	private int canvasWidht;
	
	// gemerkte Höhe des Zeichenfensters
	private int canvasHeight;
	
	// Zeichenfläche für den tisch
	private GC gcTable;
	
	// Einsätze der Spieler
	private double bet[] = new double[10];
	
	// Kennzeichen für Preflop, Flop, Turn oder River
	private int status = 0;
	
	// aktueller Tisch
	private Table table = null;
	
	// Der akuelle Display
	private Display display;
	
	// Feld mit den Positionen der Spieler am Tisch
	private ArrayList<Rectangle> playerPos;
	
	// Feld mit der Position des Buttons am Tisch
	private ArrayList<Rectangle> buttonPos;
	
	// Feld mit den Positionen der Boardkarten am Tisch
	private ArrayList<Rectangle> boardCardPos;
	
	// Bild des Buttons
	private Image button;
	
	// Thread für den Autoplay
	private static Thread thread;
	
	// der Pot
	Pot pot = new Pot();

	/**
	 * Zeichnet den Pokertsich
	 */
	public void createPartControl(Composite parent) {

		// Display besorgen
		display = Display.getCurrent();

		// Zeichenfläche mit Layout
		canvas = new Canvas(parent, SWT.BACKGROUND);
		canvas.setLayout(null);

		// Hintergundbild mit Pokertisch in der original Größe
		background_org = ApplicationWorkbenchAdvisor.getImageStore().get(
				Constants.IMG_BACKGROUND).getImageData();

		// Größe der Zeichenfläche
		int width = canvas.getParent().getShell().getBounds().width;
		int height = canvas.getParent().getShell().getBounds().height;

		// Hintergrund auf passende Größe bringen
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
					// Bei Größenänderung den Tisch neu zeichnen
					drawTable();
				}
			}
		});
	}

	/**
	 * Zeichnete den Tisch
	 */
	public void drawTable() {

		// Größe der Zeichenfläche ermitteln
		Rectangle client = canvas.getClientArea();
		int width = client.width;
		int height = client.height;

		// Bestimmte Dinge müssen nur bei einer Änderung der Größe durchgeführt
		// werden
		if (canvasHeight != height || canvasWidht != width) {
			// aktuelle Größe der Zeichenfläsche merken
			canvasWidht = canvas.getBounds().width;
			canvasHeight = canvas.getBounds().height;

			// Bild auf die passende größe bringen und als Hintergund verwenden
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

		// Hintergrund neue anzeigen
		gcTable.dispose();
		background.dispose();
		background = new Image(display, background_scal);
		gcTable = new GC(background);
		canvas.setBackgroundImage(background);

		// Prüfen ob der Tisch Daten enthält
		if (table != null && table.getPokerroom() != 0) {
			// Button anzeigen
			int posOfButton = 0;
			if (table != null) {
				posOfButton = table.getButtonposition();
			}
			if (posOfButton != 0) {
				gcTable.drawImage(button, buttonPos.get(posOfButton - 1).x,
						buttonPos.get(posOfButton - 1).y);
			}

			// Spieler an Tisch setzen
			for (int i = 0; i < 10; i++) {
				if (table.getPlayers()[i] != null) {
					Player player = table.getPlayers()[i];
					int textX = playerPos.get(i + 1).x;
					int textY = playerPos.get(i + 1).y;
					int lineSpacing2 = 20;
					// an dieser Postion sitzt auch wirklich ein Spieler

					// Name des Spielers
					gcTable.drawText(player.getName(), textX, textY, true);
					textY = textY + lineSpacing2;

					// Stack
					double stack = player.getChips();
					String text;
					if (stack == 0) {
						// Spieler ist All-In, dann Text statt Betrag anzeigen
						text = "All-In";
					} else {
						// Spieler hat nach Geld, dann das anzeigen
						text = String.valueOf(stack);
					}
					gcTable.drawText(text, textX,
							textY, true);
					textY = textY + lineSpacing2;

					// Hinweis wenn der Spieler nicht aktiv ist
					if (player.getState() != Player.AKTIV) {
						gcTable.drawText("Is not active", textX, textY, true);
						textY = textY + lineSpacing2;

					}

					// der aktuelle Einsatz
					String einsatz = "Ante: ";
					einsatz = einsatz + String.valueOf(bet[i]);
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
			int potY = (int) (canvasHeight / 2.0 - 20.0);
			int potX = (int) (canvasWidht / 2.0 - 10.0);
			String potText = "Pot: " + String.valueOf(pot.getPotSize());
			gcTable.drawText(potText, potX, potY, true);
		}
	}

	/**
	 * Zeigt den River Zusätzlich werden Flop und Turn angezeigt
	 */
	private void showRiver() {
		showFlop();
		showTurn();
		if (table.getBoard()[4] != null) {
			showCard(table.getBoard()[4], boardCardPos.get(4).x, boardCardPos.get(4).y);
		}
	}

	/**
	 * Zeigt den Turn Zusätzlich wird der Flop angezeigt
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

				// Karte auf passende Größe bringen
				imgData = imgData.scaledTo(54, 72);
				Image card2 = new Image(Display.getCurrent(), imgData);

				// und anzeigen
				gcTable.drawImage(card2, x, y);
			} catch (CardException e) {
				ErrorHandler.handleError(e,
						"Fehler beim Holen eines Kartenbildes.", false);
			}
		}
	}

	/**
	 * Position der einzelnen Spieler festlegen
	 * 
	 * @param countPlayer
	 *            Anzahl der Spieler
	 * @param height
	 *            Höhe der Grafik
	 * @param width
	 *            Breite der Grafik
	 * @return Eine Liste mit den Positionen der Spieler
	 */
	private static ArrayList<Rectangle> getPlayPosition(int countPlayer,
			int height, int width) {
		// Position und Größe der Felder für die Spieler
		ArrayList<Rectangle> playerPos = new ArrayList<Rectangle>();

		// Höhe und Breite der Spielerfelder
		int hoehe = 100;
		int breite = 75;

		// Dealer
		int posHi = (int) (height / 4 - hoehe);
		int posRi = (int) (width / 2 - breite / 2);
		playerPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 1
		posHi = (int) (height / 3.7 - hoehe);
		posRi = (int) (width - width / 3 - breite / 2);
		playerPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 2
		posHi = (int) (height / 3 - hoehe);
		posRi = (int) (width - width / 6 - breite / 2);
		playerPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 3
		posHi = (int) (height - height / 2 + hoehe);
		posRi = (int) (width - width / 6 - breite / 2);
		playerPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 4
		posHi = (int) (height - height / 2.5 + hoehe);
		posRi = (int) (width - width / 3 - breite / 2);
		playerPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 5
		posHi = (int) (height - height / 2.7 + hoehe);
		posRi = (int) (width / 2 - breite / 2);
		playerPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 6
		posHi = (int) (height - height / 2.5 + hoehe);
		posRi = (int) (width / 3 - breite / 2);
		playerPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 7
		posHi = (int) (height - height / 2 + hoehe);
		posRi = (int) (width / 6 - breite / 2);
		playerPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 8
		posHi = (int) (height / 3 - hoehe);
		posRi = (int) (width / 6 - breite / 2);
		playerPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 9
		posHi = (int) (height / 3.7 - hoehe);
		posRi = (int) (width / 3 - breite / 2);
		playerPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		return playerPos;
	}
	
	private static ArrayList<Rectangle> getBoardCardPos(int height, int width) {
		// Position und Größe der Felder für die Spieler
		ArrayList<Rectangle> cardPos = new ArrayList<Rectangle>();
		
		// Höhe und Breite der Karten
		int hoehe = 96;
		int breite = 72;
		
		// 1. Karte Flop
		int posHi = (int) (height / 2 );
		int posRi = (int) (width / 2 - breite * 2.5);
		cardPos.add(new Rectangle(posRi, posHi, breite, hoehe));
		
		// 2. Karte Flop
		posRi = (int) (width / 2 - breite * 1.5);
		cardPos.add(new Rectangle(posRi, posHi, breite, hoehe));
		
		// 3. Karte Flop
		posRi = (int) (width / 2 - breite * 0.5);
		cardPos.add(new Rectangle(posRi, posHi, breite, hoehe));
		
		// 4. Karte Turn
		posRi = (int) (width / 2 + breite * 0.5);
		cardPos.add(new Rectangle(posRi, posHi, breite, hoehe));
		
		// 5. Karte River
		posRi = (int) (width / 2 + breite * 1.5);
		cardPos.add(new Rectangle(posRi, posHi, breite, hoehe));
		
		return cardPos;
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		canvas.setFocus();
	}

	/**
	 * Überträgt eine Aktion an den aktuellen Tisch
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
						"Fehler beim Ermitteln es Spielers am Tisch.", false);
			}
		}

		// Die verschiedenen Aktionen
		double pay = 0.0;
		switch (action.getAction()) {
		case Action.DEAL:
			// Karten werden verteilt
			try {
				// alle Spieler ablaufen
				for (int i = 0; i < 10; i++) {
					Player player2 = table.getPlayers()[i];

					// sizt ein Spieler an dieser Position
					if (player2 != null) {
						// ja, ist es der aktive Spieler
						if (player.equal(player2)) {
							// ja, dann die Karte anzeigen
							player.addPocketCard((Card) action.getValue());
						} else {
							// nein, dann eine verdeckte Karte zeigen
							table.getPlayers()[i].addPocketCard(new Card(
									Card.TWO, Card.BACK));
						}
					}
				}
			} catch (PlayerException e) {
				ErrorHandler.handleError(e,
						"Fehler beim Ermitteln des Spielers.", false);
			} catch (CardException e) {
				ErrorHandler.handleError(e,
						"Fehler beim Ermitteln der Poketcards.", false);
			}
			break;
		case Action.SMALLBLIND:
			// Spieler zahl small Blind
			pay = pot.pay(player, table.getSmallblind());

			// Einsatz vermerken
			bet[pos] = pay;
			break;
		case Action.BIGBLIND:
			// Spieler zahl big Blind
			pay = pot.pay(player, table.getBigblind());
			
			// Einsatz vermerken
			bet[pos] = pay;
			break;
		case Action.ANTE:
			// Spieler zahl ante
			pay = pot.pay(player, (Double) action.getValue());

			// Einsatz vermerken
			bet[pos] = pay;
			break;
		case Action.BET:
			pay = pot.pay(player, (Double) action.getValue());

			// Einsatz vermerken
			bet[pos] = pay;
			break;
		case Action.CALL:
			// ein Spieler called den einsatz
			pay = pot.pay(player, (Double) action.getValue());

			// Einsatz vermerken
			bet[pos] = bet[pos] + pay;
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

			// Einsatz auf 0 setzen
			bet[pos] = 0.0;
			break;
		case Action.RAISE:
			// Spieler erhöht
			pay = pot.pay(player, (Double) action.getValue() - bet[pos]);

			// Einsatz vermerken
			bet[pos] = bet[pos] + pay;
			break;
		case Action.FLOP:
			// merken das der Flop gezeitgt wird
			status = 1;

			// Einsätze auf 0 setzen
			resetEinsatz();
			
			
			// es beginnt die nächste Setzrunde
			pot.roundsEnd();
			break;
		case Action.TURN:
			// merken das der Turn gezeitgt wird
			status = 2;

			// Einsätze auf 0 setzen
			resetEinsatz();
			
			// es beginnt die nächste Setzrunde
			pot.roundsEnd();
			break;
		case Action.RIVER:
			// merken das der River gezeitgt wird
			status = 3;

			// Einsätze auf 0 setzen
			resetEinsatz();
			
			// es beginnt die nächste Setzrunde
			pot.roundsEnd();
			break;
		case Action.COLLECT:
			// Spieler bekommt den Pot
			
			// zur Sicherheit die aktuelle Setzrunde beenden
			pot.roundsEnd();
			
			// Beträge auszahlen
			try {
				// Spieler seinen Anteil am Pot zuweisen
				player.deposit(pot.payout((Double) action.getValue()));
//				player.deposit((Double) action.getValue());
				
				// falls noch ein Einsatz offen ist, auch dem zuweisen
				player.deposit(pot.payout(bet[pos]));
//				player.deposit(bet[pos]);
				
				// Einsätze auf 0 setzen
				bet[pos] = 0.0;
			} catch (PlayerException e) {
				ErrorHandler.handleError(e,
						"Fehler beim verteilen des Einsatzes.", false);
			}
			break;
		case Action.SHOW:
			// ein Spieler zeigt seine Karten
			Card card1 = (Card) action.getValue();
			// Zeiger auf nächste Aktion stellen
			ApplicationWorkbenchAdvisor.nextHandStep();

			// zweite Karte ermitteln
			Hand hand = null;
			int activeHand = ApplicationWorkbenchAdvisor.getActiveHand();
			if (activeHand > -1) {
				hand = ApplicationWorkbenchAdvisor.getHands().get(activeHand);
			}
			int handStep = ApplicationWorkbenchAdvisor.getHandStep();
			
			// Deckblätter gegen echte Karten tauschen
			try {
				Card card2 = (Card) hand.getAction(handStep - 1).getValue();
				player.replacePoketCards(card1, card2);
			} catch (PlayerException e) {
				ErrorHandler.handleError(e,	"Fehler beim Zeigen der Karten", false);
			} catch (ActionIllegalActionException e) {
				ErrorHandler.handleError(e,	"Fehler beim Zeigen der Karten", false);
			}
			break;
		case Action.MUCK:
			// Spieler zeigt seine Karten am Roundenende nicht
			if (player != null) {
				// Karten nicht mehr anzeigen
				player.fold();
			}

			// Einsatz auf 0 setzen
			bet[pos] = 0.0;
			break;
		}

		// Tischinformationen anzeigen
		showTableInfo();
	}

	/**
	 * Zeigt die Tischinformationen im entsprechenden View an
	 */
	private void showTableInfo() {
		IWorkbenchPage page = getViewSite().getWorkbenchWindow().getActivePage();
		ViewTableinfo view = null;
		if (page != null) {
			view = (ViewTableinfo)(page.findView(ViewTableinfo.ID));
		}
		if (view != null) {
			view.showInfo();
		}
	}

	/**
	 * Setzt alle Einsätze auf Null
	 */
	private void resetEinsatz() {
		for (int i = 0; i < 10; i++) {
			bet[i] = 0.0;
		}
	}

	/**
	 * Versetzt den Tisch in den Anfangszustand
	 */
	public void resetTable() {
		// aktuellen Pot zurücksetzen
		pot.clear();

		// Einsätze auf Null setzen
		resetEinsatz();

		// Status für Flop/Turn/River auf Angang setzen
		status = 0;

		// Boardkarten löschen
		if (table != null) {
			table.removeBord();
		}
	}

	/**
	 * Position der einzelnen Spieler festlegen
	 * 
	 * @param countPlayer
	 *            Anzahl der Spieler
	 * @param height
	 *            Höhe der Grafik
	 * @param width
	 *            Breite der Grafik
	 * @return Eine Liste mit den Positionen der Spieler
	 */
	private static ArrayList<Rectangle> getButtonPosition(int countPlayer,
			int height, int width) {
		// Position und Größe der Felder für die Spieler
		ArrayList<Rectangle> buttonPos = new ArrayList<Rectangle>();

		// Höhe und Breite der Spielerfelder
		int hoehe = 15;
		int breite = 15;

		// Dealer
		int posHi = (int) (height / 4 - hoehe) + 60;
		int posRi = (int) (width / 2 - breite / 2);
		buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 1
		posHi = (int) (height / 3.7 - hoehe + 60);
		posRi = (int) (width - width / 3 - breite / 2);
		buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 2
		posHi = (int) (height / 3 - hoehe + 60);
		posRi = (int) (width - width / 6 - breite / 2 - 40);
		buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 3
		posHi = (int) (height - height / 2 + hoehe + 50);
		posRi = (int) (width - width / 6 - breite / 2 - 40);
		buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 4
		posHi = (int) (height - height / 2.5 + hoehe + 25);
		posRi = (int) (width - width / 3 - breite / 2);
		buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 5
		posHi = (int) (height - height / 2.7 + hoehe + 20);
		posRi = (int) (width / 2 - breite / 2);
		buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 6
		posHi = (int) (height - height / 2.5 + hoehe + 25);
		posRi = (int) (width / 3 - breite / 2);
		buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 7
		posHi = (int) (height - height / 2 + hoehe + 50);
		posRi = (int) (width / 6 - breite / 2 + 40);
		buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 8
		posHi = (int) (height / 3 - hoehe + 60);
		posRi = (int) (width / 6 - breite / 2 + 35);
		buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		// Spieler 9
		posHi = (int) (height / 3.7 - hoehe + 60);
		posRi = (int) (width / 3 - breite / 2);
		buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));

		return buttonPos;
	}

	/**
	 * Sprigt zum Anfang der Hand wenn möglich, sonst wenn möglich zur
	 * vorherigen
	 */
	public void doHandBack() {
		// aktive Hand und Handstep holen
		int activeHand = ApplicationWorkbenchAdvisor.getActiveHand();
		int handStep = ApplicationWorkbenchAdvisor.getHandStep();

		// befinden wir uns am Anfang der Hand
		if (handStep < 1) {
			// Ja prüfen ob es sich um die erste Hand handelt
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

		// Tisch zurücksetzen
		ApplicationWorkbenchAdvisor
				.setTable((Table) ApplicationWorkbenchAdvisor.getHands().get(
						activeHand));

		// Tisch auf Anfang stellen
		resetTable();

		// Tisch mit Änderungen neu anzeigen
		drawTable();

		// Handhistorie anzeigen
		updateHandView();

		// Tischinformationen anzeigen
		showTableInfo();
	}

	/**
	 * Geht, wenn möglich, einen Schritt zurück
	 */
	public void doStepBack() {
		// Anzahl der bisher gemachten Schritte holen
		int step = ApplicationWorkbenchAdvisor.getHandStep();

		// Tisch auf Start setzen

		doHandBack();

		// und genau eine Schritt weniger als bisher gemacht ausführen
		for (int i = 1; i < step; i++) {
			doNextStep();
		}
	}

	/**
	 * Springt wenn möglich zur nächsten Hand
	 */
	public void doNextHand() {
		// Zeiger auf nächste Hand setzen
		ApplicationWorkbenchAdvisor.nextHand();

		// Tisch zurück setzen
		resetTable();

		// und frischen Tisch zeichnen
		drawTable();

		// Handhistorie anzeigen
		updateHandView();

		// Tischinformationen anzeigen
		showTableInfo();
	}

	/**
	 * Führt den nächten Schritt aus
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
		
		// letzen ausgeführten Step merken
		int oldHandStep = ApplicationWorkbenchAdvisor.getHandStep();
		
		// Zeiger auf nächste Aktion stellen
		ApplicationWorkbenchAdvisor.nextHandStep();

		// neuen Step holen
		int handStep = ApplicationWorkbenchAdvisor.getHandStep();
		
		// Aktion in Tisch übernehmen
		if (handStep > 0 && oldHandStep != handStep) {
			try {
				doAction(hand.getAction(handStep - 1));
			} catch (ActionIllegalActionException e) {
				doNextHand();
			}
		}

		// Tisch mit Änderungen neu anzeigen
		drawTable();
	}

	/**
	 * Zeigt geänderten Handverlauf an
	 */
	public void updateHandView() {
		IWorkbenchPage page = getViewSite().getWorkbenchWindow().getActivePage();
		ViewHandhistory view = null;
		if (page != null) {
			view = (ViewHandhistory)(page.findView(ViewHandhistory.ID));
		}
		if (view != null) {
			view.showHand(ApplicationWorkbenchAdvisor.getHands().get(ApplicationWorkbenchAdvisor.getActiveHand()));
		}
	}

	/**
	 * Spielt die Hand bis zur nächten Aktion des Spielers ab
	 */
	public void doNextOwnAction() {
		new Thread() {
			public void run() {
				int delay = 1000 * Activator.getDefault().getPreferenceStore()
						.getInt(PreferenceConstants.P_GENERAL_TIME_DELAY);
				String name = "";
				String ownName = "";

				switch (ApplicationWorkbenchAdvisor.getTable().getPokerroom()) {
				case Handhistory.POKERSTARS:
					ownName = Activator.getDefault().getPreferenceStore()
							.getString(PreferenceConstants.P_POKERSTARS_PLAYER);
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
					int activeHand = ApplicationWorkbenchAdvisor
							.getActiveHand();
					if (activeHand > -1) {
						hand = ApplicationWorkbenchAdvisor.getHands().get(
								activeHand);
					}
					handStep = ApplicationWorkbenchAdvisor.getHandStep();
					if (handStep > 0 && hand != null) {
						try {
							Player player = hand.getAction(handStep - 1).getPlayer();
							if (player != null) {
								name = player.getName();
							} else {
								name = "";
							}
						} catch (ActionIllegalActionException e) {
							ErrorHandler.handleError(e, "Fehler beim Autoplay",
									false);
						}
					}
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						ErrorHandler.handleError(e,
								"Fehler beim Warten in Autoplay", false);
					}
				} while (!name.equals(ownName) && handStepOld != handStep);
			}
		}.start();

	}

	/**
	 * Spielt eine Hand bis zu deren Ende ab oder unterbricht die Ausführung
	 */
	public void doAutoplay() {
		// einen Thraed anlegen für den Autoplay
		if (thread == null) {
			thread = new Thread() {
				public void run() {
					// Wartezeit zwischen zwei Schritten ermitteln
					int delay = 1000 * Activator.getDefault().getPreferenceStore()
							.getInt(PreferenceConstants.P_GENERAL_TIME_DELAY);
					
					int handStepOld = 0;
					int handStep = 0;
					
					do {
						// aktuellen Schritt in der Hand ermitteln
						handStepOld = ApplicationWorkbenchAdvisor.getHandStep();
						
						// und synchron wegen der Anzeige den nächsten Schritt ausführen
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								doNextStep();
							}
						});
						
						// den jetzt aktuelln Schritt merken
						handStep = ApplicationWorkbenchAdvisor.getHandStep();
						
						// die vorgegebene Zeit warten
						try {
							Thread.sleep(delay);
						} catch (InterruptedException e) {
							ErrorHandler.handleError(e,
									"Autoplay wurde ungeplant unterbrochen.", false);
						}
						
						// und solange ein neuer Schritt ausgeführt wurde, den nächsten ausführen
					} while (handStepOld != handStep && ApplicationWorkbenchAdvisor.isAutoplay());
				}
			};
		}
		
		// wenn kein Autoplay läuft und eine Hand ausgewählt wurde
		if (thread != null && !ApplicationWorkbenchAdvisor.isAutoplay() && ApplicationWorkbenchAdvisor.getActiveHand() != -1) {
		
			// Status für Autoplay passend setzen
			ApplicationWorkbenchAdvisor.setAutoplay(true);
			
			// den Autoplay in einem neuen Thread starten
			thread.start();
		
			
		} else {
			// Thread für Autoplay stoppen
			ApplicationWorkbenchAdvisor.setAutoplay(false);
			
			// Thread löschen, nötig falls dieser neu gestartet wird
			thread = null;
		}
	}

}