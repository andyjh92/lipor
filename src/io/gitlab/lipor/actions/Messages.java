package io.gitlab.lipor.actions;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "io.gitlab.lipor.actions.messages"; //$NON-NLS-1$
	public static String LicenceAction_0;
	public static String ShowViewAction_0;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
