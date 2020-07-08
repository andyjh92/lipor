/**
 * 
 */
package io.gitlab.lipor.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;

import io.gitlab.lipor.Activator;


public class LicenceDialog extends Dialog {

	// Textfeld fuer die Lizenz
	Text text;

	/**
	 * Konstruktor
	 * 
	 * @param parent Die uebergeordnete Shell
	 * @param style Style fuer den Dialog
	 */
	public LicenceDialog (Shell parent, int style) {
		super (parent, style);
		setText(Messages.LicenceDialog_0);
	}
	
	/**
	 * Konstruktor
	 * 
	 * @param parent Die uebergeordnete Shell
	 */
	public LicenceDialog (Shell parent) {
		this (parent, 0);
	}
	
	/**
	 * Zeigt den Lizenzdialog an
	 */
	public void open () {
		// eigenen Shell fuer die Anzeige erzeugen
		Shell parent = getParent();
		Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText(getText());
		Rectangle bounds = shell.getBounds();
		bounds.width = 600;
		shell.setBounds(bounds);
		
		// Wenn das Textfeld noch nicht existiert anlegen
		if (text == null) {
			// Layout festlegen, das Textfeld soll den kpl. Dialog ausfuellen
			shell.setLayout(new FillLayout());
			
			// Textfeld fuer die Lizenz erstellen
			text = new Text(shell, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
			text.setEditable(false);
			
			// Lizenz von der Platte laden. Muss im Root-Verzeichnis des Projektes liegen
			final Bundle bundle = Activator.getDefault().getBundle();
			Path path = new Path("gpl.txt"); //$NON-NLS-1$
			URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
			URL fileUrl = null;
			try {
				fileUrl = FileLocator.toFileURL(url);
				File file = new File(fileUrl.getPath());
				text.setText(""); //$NON-NLS-1$
				BufferedReader inputFile = null;;
				inputFile = new BufferedReader( new FileReader(file));
				String strZwischen;
				while ( ((inputFile != null) && (strZwischen = inputFile.readLine()) != null)) {
					text.append(strZwischen + "\n"); //$NON-NLS-1$
				}
			} catch (IOException e) {
				ErrorHandler.handleError(e, Messages.LicenceDialog_1, false);
			}
			
			// Text zum Anfang scrollen
			text.setSelection(0,0);
			text.showSelection();
		}
		
		// Dialog anzeigen
		shell.open();
	}
	
}


