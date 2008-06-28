package net.sourceforge.rjPokerReplay;

import java.util.ArrayList;

import net.sourceforge.rjPokerReplay.cardstuff.Hand;
import net.sourceforge.rjPokerReplay.cardstuff.Table;
import net.sourceforge.rjPokerReplay.preferences.PreferenceConstants;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.osgi.framework.Bundle;


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
	
	// die ActionBar
	private static ApplicationActionBarAdvisor advisor;
	
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
	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		// einschalten das beim Beenden das Layout gesichert wird
		configurer.setSaveAndRestore(true);
		
		// Imagestore für die Bilder initialisieren
		initImageStore();
		
    }
	
	/**
	 * Erzeugt eine Imageregistry und versorgt diese  mit den notwendigen Bildern
	 *
	 */
	public static void initImageStore() {
				
		// Grundpfad zu den Bildern
		String imagesPath ="images/"; //$NON-NLS-1$
		
		// Grundpfad zu den Icons
		String iconsPath = "icons/"; //$NON-NLS-1$
		
		// Speicher fuer die Bilder anlegen und befuellen
		imageReg = new ImageRegistry();
		imageReg.put(Constants.IMG_BACKGROUND, ImageDescriptor.createFromFile(null, Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_GENERAl_TABLE_IMAGE)));
		imageReg.put(Constants.IMG_BUTTON_BUTTON, getImageDescriptor(imagesPath + "button_dealer.gif")); //$NON-NLS-1$
		imageReg.put(Constants.IMG_BUTTON_START, getImageDescriptor(iconsPath + "player_start.png")); //$NON-NLS-1$
		imageReg.put(Constants.IMG_BUTTON_REW, getImageDescriptor(iconsPath + "player_rew.png")); //$NON-NLS-1$
		imageReg.put(Constants.IMG_BUTTON_FWD, getImageDescriptor(iconsPath + "player_fwd.png")); //$NON-NLS-1$
		imageReg.put(Constants.IMG_BUTTON_END, getImageDescriptor(iconsPath + "player_end.png")); //$NON-NLS-1$
		imageReg.put(Constants.IMG_BUTTON_EJECT, getImageDescriptor(iconsPath + "player_eject.png")); //$NON-NLS-1$
		imageReg.put(Constants.IMG_BUTTON_PLAY, getImageDescriptor(iconsPath + "player_play.png")); //$NON-NLS-1$
		imageReg.put(Constants.IMG_CHECKED, getImageDescriptor(iconsPath + "haken.png")); //$NON-NLS-1$
		imageReg.put(Constants.IMG_OPEN_FOLDER, getImageDescriptor(iconsPath + "openFolder.gif")); //$NON-NLS-1$
		
		// Karten
		String cardPath = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_GENERAl_CARD_DIR) + "/";  //$NON-NLS-1$
		addCard(Constants.IMG_CARD_BACK, cardPath);
		addCard(Constants.IMG_CARD_CLUB_TWO, cardPath);
		addCard(Constants.IMG_CARD_CLUB_THREE, cardPath);
		addCard(Constants.IMG_CARD_CLUB_FOUR, cardPath);
		addCard(Constants.IMG_CARD_CLUB_FIVE, cardPath);
		addCard(Constants.IMG_CARD_CLUB_SIX, cardPath);
		addCard(Constants.IMG_CARD_CLUB_SEVEN, cardPath);
		addCard(Constants.IMG_CARD_CLUB_EIGHT, cardPath);
		addCard(Constants.IMG_CARD_CLUB_NINE, cardPath);
		addCard(Constants.IMG_CARD_CLUB_TEN, cardPath);
		addCard(Constants.IMG_CARD_CLUB_JACK, cardPath);
		addCard(Constants.IMG_CARD_CLUB_QUEEN, cardPath);
		addCard(Constants.IMG_CARD_CLUB_KING, cardPath);
		addCard(Constants.IMG_CARD_CLUB_ASS, cardPath);
		addCard(Constants.IMG_CARD_HART_TWO, cardPath);
		addCard(Constants.IMG_CARD_HART_THREE, cardPath);
		addCard(Constants.IMG_CARD_HART_FOUR, cardPath);
		addCard(Constants.IMG_CARD_HART_FIVE, cardPath);
		addCard(Constants.IMG_CARD_HART_SIX, cardPath);
		addCard(Constants.IMG_CARD_HART_SEVEN, cardPath);
		addCard(Constants.IMG_CARD_HART_EIGHT, cardPath);
		addCard(Constants.IMG_CARD_HART_NINE, cardPath);
		addCard(Constants.IMG_CARD_HART_TEN, cardPath);
		addCard(Constants.IMG_CARD_HART_JACK, cardPath);
		addCard(Constants.IMG_CARD_HART_QUEEN, cardPath);
		addCard(Constants.IMG_CARD_HART_KING, cardPath);
		addCard(Constants.IMG_CARD_HART_ASS, cardPath);
		addCard(Constants.IMG_CARD_DIAMOND_TWO, cardPath);
		addCard(Constants.IMG_CARD_DIAMOND_THREE, cardPath);
		addCard(Constants.IMG_CARD_DIAMOND_FOUR, cardPath);
		addCard(Constants.IMG_CARD_DIAMOND_FIVE, cardPath);
		addCard(Constants.IMG_CARD_DIAMOND_SIX, cardPath);
		addCard(Constants.IMG_CARD_DIAMOND_SEVEN, cardPath);
		addCard(Constants.IMG_CARD_DIAMOND_EIGHT, cardPath);
		addCard(Constants.IMG_CARD_DIAMOND_NINE, cardPath);
		addCard(Constants.IMG_CARD_DIAMOND_TEN, cardPath);
		addCard(Constants.IMG_CARD_DIAMOND_JACK, cardPath);
		addCard(Constants.IMG_CARD_DIAMOND_QUEEN, cardPath);
		addCard(Constants.IMG_CARD_DIAMOND_KING, cardPath);
		addCard(Constants.IMG_CARD_DIAMOND_ASS, cardPath);
		addCard(Constants.IMG_CARD_SPADE_TWO, cardPath);
		addCard(Constants.IMG_CARD_SPADE_THREE, cardPath);
		addCard(Constants.IMG_CARD_SPADE_FOUR, cardPath);
		addCard(Constants.IMG_CARD_SPADE_FIVE, cardPath);
		addCard(Constants.IMG_CARD_SPADE_SIX, cardPath);
		addCard(Constants.IMG_CARD_SPADE_SEVEN, cardPath);
		addCard(Constants.IMG_CARD_SPADE_EIGHT, cardPath);
		addCard(Constants.IMG_CARD_SPADE_NINE, cardPath);
		addCard(Constants.IMG_CARD_SPADE_TEN, cardPath);
		addCard(Constants.IMG_CARD_SPADE_JACK, cardPath);
		addCard(Constants.IMG_CARD_SPADE_QUEEN, cardPath);
		addCard(Constants.IMG_CARD_SPADE_KING, cardPath);
		addCard(Constants.IMG_CARD_SPADE_ASS, cardPath);
	}
	
	/**
	 * Hilfsmethode um eine Karte zum ImageReg hinzu zufügem
	 * 
	 * @param card Die Konstante für die Karte
	 * @param path Das Verzeichnis in dem die Bilder der Karten liegen
	 */
	private static void addCard(String card, String path) {
		imageReg.put(card, ImageDescriptor.createFromFile(null, path + card));
	}

	/**
	 * Gibt einen Verweis auf die ImageRegistry zurueck
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
	
	public static void setApplicationActionBarAdvisor(ApplicationActionBarAdvisor advi){
		advisor = advi;
	}
	
	public static ApplicationActionBarAdvisor getApplicationActionBarAdvisor() {
		return advisor;
	}
	
//	@Override
//	public void postShutdown() {
////		IMemento state = new IMemento();
////		advisor.saveState(state);
//		
////		IWorkbench workbench = PlatformUI.getWorkbench();
////		IWorkbenchWindow win = workbench.getActiveWorkbenchWindow();
////		IWorkbenchPage page = win.getActivePage();
////		IEditorPart editor = page.getActiveEditor();
////		IEditorSite side = editor.getEditorSite();
////		IActionBars bar = side.getActionBars();
////		IMenuManager menueMgr = bar.getMenuManager();
//////		IMenuManager menueMgr = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorSite().getActionBars().getMenuManager();
////		IContributionItem[] items = menueMgr.getItems();
//		
//	}
	
//	@Override
//	public void postStartup(){
//	}

//	@Override
//	public boolean preShutdown() {
//		IWorkbench workbench = PlatformUI.getWorkbench();
//		IWorkbenchWindow win = workbench.getActiveWorkbenchWindow();
//
//////		IWorkbenchPage page = win.getActivePage();
//////		IViewReference viewRef[] = page.getViewReferences();
//////		IViewPart view = viewRef[0].getView(false);
//////		
//////		IEditorPart editor = page.getActiveEditor();
//////		
////////		IEditorSite side = editor.getEditorSite();
//////		IViewSite side = view.getViewSite();
//////		
//////		IActionBars bar = side.getActionBars();
//////		IMenuManager menueMgr = bar.getMenuManager();
////////		IMenuManager menueMgr = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorSite().getActionBars().getMenuManager();
//////		IContributionItem[] items = menueMgr.getItems();
//		return true;
//	}
}