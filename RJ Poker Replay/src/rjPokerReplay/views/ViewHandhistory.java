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

package rjPokerReplay.views;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import rjPokerReplay.ApplicationWorkbenchAdvisor;

import cardstuff.Hand;

public class ViewHandhistory extends ViewPart {
	
	public static final String ID = "RJPoker Replay.Handhistory";
	
	private Text text;

	public ViewHandhistory() {
	}
	
	@Override
	public void createPartControl(Composite parent) {
		text = new Text(parent, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		text.setEditable(false);
	}

	@Override
	public void setFocus() {
		if (ApplicationWorkbenchAdvisor.getHands() != null && ApplicationWorkbenchAdvisor.getHands().size() != 0) {
			showHand(ApplicationWorkbenchAdvisor.getHands().get(ApplicationWorkbenchAdvisor.getActiveHand()));
		}
	}
	
	public void showHand(Hand hand) {
		text.setText("");
		if (hand != null) {
			Iterator<String> iter = hand.getFileLines().iterator();
			while (iter.hasNext()) {
				text.append(iter.next() + Text.DELIMITER);
			}
		}
	}

}

