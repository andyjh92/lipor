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

package rjPokerReplay;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import rjPokerReplay.views.ViewHandhistory;
import rjPokerReplay.views.ViewHandinfo;
import rjPokerReplay.views.ViewTable;
import rjPokerReplay.views.ViewTableinfo;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
	
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, (float)(IPageLayout.RATIO_MAX * 0.8), editorArea); //$NON-NLS-1$
		bottom.addPlaceholder(ViewHandhistory.ID);
		bottom.addView(ViewTableinfo.ID);
		bottom.addView(ViewHandinfo.ID);
		layout.addStandaloneView(ViewTable.ID, false, IPageLayout.LEFT, (float)(IPageLayout.RATIO_MAX * 0.2), editorArea);
		layout.getViewLayout(ViewTable.ID).setCloseable(false);
	}
}

