/**
 * Copyright (C) 2008  Ralf Joswig
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 *
 */
package net.sourceforge.rjPokerReplay.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Ralf Joswig
 * 
 * Klasse zur Kontrollierten Fehlerbehandlung
 */
public class ErrorHandler {

	@SuppressWarnings("deprecation") //$NON-NLS-1$
	public static void handleError(Exception e, String message, boolean exit) {
		 final Thread current = Thread.currentThread();
	        if (current.getName().equals("rjPokerAutoPlay")) current.stop(); //$NON-NLS-1$
	        final Thread[] all = new Thread[Thread.activeCount()+100];
	        final int count = Thread.enumerate(all);
	        
	        // Try to find the main thread
	        //
	        for (int i=0;i<count;i++) {
	            if (all[i].getName().equals("rjPokerAutoPlay")) all[i].stop(); //$NON-NLS-1$
	        }


		if (message != null) {
			MessageBox mb = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK);
			mb.setMessage(e.getMessage());
			mb.setText(Messages.ErrorHandler_3);
			mb.open();
			System.out.println(message);
		}
		e.printStackTrace();
		if (exit) {
			System.exit(1);
		}
	}
}
