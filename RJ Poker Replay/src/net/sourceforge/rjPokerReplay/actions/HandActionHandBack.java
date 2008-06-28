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

package net.sourceforge.rjPokerReplay.actions;

import net.sourceforge.rjPokerReplay.views.ViewTable;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;


public class HandActionHandBack implements IWorkbenchWindowActionDelegate {
	
	public static final String ID = "HandActionHandBack.action"; //$NON-NLS-1$
	
	IWorkbenchWindow window;

	public void dispose() {
		window = null;
	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	public void run(IAction action) {
		((ViewTable)(window.getActivePage().findView(ViewTable.ID))).doHandBack();
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
