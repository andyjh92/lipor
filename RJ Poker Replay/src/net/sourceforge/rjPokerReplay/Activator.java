package net.sourceforge.rjPokerReplay;

import java.util.HashMap;

import net.sourceforge.rjPokerReplay.util.AbstractListener;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "RjPokerReplay"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	// Liste der Views die auf Tischaenderungen horchen
	private HashMap<String, AbstractListener> listener = new HashMap<String, AbstractListener> ();
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	
	/**
	 * Fuegt einen Listener an, der bei Veraenderungen am Tisch informiert werden will
	 * 
	 * @param id Eindeutige Kennung fuer den Listener
	 * @param listener Das Objekt das benachrichtigt werden will
	 * @throws IllegalArgumentException
	 */
	public void addListener(String id, AbstractListener listener) throws IllegalArgumentException {
		// eine ID und ein Listener muessen uebergeben werden
		if ("".equals(id) || id == null || listener == null) {  //$NON-NLS-1$
			throw new IllegalArgumentException(Messages.Activator_0);
		}
		
		// pruefen ob bereits ein Listener unter dieser ID aktiv ist
		if (this.listener.containsKey(id)) {
			throw new IllegalArgumentException(Messages.Activator_1);
		}
		
		// wenn wir bis hierhin gekommen sind
		this.listener.put(id, listener);
	}
	
	/**
	 * Loescht einen Listener aus der Liste der Beobacher
	 * 
	 * @param id Eindeutige ID des Listeners
	 * @throws IllegalArgumentException
	 */
	public void removeListener(String id) throws IllegalArgumentException {
		// eine ID muss uebergeben werden
		if ("".equals(id) || id == null) {  //$NON-NLS-1$
			throw new IllegalArgumentException(Messages.Activator_2);
		}
		
		// versuchen wir den Listener zu entfernen
		listener.remove(id);
	}
	
	/**
	 * Informiert alle gewuenschten Objekte ueber Aenderungen am Tisch
	 */
	public void inform() {
		for ( String elem : listener.keySet() ) {
			listener.get(elem).inform(elem);
		}
	}
	
}
