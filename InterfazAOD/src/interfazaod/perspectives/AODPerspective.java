/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package interfazaod.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.cea.papyrus.core.IPapyrusUIConstants;
import com.cea.papyrus.core.ui.wizards.CreateModelFromFileWizard;
import com.cea.papyrus.core.ui.wizards.CreateModelLibraryWizard;
import com.cea.papyrus.core.ui.wizards.CreateModelWizard;
import com.cea.papyrus.core.ui.wizards.CreateProfileWizard;
import com.cea.papyrus.core.ui.wizards.CreateProjectWizard;

/**
 *  This class is meant to serve as an example for how various contributions 
 *  are made to a perspective. Note that some of the extension point id's are
 *  referred to as API constants while others are hardcoded and may be subject 
 *  to change. 
 */
public class AODPerspective implements IPerspectiveFactory {

	private IPageLayout factory;

	public AODPerspective() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
	/**
	 * 
	 * this method create the layout attached to this perspective
	 * @param layout 
	 */
	public void createInitialLayout(IPageLayout layout) {
		defineActions(layout);
		defineLayout(layout);
	}

	/**
	 * Add actions into the workbench UI.
	 * 
	 * @param layout the page layout
	 * 
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
	public void defineActions(IPageLayout layout) {
		// Add "new wizards".
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.project");
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");//$NON-NLS-1$
		layout.addNewWizardShortcut(CreateProjectWizard.WIZARD_ID);
        layout.addNewWizardShortcut(CreateModelWizard.WIZARD_ID);
		layout.addNewWizardShortcut(CreateProfileWizard.WIZARD_ID);
		layout.addNewWizardShortcut(CreateModelFromFileWizard.WIZARD_ID);
		layout.addNewWizardShortcut(CreateModelLibraryWizard.WIZARD_ID);
		
		// Add "show views".
		layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
		layout.addShowViewShortcut(IPapyrusUIConstants.BIRDVIEW_ID);
		// layout.addShowViewShortcut("org.eclipse.pde.runtime.LogView");
		
		layout.addActionSet("org.eclipse.debug.ui.launchActionSet");
		
		// add perspectives
		layout.addPerspectiveShortcut("org.eclipse.ui.resourcePerspective");
		layout.addPerspectiveShortcut("org.eclipse.jdt.ui.JavaPerspective");
		
		layout.addActionSet("InterfazAOD.actionSet");
	}

	/**
	 * Defines the layout of the perspective (where and which views are available).
	 * 
	 * @param layout the page layout
	 * 
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
	public void defineLayout(IPageLayout layout) {
		// Editors are placed for free.
		String editorArea = layout.getEditorArea();

		// Place navigator to the left of editor area.
		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, (float) 0.26, editorArea);
		left.addView(IPageLayout.ID_RES_NAV);

		// place outline under the navigator
		IFolderLayout left_middle = layout.createFolder("left_middle_under", IPageLayout.BOTTOM, (float) 0.26, "left");
		left_middle.addView(IPageLayout.ID_OUTLINE);
		
		// Place birdview under the navigator
		IFolderLayout left_bottom = layout.createFolder("left_under", IPageLayout.BOTTOM, (float) 0.70, "left_middle_under");
		left_bottom.addView(IPapyrusUIConstants.BIRDVIEW_ID);
		

		// place properties under the editor
		IFolderLayout bottom = layout.createFolder("under", IPageLayout.BOTTOM, (float) 0.70, editorArea);
		bottom.addView(IPageLayout.ID_PROP_SHEET);
		// bottom.addView("org.eclipse.pde.runtime.LogView");
	}

	
}
