package rjPokerReplay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.ViewPart;

import cardstuff.Action;
import cardstuff.Card;
import cardstuff.Hand;
import cardstuff.Player;
import cardstuff.Table;
import cardstuffExceptions.ActionIllegalActionException;
import cardstuffExceptions.CardException;
import cardstuffExceptions.PlayerException;

public class View extends ViewPart {
	public static final String ID = "RjPokerReplay.view";

	private Canvas canvas;
	private Image background;
	private ImageData imgData;
	private int canvasWidht;
	private int canvasHeight;
	private GC gcTable;
	private Button handStart;
	private Button stepBack;
	private Button stepForward;
	private Button handNext;
	private Button nextAction;
	private Button autoPlay;
	private double bet[] = new double[10];
	private int status = 0;
	private String actionText = "";
	// Der Pot am Ende einer Aktion
	private double currentPot = 0.0;
	private Table table = null;
	private double currentBet = 0.0;
	

	/**
	 * Zeichnet den Pokertsich
	 */
	public void createPartControl(Composite parent) {
		
		// Zeichenfläche mit Layout
		canvas = new Canvas(parent, SWT.BACKGROUND);
		canvas.setLayout(null);
		
		// Hintergundbild mit Pokertisch
		imgData = new ImageData(View.class.getClassLoader().getResourceAsStream("images/table.jpg")); //$NON-NLS-1$
		
		// Größe der Zeichenfläche
		int width = canvas.getParent().getShell().getBounds().width;
		int height = canvas.getParent().getShell().getBounds().height;
		
		// Hintergrund auf passende Größe bringen
		imgData = imgData.scaledTo(width, height);
		
		// und Bild als Hintergrund verwenden
        background = new Image(Display.getCurrent(), imgData);
        gcTable = new GC(background);
        canvas.setBackgroundImage(background);
        
        // Wenn das Bild neu gezeichnet werden soll 
 		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				if (canvasHeight != canvas.getBounds().height || canvasWidht != canvas.getBounds().width) {
					// Bei Größenänderung den Tisch neu zeichnen 
					drawTable();
				}
			}
 		}); 
	}

	/**
	 * Zeichnete den Tisch
	 */
	public void drawTable(){
		
		// Display besorgen
		Display display = Display.getCurrent();
		
		// Hintergundbild mit Tisch laden
		imgData = new ImageData(View.class.getClassLoader().getResourceAsStream("images/table.jpg"));
		
		// Größe der Zeichenfläche ermitteln
		Rectangle client = canvas.getClientArea();
		int width = client.width;
		int height = client.height;
		
		// Bild auf die passende größe bringen und als Hintergund verwenden
        imgData = imgData.scaledTo(width, height);
        background = new Image(display, imgData);
        gcTable = new GC(background);
        canvas.setBackgroundImage(background);
        
        // aktuelle Größe der Zeichenfläsche merken
        canvasWidht = canvas.getBounds().width;
        canvasHeight = canvas.getBounds().height;
        
        // Die Drucktasten für die einzelnen Benutzeraktionen einfügen
       	getButtons(display);

        // Position der einzelnen Spieler am Tisch ermitteln
        ArrayList<Rectangle> playerPos = getPlayPosition(9, height, width);
        
        // aktuellen Tisch holen
        if (ApplicationWorkbenchAdvisor.getTable() != null && ApplicationWorkbenchAdvisor.getHandStep() == 0) {
        	table = ApplicationWorkbenchAdvisor.getTable().copy();
        }
        
        // Prüfen ob der Tisch Daten enthält
        if (table != null && table.getPokerroom() != 0) {
        	// Variablen für die Textposition
        	int colum = 0;
        	int row = 0;
        	int linespacing = 20;
        	
        	// ja, dann Tisch anzeigen
        	// Tischinformation wie Name, Spielnummer oder Datum
        	String text = "Pokerroom: " + table.getPokerroomText();
        	text = text + " " + table.getGame();
        	text = text + " " + table.getGameTypeText();
        	text = text + " " + table.getTabletypeText();
        	gcTable.drawText(text, colum, row, true);
        	row = row + linespacing;
        	
        	text = "Blinds: " + table.getSmallblind() + "/" + table.getBigblind();
        	text = text + " " + table.getCountSeats() + "-max";
        	gcTable.drawText(text, colum, row, true);
        	row = row + linespacing;

        	text = "Table: " + table.getTitel();
        	SimpleDateFormat df = new SimpleDateFormat( "dd.MM.yyyy - HH:mm:ss" );
        	text = text + " " + df.format(table.getDate().getTime());
        	text = text + " Rake: " + String.valueOf(table.getRake());
        	gcTable.drawText(text, colum, row, true);
        	row = row + linespacing;
        	
        	// letzte Aktion
        	gcTable.drawText(actionText, colum, row, true);
        	row = row + linespacing;
        	
        	// Spieler an Tisch setzen
        	for (int i = 0; i < 10; i++) {
        		if (table.getPlayers()[i] != null) {
        			Player player = table.getPlayers()[i];
        			int textX = playerPos.get(i + 1).x;
        			int textY = playerPos.get(i + 1).y;
        			int lineSpacing2 = 20;
        			// an dieser Postion sitzt auch wirklich ein Spieler
        			// Rahmen um den Spieler zeichnen
//        			gcTable.drawRectangle(playerPos.get(i + 1));
        			
        			// Name des Spielers
        			gcTable.drawText(player.getName(), textX, textY, true);
        			textY = textY + lineSpacing2;
        			
        			// Stack
        			gcTable.drawText(String.valueOf(player.getChips()), textX, textY, true);
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
    		int potX = (int)(canvasHeight / 1.6);
    		int potY = (int)(canvasWidht / 3.5);
    		String potText = "Pot: " + String.valueOf(currentPot);
    		gcTable.drawText(potText, potX, potY, true);
    		
    		// Bild des Buttons laden
    		ArrayList<Rectangle> buttonPos = getButtonPosition(9, height, width);
    		imgData = new ImageData(View.class.getClassLoader().getResourceAsStream("images/button_dealer.gif"));
    		imgData = imgData.scaledTo(buttonPos.get(0).width, buttonPos.get(0).height);
            Image button = new Image(display, imgData);
    		
    		// Button anzeigen
    		int posOfButton = table.getButtonposition();
    		if (posOfButton != 0) {
    			gcTable.drawImage(button, buttonPos.get(posOfButton - 1).x, buttonPos.get(posOfButton - 1).y);
    		}
    		
        }
	}

	/**
	 * Zeigt den River
	 * Zusätzlich werden Flop und Turn angezeigt
	 */
	private void showRiver() {
		showFlop();
		showTurn();
		if (table.getBoard()[4] != null) {
			showCard(table.getBoard()[4], 590, 325);
		}
	}
	
	/**
	 * Zeigt den Turn
	 * Zusätzlich wird der Flop angezeigt
	 * 
	 * @param table Der Tisch zu dem der Turn gezeigt wird
	 */
	private void showTurn() {
		showFlop();
		if (table.getBoard()[3] != null) {
			showCard(table.getBoard()[3], 530, 325);
		}
	}

	/**
	 * Zeigt den Flop
	 */
	private void showFlop() {
		if (table.getBoard()[0] != null && table.getBoard()[1] != null && table.getBoard()[2] != null) {
			showCard(table.getBoard()[0], 350, 325);
			showCard(table.getBoard()[1], 410, 325);
			showCard(table.getBoard()[2], 470, 325);
		}
	}

	/**
	 * Zeit eine Karte am Tisch
	 * 
	 * @param card Die Karte
	 * @param x Die X-Koordinate an der die Karte gezeigt werden soll
	 * @param y Die Y-Koordinate an der die Karte gezeigt werden soll
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
				ErrorHandler.handleError(e, "Fehler beim Holen eines Kartenbildes.", false);
			}
		}
	}

	/**
	 * Zeigt die verschiedenen Drucktasten für die Interaktion an
	 * 
	 * @param display Der Display für die Anzeige
	 */
	private void getButtons(Display display) {
		// Größe der Buttons
		int buttonWidth = 30;
		int buttonHeight = 30;
		
		// Positon der Buttons
		int buttonSpacing = 50;
		int buttonYPos = canvasHeight - 50;
		int buttonXPos = 20;
		
		// Größe des Bildes auf den Drucktasten
		int iconWidth = buttonWidth - 10;
		int iconHeight = buttonHeight -10; 
		
		// Taste um an den Anfang der Hand zu springen
		handStart = new Button(canvas, SWT.PUSH);
        handStart.setBounds(new Rectangle(buttonXPos, buttonYPos, buttonWidth, buttonHeight));
        handStart.setToolTipText("Back to handstart");
        handStart.setVisible(true);
        imgData = new ImageData(View.class.getClassLoader().getResourceAsStream("icons/player_start.png")); //$NON-NLS-1$
        imgData = imgData.scaledTo(iconWidth, iconHeight);
        handStart.setImage(new Image(display, imgData));
        handStart.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                doHandBack();
            }
        });
        buttonXPos = buttonXPos + buttonSpacing;
        
        // Taste für einen Schritt zurück
        stepBack = new Button(canvas, SWT.PUSH);
        stepBack.setBounds(new Rectangle(buttonXPos, buttonYPos, buttonWidth, buttonHeight));
        stepBack.setToolTipText("Step back");
        stepBack.setVisible(true);
        imgData = new ImageData(View.class.getClassLoader().getResourceAsStream("icons/player_rew.png")); //$NON-NLS-1$
        imgData = imgData.scaledTo(iconWidth, iconHeight);
        stepBack.setImage(new Image(display, imgData));
        stepBack.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
            	doStepBack();
            }
        });
        buttonXPos = buttonXPos + buttonSpacing;
        
        // Drucktaste für den nächsten Schritt
        stepForward = new Button(canvas, SWT.PUSH);
        stepForward.setBounds(new Rectangle(120, buttonYPos, buttonWidth, buttonHeight));
        stepForward.setToolTipText("Step forward");
        stepForward.setVisible(true);
        imgData = new ImageData(View.class.getClassLoader().getResourceAsStream("icons/player_fwd.png")); //$NON-NLS-1$
        imgData = imgData.scaledTo(iconWidth, iconHeight);
        stepForward.setImage(new Image(display, imgData));
        stepForward.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
            	doNextStep();
            }
        });
        buttonXPos = buttonXPos + buttonSpacing;
        
        // Taste für die nächste Hand
        handNext = new Button(canvas, SWT.PUSH);
        handNext.setBounds(new Rectangle(buttonXPos, buttonYPos, buttonWidth, buttonHeight));
        handNext.setToolTipText("Next hand");
        handNext.setVisible(true);
        imgData = new ImageData(View.class.getClassLoader().getResourceAsStream("icons/player_end.png")); //$NON-NLS-1$
        imgData = imgData.scaledTo(iconWidth, iconHeight);
        handNext.setImage(new Image(display, imgData));
        handNext.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                doNexthand();
            }
        });
        buttonXPos = buttonXPos + buttonSpacing;
        
        // Taste für die nächste Aktion des Spielers
        nextAction = new Button(canvas, SWT.PUSH);
        nextAction.setBounds(new Rectangle(buttonXPos, buttonYPos, buttonWidth, buttonHeight));
        nextAction.setToolTipText("Next own action");
        nextAction.setVisible(true);
        imgData = new ImageData(View.class.getClassLoader().getResourceAsStream("icons/player_eject.png")); //$NON-NLS-1$
        imgData = imgData.scaledTo(iconWidth, iconHeight);
        nextAction.setImage(new Image(display, imgData));
        nextAction.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                System.out.println("Next action");
            }
        });
        buttonXPos = buttonXPos + buttonSpacing;

        // Taste für das Starten / Stoppen des Filmmodus
        autoPlay = new Button(canvas, SWT.PUSH);
        autoPlay.setBounds(new Rectangle(buttonXPos, buttonYPos, buttonWidth, buttonHeight));
        autoPlay.setToolTipText("Start autoplay");
        autoPlay.setVisible(true);
        imgData = new ImageData(View.class.getClassLoader().getResourceAsStream("icons/player_play.png")); //$NON-NLS-1$
        imgData = imgData.scaledTo(iconWidth, iconHeight);
        autoPlay.setImage(new Image(display, imgData));
        autoPlay.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                System.out.println("Auto play");
            }
        });
        buttonXPos = buttonXPos + buttonSpacing;
	}

	/**
	 * Position der einzelnen Spieler festlegen
	 * 
	 * @param countPlayer Anzahl der Spieler
	 * @param height Höhe der Grafik
	 * @param width Breite der Grafik
	 * @return Eine Liste mit den Positionen der Spieler
	 */
	private static ArrayList<Rectangle> getPlayPosition(int countPlayer, int height, int width) {
		// Position und Größe der Felder für die Spieler
		ArrayList<Rectangle> playerPos = new ArrayList<Rectangle>();
		
		// Höhe und Breite der Spielerfelder
		int hoehe = 100;
        int breite = 75;
        
        // Dealer
        int posHi = (int)(height / 4 - hoehe);
        int posRi = (int)(width / 2 - breite / 2);
        playerPos.add(new Rectangle(posRi, posHi, breite, hoehe));
        
        
        // Spieler 1
        posHi = (int)(height / 3.7 - hoehe);
        posRi = (int)(width - width / 3 - breite / 2);
        playerPos.add(new Rectangle(posRi, posHi, breite, hoehe));
       
        // Spieler 2
        posHi = (int)(height / 3 - hoehe);
        posRi = (int)(width - width / 6 - breite / 2);
        playerPos.add(new Rectangle(posRi, posHi, breite, hoehe));
        
        // Spieler 3
        posHi = (int)(height - height / 2 + hoehe);
        posRi = (int)(width - width / 6 - breite / 2);
        playerPos.add(new Rectangle(posRi, posHi, breite, hoehe));
        
        // Spieler 4
        posHi = (int)(height - height / 2.5 + hoehe);
        posRi = (int)(width - width / 3 - breite / 2);
        playerPos.add(new Rectangle(posRi, posHi, breite, hoehe));
        
        // Spieler 5
        posHi = (int)(height - height / 2.7 + hoehe);
        posRi = (int)(width / 2 - breite / 2);
        playerPos.add(new Rectangle(posRi, posHi, breite, hoehe));
        
        // Spieler 6
        posHi = (int)(height - height / 2.5 + hoehe);
        posRi = (int)(width / 3 - breite / 2);
        playerPos.add(new Rectangle(posRi, posHi, breite, hoehe));
        
        // Spieler 7
        posHi = (int)(height - height / 2 + hoehe);
        posRi = (int)(width / 6 - breite / 2);
        playerPos.add(new Rectangle(posRi, posHi, breite, hoehe));
        
        // Spieler 8
        posHi = (int)(height / 3 - hoehe);
        posRi = (int)(width / 6 - breite / 2);
        playerPos.add(new Rectangle(posRi, posHi, breite, hoehe));
        
        // Spieler 9
        posHi = (int)(height / 3.7 - hoehe);
        posRi = (int)(width / 3 - breite / 2);
        playerPos.add(new Rectangle(posRi, posHi, breite, hoehe));
        
		return playerPos;
	}

	/**
	 * Nächste Aktion der Hand holen und an Tisch weiterleiten
	 */
	protected void doNextStep() {
		// Zeiger auf nächste Aktion stellen
		ApplicationWorkbenchAdvisor.nextHandStep();
		
		// Aktion in Tisch übernehmen
		Hand hand = null;
		int activeHand = ApplicationWorkbenchAdvisor.getActiveHand();
		if (activeHand > -1 ) {
			hand = ApplicationWorkbenchAdvisor.getHands().get(activeHand);
		}
		int handStep = ApplicationWorkbenchAdvisor.getHandStep();
		if (handStep > 0 && hand != null) {
      		try {
				doAction(hand.getAction(handStep -1));
			} catch (ActionIllegalActionException e) {
				doNexthand();
			}
      	}
      
		// Tisch mit Änderungen neu anzeigen
		drawTable();
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
	private void doAction(Action action) {

		actionText = action.toString();
		
		// Spieler zur Aktion ermitteln
		Player p1 = action.getPlayer();
		Player player = null;
		
		// Position des Spielers am Tisch
		int pos = 0;
		if (p1 != null) {
			try {
				pos = table.getSeatOfPlayerByName(p1.getName()) -1;
				player = table.getPlayerByName(p1.getName());
			} catch (PlayerException e) {
				ErrorHandler.handleError(e, "Fehler beim Ermitteln es Spielers am Tisch.", false);
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
							player.addPocketCard((Card)action.getValue());
						} else {
							// nein, dann eine verdeckte Karte zeigen
							table.getPlayers()[i].addPocketCard(new Card(Card.TWO, Card.BACK));
						}
					}
				}
			} catch (PlayerException e) {
				ErrorHandler.handleError(e, "Fehler beim Ermitteln des Spielers.", false);
			} catch (CardException e) {
				ErrorHandler.handleError(e, "Fehler beim Ermitteln der Poketcards.", false);
			}
			break;
		case Action.SMALLBLIND:
			// Spieler zahl small Blind
			pay = player.pay(table.getSmallblind());
			
			// Pot erhöhen
			currentPot = currentPot + pay;
			
			// Einsatz vermerken
			bet[pos] = pay;
			
			// aktuelle Setzhöhe zurück setzen
			currentBet = 0.0;
			break;
		case Action.BIGBLIND:
			// Spieler zahl big Blind
			pay = player.pay(table.getBigblind());
			
			// Pot erhöhen
			currentPot = currentPot + pay;
			
			// Einsatz vermerken
			bet[pos] = pay;
			
			// aktuelle Setzhöhe zurück setzen
			currentBet = table.getBigblind();
			break;
		case Action.BET:
			pay = player.pay((Double)action.getValue());
			
			// Pot erhöhen
			currentPot = currentPot + pay;
			
			// Einsatz vermerken
			bet[pos] = pay;
			
			// aktuelle Setzhöhe merken
			currentBet = pay;
			break;
		case Action.CALL:
			pay = player.pay((Double)action.getValue());
			
			// Pot erhöhen
			currentPot = currentPot + pay;
			
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
			// Höhe der Erhöhung ermitteln
			double raise = (Double)action.getValue();
			
			// wieviel setzt der Spieler
			pay = currentBet + raise;
			
			// Spieler bezahlt
			pay = player.pay(pay);
			
			// Pot erhöhen
			currentPot = currentPot + pay;
			
			// Einsatz vermerken
			bet[pos] = bet[pos] + pay;
			
			// Setzhöhe für folgende Spieler erhöhen
			currentBet = currentBet + raise;
			break;
		case Action.FLOP:
			// merken das der Flop gezeitgt wird
			status = 1;
			
			// Einsätze auf 0 setzen
			resetEinsatz();
			break;
		case Action.TURN:
			// merken das der Turn gezeitgt wird
			status = 2;
			
			// Einsätze auf 0 setzen
			resetEinsatz();
			break;
		case Action.RIVER:
			// merken das der River gezeitgt wird
			status = 3;
			
			// Einsätze auf 0 setzen
			resetEinsatz();
			break;
		case Action.COLLECT:
			// Spieler bekommt den Pot
			try {
				// Spieler den Pot zuweisen
				player.deposit(currentPot);
				
				// Pot auf 0 setzen
				currentPot = 0.0;
				
				// Einsätze auf 0 setzen
				bet[pos] = 0.0;
			} catch (PlayerException e) {
				ErrorHandler.handleError(e, "Fehler beim verteilen des Einsatzes.", false);
			}
			break;
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
	 * Geht entweder zum Anfang der aktuellen Hand oder zum Anfang der vorherigen
	 */
	private void doHandBack() {
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
		ApplicationWorkbenchAdvisor.setTable((Table)ApplicationWorkbenchAdvisor.getHands().get(activeHand));
		
		// Tisch auf Anfang stellen
		resetTable();
		
		// Tisch mit Änderungen neu anzeigen
		drawTable();
	}
	
	/**
	 * Geht im Handverlauf einen Schritt zurück
	 */
	private void doStepBack() {
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
	 * Springt zur nächsten Hand
	 */
	private void doNexthand() {
		// Zeiger auf nächste Hand setzen
		ApplicationWorkbenchAdvisor.nextHand();
		
		// Tisch zurück setzen
		resetTable();
		
		// und frischen Tisch zeichnen
		drawTable();
	}

	/**
	 * Versetzt den Tisch in den Anfangszustand
	 */
	public void resetTable() {
		// aktuellen Pot zurücksetzen
		currentPot = 0.0;
		
		// Einsätze auf Null setzen
		resetEinsatz();
		
		// Text für die letze Aktion löschen
		actionText = "";
		
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
	 * @param countPlayer Anzahl der Spieler
	 * @param height Höhe der Grafik
	 * @param width Breite der Grafik
	 * @return Eine Liste mit den Positionen der Spieler
	 */
	private static ArrayList<Rectangle> getButtonPosition(int countPlayer, int height, int width) {
		// Position und Größe der Felder für die Spieler
		ArrayList<Rectangle> buttonPos = new ArrayList<Rectangle>();
		
		// Höhe und Breite der Spielerfelder
		int hoehe = 15;
        int breite = 15;
        
        // Dealer
        int posHi = (int)(height / 4 - hoehe) + 60;
        int posRi = (int)(width / 2 - breite / 2);
        buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));
        
        // Spieler 1
        posHi = (int)(height / 3.7 - hoehe + 60);
        posRi = (int)(width - width / 3 - breite / 2);
        buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));
        
        // Spieler 2
        posHi = (int)(height / 3 - hoehe + 60);
        posRi = (int)(width - width / 6 - breite / 2 - 40);
        buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));
        
        // Spieler 3
        posHi = (int)(height - height / 2 + hoehe + 50);
        posRi = (int)(width - width / 6 - breite / 2 - 40);
        buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));
        
        // Spieler 4
        posHi = (int)(height - height / 2.5 + hoehe + 25);
        posRi = (int)(width - width / 3 - breite / 2);
        buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));
        
        // Spieler 5
        posHi = (int)(height - height / 2.7 + hoehe + 20);
        posRi = (int)(width / 2 - breite / 2);
        buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));
        
        // Spieler 6
        posHi = (int)(height - height / 2.5 + hoehe + 25);
        posRi = (int)(width / 3 - breite / 2);
        buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));
        
        // Spieler 7
        posHi = (int)(height - height / 2 + hoehe + 50);
        posRi = (int)(width / 6 - breite / 2 + 40);
        buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));
        
        // Spieler 8
        posHi = (int)(height / 3 - hoehe + 60);
        posRi = (int)(width / 6 - breite / 2 + 35);
        buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));
        
        // Spieler 9
        posHi = (int)(height / 3.7 - hoehe + 60);
        posRi = (int)(width / 3 - breite / 2);
        buttonPos.add(new Rectangle(posRi, posHi, breite, hoehe));
        
		return buttonPos;
	}
}