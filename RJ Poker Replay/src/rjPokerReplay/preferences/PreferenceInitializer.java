package rjPokerReplay.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import rjPokerReplay.Activator;

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
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_POKERSTARS_PATH, "C:\\Programme\\PokerStars\\HandHistory"); //$NON-NLS-1$
		store.setDefault(PreferenceConstants.P_POKERSTARS_PLAYER, "me"); //$NON-NLS-1$
		store.setDefault(PreferenceConstants.P_GENERAL_TIME_DELAY, 1);
//		store.setDefault(PreferenceConstants.P_BOOLEAN, true);
//		store.setDefault(PreferenceConstants.P_CHOICE, "choice4711");
//		store.setDefault(PreferenceConstants.P_STRING,
//				"Default value");
	}

}

