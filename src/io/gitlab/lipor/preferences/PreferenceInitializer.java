package io.gitlab.lipor.preferences;

import java.net.URL;
import java.security.CodeSource;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import io.gitlab.lipor.Activator;


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
		String path = "";
		try {
			path = Class.forName(this.getClass().getName()).getProtectionDomain().getCodeSource().getLocation()
					.getPath() + "images";
		} catch (ClassNotFoundException e) {
		}
	        
		// PreferenceStore besorgen
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		// und Felder vorbelegen
		store.setDefault(PreferenceConstants.P_WIN2DAY_PATH, "C:\\"); //$NON-NLS-1$
		store.setDefault(PreferenceConstants.P_WIN2DAY_PLAYER, "me"); //$NON-NLS-1$
		store.setDefault(PreferenceConstants.P_GENERAL_TIME_DELAY, 1);
		store.setDefault(PreferenceConstants.P_GENERAl_CARD_DIR, path); //$NON-NLS-1$
		store.setDefault(PreferenceConstants.P_GENERAl_TABLE_IMAGE, path + "/table.jpg"); //$NON-NLS-1$
	}

}


