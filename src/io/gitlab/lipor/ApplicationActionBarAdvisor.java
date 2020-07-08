package io.gitlab.lipor;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import io.gitlab.lipor.actions.LicenceAction;
import io.gitlab.lipor.actions.ShowViewAction;
import io.gitlab.lipor.views.ViewHandhistory;
import io.gitlab.lipor.views.ViewHandinfo;
import io.gitlab.lipor.views.ViewTableinfo;


/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	// Actions - important to allocate these only in makeActions, and then use
	// them
	// in the fill methods. This ensures that the actions aren't recreated
	// when fillActionBars is called with FILL_PROXY.
	private IWorkbenchAction exitAction;
	private IWorkbenchAction importAction;
	private IWorkbenchAction helpAction;
	private IWorkbenchAction searchHelpAction;
    private IWorkbenchAction dynamicHelpAction;
	private IWorkbenchAction aboutAction;
	private IWorkbenchAction preferenceAction;
	private ShowViewAction showHandhistoryViewAction;
	private ShowViewAction showTableinfoViewAction;
	private ShowViewAction showHandinfoViewAction;
	private LicenceAction licenceAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
		ApplicationWorkbenchAdvisor.setApplicationActionBarAdvisor(this);
	}

	protected void makeActions(final IWorkbenchWindow window) {
		// Creates the actions and registers them.
		// Registering is needed to ensure that key bindings work.
		// The corresponding commands keybindings are defined in the plugin.xml
		// file.
		// Registering also provides automatic disposal of the actions when
		// the window is closed.

		exitAction = ActionFactory.QUIT.create(window);
		register(exitAction);
		
		importAction = ActionFactory.IMPORT.create(window);
		register(importAction);
		
		helpAction = ActionFactory.HELP_CONTENTS.create(window);
		register(helpAction);
		
		//searchHelpAction = ActionFactory.HELP_SEARCH.create(window);
	    //register(searchHelpAction);

	    //dynamicHelpAction = ActionFactory.DYNAMIC_HELP.create(window);
	    //register(dynamicHelpAction);
		
		aboutAction = ActionFactory.ABOUT.create(window);
		register(aboutAction);
		
		preferenceAction = ActionFactory.PREFERENCES.create(window);
		register(preferenceAction);
		
		showHandhistoryViewAction = new ShowViewAction(window, Messages.ApplicationActionBarAdvisor_0, ViewHandhistory.ID);
		showHandhistoryViewAction.show(false);
		showHandhistoryViewAction.setChecked(false);
		register(showHandhistoryViewAction);
		
		showTableinfoViewAction = new ShowViewAction(window, Messages.ApplicationActionBarAdvisor_1, ViewTableinfo.ID);
		showTableinfoViewAction.show(true);
		showTableinfoViewAction.setChecked(true);
		register(showTableinfoViewAction);
		
		showHandinfoViewAction = new ShowViewAction(window, Messages.ApplicationActionBarAdvisor_2, ViewHandinfo.ID);
		showHandinfoViewAction.show(true);
		showHandinfoViewAction.setChecked(true);
		register(showHandinfoViewAction);
		
		licenceAction = new LicenceAction(window);
		register(licenceAction);
	}

	/**
	 * Menueleiste zusammenbauen
	 */
	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_3,  
				IWorkbenchActionConstants.M_FILE);
		menuBar.add(fileMenu);
		fileMenu.add(importAction);
		fileMenu.add(exitAction);
		
		MenuManager windowMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_4, IWorkbenchActionConstants.M_WINDOW);
		windowMenu.add(showHandhistoryViewAction);
		windowMenu.add(showHandinfoViewAction);
		windowMenu.add(showTableinfoViewAction);
		windowMenu.add(preferenceAction);
		menuBar.add(windowMenu);
		
		MenuManager helpMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_5,  
				IWorkbenchActionConstants.M_HELP);
		menuBar.add(helpMenu);
//		helpMenu.add(helpAction);
//		helpMenu.add(searchHelpAction);
//		helpMenu.add(dynamicHelpAction);
		helpMenu.add(licenceAction);
		helpMenu.add(aboutAction);
	}
}
