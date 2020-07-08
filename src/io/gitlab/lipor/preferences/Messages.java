package io.gitlab.lipor.preferences;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "io.gitlab.lipor.preferences.messages"; //$NON-NLS-1$
	public static String PreferenceGeneral_0;
	public static String PreferenceGeneral_1;
	public static String PreferenceGeneral_2;
	public static String PreferenceGeneral_3;
	public static String PreferencePagePokerstars_0;
	public static String PreferencePagePokerstars_1;
	public static String PreferencePagePokerstars_2;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
