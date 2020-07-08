package io.gitlab.lipor.util;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "io.gitlab.lipor.util.messages"; //$NON-NLS-1$
	public static String ErrorHandler_3;
	public static String LicenceDialog_0;
	public static String LicenceDialog_1;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
