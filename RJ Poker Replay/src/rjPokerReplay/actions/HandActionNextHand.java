package rjPokerReplay.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import rjPokerReplay.views.ViewTable;

public class HandActionNextHand implements IWorkbenchWindowActionDelegate {
	
	public static final String ID = "HandActionNextHand.action"; //$NON-NLS-1$
	
	IWorkbenchWindow window;

	public void dispose() {
		window = null;
	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	public void run(IAction action) {
		((ViewTable)(window.getActivePage().findView(ViewTable.ID))).doNextHand();
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
