package rjPokerReplay;

import java.util.ArrayList;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.osgi.framework.Bundle;

import cardstuff.Hand;
import cardstuff.Table;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "RjPokerReplay.perspective";  //$NON-NLS-1$
	
	// ArrayListe mit den aktuellen Haenden
	private static ArrayList<Hand> hands;
	
	// die gerade gezeigte Hand
	private static int activeHand = -1;
	
	// Der gerade gezeigte Schritt in der Hand
	private static int handStep = -1;
	
	// Der aktuelle tisch
	private static Table table;
	
	// Ein Sammler fuer Grafiken
	private static ImageRegistry imageReg;
	
	// Kennzeichen ob am Tisch eine Hand per Autoplay laeuft
	private static boolean autoplay = false;
	
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
    		IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}
	
	/**
	 * Gibt eine Liste der Haende zurueck
	 * 
	 * @return Die Liste der Haende
	 */
	public static ArrayList<Hand> getHands() {
		return hands;
	}
	
	/**
	 * Setzt die aktuelle Liste der Haende
	 * 
	 * @param handsList Die Haende die gesetzt werden sollen
	 */
	public static void setHands(ArrayList<Hand> handsList) {
		hands = handsList;
	}
	
	/**
	 * Setzt die aktive Hand
	 * Soll eine groesser als die hoechste Hand gesetzt werde, wird die hoechste Hand genommen
	 * 
	 * @param handNumber Die Nummer der Hand die gesetzt werden soll
	 */
	public static void setActiveHand(int handNumber) {
		// Nummer der letzte Hand ermitteln
		int handmax = hands.size();
		
		// Haende kleiner Null gibt es nicht
		if (handNumber < 0) {
			// dann auf die erste Handsetzen
			handNumber = 0;
		}
		
		// liegt die gewueschte Hand vor der letzen ?
		if (handNumber <= handmax) {
			// ja, dann merken
			activeHand = handNumber;
		} else {
			// nein, dann auf die letzte Hand setzen
			activeHand = handmax;
		}
		
		// und auf die erste Atkion dieser Hand
		handStep = 0;
	}
	
	/**
	 * Gibt die Nummer der aktiven hand zurueck
	 * 
	 * @return Die Nummer der aktiven Hand
	 */
	public static int getActiveHand() {
		return activeHand;
	}
	
	/**
	 * Setzt die aktuelle Aktion in der Hand
	 * 
	 * @param step Die Nummer der gewuenschten Aktion
	 */
	public static void setHandStep(int step) {
		// Es gibt keine Aktion kleiner Null
		if (step < 0) {
			step = 0;
		}
		
		// letzte Aktion der Hand ermitteln
		int lastStep = hands.get(activeHand).getCountOfActions();
		
		// liegt die gewueschte Aktion hinter der letzen?
		if (step > lastStep) {
			// ja, dann auf die letze Aktion setzen
			handStep = lastStep;
		} else {
			// nein, dann auf die gewuenschte Aktion setzen
			handStep = step;
		}
	}
	
	/**
	 * Gibt die Nummer der aktuellen Ation der aktuellen Hand zurueck
	 * 
	 * @return Die Nummer der Aktion
	 */
	public static int getHandStep() {
		return handStep;
	}
	
	/**
	 * Setzt den Zeiger fuer die aktuelle Atkion der Hand um eins weiter
	 */
	public static void nextHandStep() {
		// letzte Aktion der Hand ermitteln
		int lastStep = hands.get(activeHand).getCountOfActions();
		
		// Nur dann weiter setzen wenn noch nicht die letze Atkion
		if (handStep < lastStep) {
			handStep++;
		}
	}
	
	/**
	 * Setzt aktuellen Tisch
	 * 
	 * @param tableNew Der Tisch der gesetzt werden soll
	 */
	public static void setTable(Table tableNew) {
		table = tableNew;
	}
	
	/**
	 * Gibt den Tisch zurueck
	 * 
	 * @return Der aktuelle Tisch
	 */
	public static Table getTable() {
		return table;
	}
	
	/**
	 * Setzt den Zeiger auf die naechste Hand
	 */
	public static void nextHand() {
		setActiveHand(activeHand + 1);
		setTable((Table)getHands().get(activeHand));
	}
	
	/**
	 * Beschaft die fuer den Start notwendigen Daten
	 */
	public void initialize(IWorkbenchConfigurer configurer) {
		initImageStore();
//		initPrefDaten();
    }
	
	/**
	 * Erzeugt eine Imageregistry und versorgt diese  mit den notwendigen Bildern
	 *
	 */
	private void initImageStore() {
				
		// Grundpfad zu den Bildern
		String imagesPath ="images/"; //$NON-NLS-1$
		
		// Grundpfad zu den Icons
		String iconsPath = "icons/"; //$NON-NLS-1$
		
		// Speicher fuer die Bilder anlegen und befuellen
		imageReg = new ImageRegistry();
		imageReg.put(Constants.IMG_BACKGROUND, getImageDescriptor(imagesPath + "table.jpg")); //$NON-NLS-1$
		imageReg.put(Constants.IMG_BUTTON_BUTTON, getImageDescriptor(imagesPath + "button_dealer.gif")); //$NON-NLS-1$
		imageReg.put(Constants.IMG_BUTTON_START, getImageDescriptor(iconsPath + "player_start.png")); //$NON-NLS-1$
		imageReg.put(Constants.IMG_BUTTON_REW, getImageDescriptor(iconsPath + "player_rew.png")); //$NON-NLS-1$
		imageReg.put(Constants.IMG_BUTTON_FWD, getImageDescriptor(iconsPath + "player_fwd.png")); //$NON-NLS-1$
		imageReg.put(Constants.IMG_BUTTON_END, getImageDescriptor(iconsPath + "player_end.png")); //$NON-NLS-1$
		imageReg.put(Constants.IMG_BUTTON_EJECT, getImageDescriptor(iconsPath + "player_eject.png")); //$NON-NLS-1$
		imageReg.put(Constants.IMG_BUTTON_PLAY, getImageDescriptor(iconsPath + "player_play.png")); //$NON-NLS-1$
		imageReg.put(Constants.IMG_CHECKED, getImageDescriptor(iconsPath + "haken.png")); //$NON-NLS-1$
		imageReg.put(Constants.IMG_CARD_BACK, getImageDescriptor(imagesPath + Constants.IMG_CARD_BACK));
		imageReg.put(Constants.IMG_CARD_CLUB_TWO, getImageDescriptor(imagesPath + Constants.IMG_CARD_CLUB_TWO));
		imageReg.put(Constants.IMG_CARD_CLUB_THREE, getImageDescriptor(imagesPath + Constants.IMG_CARD_CLUB_THREE));
		imageReg.put(Constants.IMG_CARD_CLUB_FOUR, getImageDescriptor(imagesPath + Constants.IMG_CARD_CLUB_FOUR));
		imageReg.put(Constants.IMG_CARD_CLUB_FIVE, getImageDescriptor(imagesPath + Constants.IMG_CARD_CLUB_FIVE));
		imageReg.put(Constants.IMG_CARD_CLUB_SIX, getImageDescriptor(imagesPath + Constants.IMG_CARD_CLUB_SIX));
		imageReg.put(Constants.IMG_CARD_CLUB_SEVEN, getImageDescriptor(imagesPath + Constants.IMG_CARD_CLUB_SEVEN));
		imageReg.put(Constants.IMG_CARD_CLUB_EIGHT, getImageDescriptor(imagesPath + Constants.IMG_CARD_CLUB_EIGHT));
		imageReg.put(Constants.IMG_CARD_CLUB_NINE, getImageDescriptor(imagesPath + Constants.IMG_CARD_CLUB_NINE));
		imageReg.put(Constants.IMG_CARD_CLUB_TEN, getImageDescriptor(imagesPath + Constants.IMG_CARD_CLUB_TEN));
		imageReg.put(Constants.IMG_CARD_CLUB_JACK, getImageDescriptor(imagesPath + Constants.IMG_CARD_CLUB_JACK));
		imageReg.put(Constants.IMG_CARD_CLUB_QUEEN, getImageDescriptor(imagesPath + Constants.IMG_CARD_CLUB_QUEEN));
		imageReg.put(Constants.IMG_CARD_CLUB_KING, getImageDescriptor(imagesPath + Constants.IMG_CARD_CLUB_KING));
		imageReg.put(Constants.IMG_CARD_CLUB_ASS, getImageDescriptor(imagesPath + Constants.IMG_CARD_CLUB_ASS));
		imageReg.put(Constants.IMG_CARD_HART_TWO, getImageDescriptor(imagesPath + Constants.IMG_CARD_HART_TWO));
		imageReg.put(Constants.IMG_CARD_HART_THREE, getImageDescriptor(imagesPath + Constants.IMG_CARD_HART_THREE));
		imageReg.put(Constants.IMG_CARD_HART_FOUR, getImageDescriptor(imagesPath + Constants.IMG_CARD_HART_FOUR));
		imageReg.put(Constants.IMG_CARD_HART_FIVE, getImageDescriptor(imagesPath + Constants.IMG_CARD_HART_FIVE));
		imageReg.put(Constants.IMG_CARD_HART_SIX, getImageDescriptor(imagesPath + Constants.IMG_CARD_HART_SIX));
		imageReg.put(Constants.IMG_CARD_HART_SEVEN, getImageDescriptor(imagesPath + Constants.IMG_CARD_HART_SEVEN));
		imageReg.put(Constants.IMG_CARD_HART_EIGHT, getImageDescriptor(imagesPath + Constants.IMG_CARD_HART_EIGHT));
		imageReg.put(Constants.IMG_CARD_HART_NINE, getImageDescriptor(imagesPath + Constants.IMG_CARD_HART_NINE));
		imageReg.put(Constants.IMG_CARD_HART_TEN, getImageDescriptor(imagesPath + Constants.IMG_CARD_HART_TEN));
		imageReg.put(Constants.IMG_CARD_HART_JACK, getImageDescriptor(imagesPath + Constants.IMG_CARD_HART_JACK));
		imageReg.put(Constants.IMG_CARD_HART_QUEEN, getImageDescriptor(imagesPath + Constants.IMG_CARD_HART_QUEEN));
		imageReg.put(Constants.IMG_CARD_HART_KING, getImageDescriptor(imagesPath + Constants.IMG_CARD_HART_KING));
		imageReg.put(Constants.IMG_CARD_HART_ASS, getImageDescriptor(imagesPath + Constants.IMG_CARD_HART_ASS));
		imageReg.put(Constants.IMG_CARD_DIAMOND_TWO, getImageDescriptor(imagesPath + Constants.IMG_CARD_DIAMOND_TWO));
		imageReg.put(Constants.IMG_CARD_DIAMOND_THREE, getImageDescriptor(imagesPath + Constants.IMG_CARD_DIAMOND_THREE));
		imageReg.put(Constants.IMG_CARD_DIAMOND_FOUR, getImageDescriptor(imagesPath + Constants.IMG_CARD_DIAMOND_FOUR));
		imageReg.put(Constants.IMG_CARD_DIAMOND_FIVE, getImageDescriptor(imagesPath + Constants.IMG_CARD_DIAMOND_FIVE));
		imageReg.put(Constants.IMG_CARD_DIAMOND_SIX, getImageDescriptor(imagesPath + Constants.IMG_CARD_DIAMOND_SIX));
		imageReg.put(Constants.IMG_CARD_DIAMOND_SEVEN, getImageDescriptor(imagesPath + Constants.IMG_CARD_DIAMOND_SEVEN));
		imageReg.put(Constants.IMG_CARD_DIAMOND_EIGHT, getImageDescriptor(imagesPath + Constants.IMG_CARD_DIAMOND_EIGHT));
		imageReg.put(Constants.IMG_CARD_DIAMOND_NINE, getImageDescriptor(imagesPath + Constants.IMG_CARD_DIAMOND_NINE));
		imageReg.put(Constants.IMG_CARD_DIAMOND_TEN, getImageDescriptor(imagesPath + Constants.IMG_CARD_DIAMOND_TEN));
		imageReg.put(Constants.IMG_CARD_DIAMOND_JACK, getImageDescriptor(imagesPath + Constants.IMG_CARD_DIAMOND_JACK));
		imageReg.put(Constants.IMG_CARD_DIAMOND_QUEEN, getImageDescriptor(imagesPath + Constants.IMG_CARD_DIAMOND_QUEEN));
		imageReg.put(Constants.IMG_CARD_DIAMOND_KING, getImageDescriptor(imagesPath + Constants.IMG_CARD_DIAMOND_KING));
		imageReg.put(Constants.IMG_CARD_DIAMOND_ASS, getImageDescriptor(imagesPath + Constants.IMG_CARD_DIAMOND_ASS));
		imageReg.put(Constants.IMG_CARD_SPADE_TWO, getImageDescriptor(imagesPath + Constants.IMG_CARD_SPADE_TWO));
		imageReg.put(Constants.IMG_CARD_SPADE_THREE, getImageDescriptor(imagesPath + Constants.IMG_CARD_SPADE_THREE));
		imageReg.put(Constants.IMG_CARD_SPADE_FOUR, getImageDescriptor(imagesPath + Constants.IMG_CARD_SPADE_FOUR));
		imageReg.put(Constants.IMG_CARD_SPADE_FIVE, getImageDescriptor(imagesPath + Constants.IMG_CARD_SPADE_FIVE));
		imageReg.put(Constants.IMG_CARD_SPADE_SIX, getImageDescriptor(imagesPath + Constants.IMG_CARD_SPADE_SIX));
		imageReg.put(Constants.IMG_CARD_SPADE_SEVEN, getImageDescriptor(imagesPath + Constants.IMG_CARD_SPADE_SEVEN));
		imageReg.put(Constants.IMG_CARD_SPADE_EIGHT, getImageDescriptor(imagesPath + Constants.IMG_CARD_SPADE_EIGHT));
		imageReg.put(Constants.IMG_CARD_SPADE_NINE, getImageDescriptor(imagesPath + Constants.IMG_CARD_SPADE_NINE));
		imageReg.put(Constants.IMG_CARD_SPADE_TEN, getImageDescriptor(imagesPath + Constants.IMG_CARD_SPADE_TEN));
		imageReg.put(Constants.IMG_CARD_SPADE_JACK, getImageDescriptor(imagesPath + Constants.IMG_CARD_SPADE_JACK));
		imageReg.put(Constants.IMG_CARD_SPADE_QUEEN, getImageDescriptor(imagesPath + Constants.IMG_CARD_SPADE_QUEEN));
		imageReg.put(Constants.IMG_CARD_SPADE_KING, getImageDescriptor(imagesPath + Constants.IMG_CARD_SPADE_KING));
		imageReg.put(Constants.IMG_CARD_SPADE_ASS, getImageDescriptor(imagesPath + Constants.IMG_CARD_SPADE_ASS));
		imageReg.put(Constants.IMG_OPEN_FOLDER, getImageDescriptor(iconsPath + "openFolder.gif")); //$NON-NLS-1$
	}
	
	/**
	 * Gibt einen Verweis auf die ImageRegistry zur�ck
	 * 
	 * @return Imageregistry - die ImageRegistry
	 */
	public static ImageRegistry getImageStore(){
		return imageReg;
	}
	
	/**
	 * Gibt den Status zurueck ob gerade eine Hand per Autoplay abgespielt wird.
	 * 
	 * @return True wenn ein Autoplay laeuft
	 */
	public static boolean isAutoplay() {
		return autoplay;
	}
	
	/**
	 * Setzt den Status ob gerade eine Hand per Autoplay abgespielt wird.
	 * 
	 * @param current Den zu setzenden Status
	 */
	public static void setAutoplay(boolean current) {
		autoplay = current;
	}
	
	private static ImageDescriptor getImageDescriptor(String path) {
		final Bundle bundle = Activator.getDefault().getBundle();
		ImageDescriptor ret = null;
		ret = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path(path), null));
		return ret;
		
	}
}