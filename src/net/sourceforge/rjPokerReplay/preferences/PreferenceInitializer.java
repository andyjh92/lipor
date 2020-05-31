package net.sourceforge.rjPokerReplay.preferences;

import java.net.URL;
import java.security.CodeSource;

import net.sourceforge.rjPokerReplay.Activator;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;


/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		// Pfad für einige Felder bestimmen
		String path = ""; //$NON-NLS-1$
		try {
			CodeSource source = Class.forName(this.getClass().getName()).getProtectionDomain().getCodeSource();
			if ( source != null ) {
				URL location = source.getLocation();
				path = location.getPath();
				int i = 0;
				String sep = System.getProperty("file.separator"); //$NON-NLS-1$
				do {
					String t = path.substring(path.length() - 1, path.length());
					if (sep.equals(t)) {
						i++;
					}
					path = path.substring(0, path.length() - 1);
				} while (i < 2);
				path = path + sep + "images"; //$NON-NLS-1$
			} 
        } catch ( Exception e ) {
        	// Nix tun
        }
	        
		// PreferenceStore besorgen
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		// und Felder vorbelegen
		store.setDefault(PreferenceConstants.P_POKERSTARS_PATH, "C:\\Programme\\PokerStars\\HandHistory"); //$NON-NLS-1$
		store.setDefault(PreferenceConstants.P_POKERSTARS_PLAYER, "me"); //$NON-NLS-1$
		store.setDefault(PreferenceConstants.P_GENERAL_TIME_DELAY, 1);
		store.setDefault(PreferenceConstants.P_GENERAl_CARD_DIR, path); //$NON-NLS-1$
		store.setDefault(PreferenceConstants.P_GENERAl_TABLE_IMAGE, path + "/table.jpg"); //$NON-NLS-1$
//		store.setDefault(PreferenceConstants.P_BOOLEAN, true);
//		store.setDefault(PreferenceConstants.P_CHOICE, "choice4711");
//		store.setDefault(PreferenceConstants.P_STRING,
//				"Default value");
	}

}


