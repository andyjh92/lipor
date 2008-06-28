package net.sourceforge.rjPokerReplay.util;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "net.sourceforge.rjPokerReplay.util.messages"; //$NON-NLS-1$
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
