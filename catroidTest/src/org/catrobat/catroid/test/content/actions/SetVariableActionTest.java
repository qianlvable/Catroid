/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2013 The Catrobat Team
 *  (<http://developer.catrobat.org/credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://developer.catrobat.org/license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.test.content.actions;

import android.test.AndroidTestCase;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.bricks.SetVariableBrick;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.UserVariable;

public class SetVariableActionTest extends AndroidTestCase {

	private static final String TEST_USERVARIABLE = "testUservariable";
	private static final double SET_VARIABLE_VALUE = 17;
	private static final double INITIALIZED_VALUE = 0.0;
	private Sprite testSprite;
	private StartScript testScript;
	private Project project;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		testSprite = new Sprite("testSprite");
		project = new Project(null, "testProject");
		testScript = new StartScript(testSprite);
		ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentSprite(testSprite);
		ProjectManager.getInstance().setCurrentScript(testScript);
		ProjectManager.getInstance().getCurrentProject().getUserVariables().deleteUserVariableByName(TEST_USERVARIABLE);
		ProjectManager.getInstance().getCurrentProject().getUserVariables().addProjectUserVariable(TEST_USERVARIABLE);
	}

	public void testSetVariableWithNumericalFormula() {

		UserVariable userVariable = ProjectManager.getInstance().getCurrentProject().getUserVariables()
				.getUserVariable(TEST_USERVARIABLE, null);

		SetVariableBrick setBrick = new SetVariableBrick(testSprite, new Formula(SET_VARIABLE_VALUE), userVariable);
		testScript.addBrick(setBrick);
		testSprite.addScript(testScript);
		project.addSprite(testSprite);
		testSprite.createStartScriptActionSequence();
		testSprite.look.act(1f);

		userVariable = ProjectManager.getInstance().getCurrentProject().getUserVariables()
				.getUserVariable(TEST_USERVARIABLE, null);
		assertEquals("Variable not changed", SET_VARIABLE_VALUE, userVariable.getValue());
	}

	public void testSetVariableWithInvalidUserVariable() {

		SetVariableBrick setBrick = new SetVariableBrick(testSprite, SET_VARIABLE_VALUE);
		testScript.addBrick(setBrick);
		testSprite.addScript(testScript);
		project.addSprite(testSprite);
		ProjectManager.getInstance().setCurrentSprite(testSprite);
		testSprite.createStartScriptActionSequence();
		testSprite.look.act(1f);

		UserVariable userVariable = ProjectManager.getInstance().getCurrentProject().getUserVariables()
				.getUserVariable(TEST_USERVARIABLE, null);
		assertEquals("Variable changed, but should not!", INITIALIZED_VALUE, userVariable.getValue());
	}

	public void testSetVariableWithNumericalStringFormula() {
		UserVariable userVariable = ProjectManager.getInstance().getCurrentProject().getUserVariables()
				.getUserVariable(TEST_USERVARIABLE, null);

		String myString = "155";
		SetVariableBrick setVariableBrick = new SetVariableBrick(testSprite, new Formula(myString), userVariable);
		testScript.addBrick(setVariableBrick);
		testSprite.addScript(testScript);
		project.addSprite(testSprite);
		testSprite.createStartScriptActionSequence();
		testSprite.look.act(1f);

		userVariable = ProjectManager.getInstance().getCurrentProject().getUserVariables()
				.getUserVariable(TEST_USERVARIABLE, null);
		assertEquals("String UserVariable not changed!", Double.valueOf(myString), userVariable.getValue());
	}

	public void testSetVariableWithStringFormula() {
		UserVariable userVariable = ProjectManager.getInstance().getCurrentProject().getUserVariables()
				.getUserVariable(TEST_USERVARIABLE, null);

		String myString = "myString";
		Formula validFormula = new Formula(myString);
		SetVariableBrick setVariableBrick = new SetVariableBrick(testSprite, validFormula, userVariable);
		testScript.addBrick(setVariableBrick);
		testSprite.addScript(testScript);
		project.addSprite(testSprite);
		testSprite.createStartScriptActionSequence();
		testSprite.look.act(1f);

		userVariable = ProjectManager.getInstance().getCurrentProject().getUserVariables()
				.getUserVariable(TEST_USERVARIABLE, null);
		assertEquals("String UserVariable not changed!", myString, (String) userVariable.getValue());
	}

	public void testNullFormula() {
		UserVariable userVariable = ProjectManager.getInstance().getCurrentProject().getUserVariables()
				.getUserVariable(TEST_USERVARIABLE, null);

		SetVariableBrick setVariableBrick = new SetVariableBrick(testSprite, null, userVariable);
		testScript.addBrick(setVariableBrick);
		testSprite.addScript(testScript);
		project.addSprite(testSprite);
		testSprite.createStartScriptActionSequence();
		testSprite.look.act(1f);

		userVariable = ProjectManager.getInstance().getCurrentProject().getUserVariables()
				.getUserVariable(TEST_USERVARIABLE, null);
		assertEquals("String UserVariable not changed!", 0d, userVariable.getValue());
	}

	public void testNotANumberFormula() {
		UserVariable userVariable = ProjectManager.getInstance().getCurrentProject().getUserVariables()
				.getUserVariable(TEST_USERVARIABLE, null);

		SetVariableBrick setVariableBrick = new SetVariableBrick(testSprite, new Formula(Double.NaN), userVariable);
		testScript.addBrick(setVariableBrick);
		testSprite.addScript(testScript);
		project.addSprite(testSprite);
		testSprite.createStartScriptActionSequence();
		testSprite.look.act(1f);

		userVariable = ProjectManager.getInstance().getCurrentProject().getUserVariables()
				.getUserVariable(TEST_USERVARIABLE, null);
		assertEquals("String UserVariable not changed!", Double.NaN, userVariable.getValue());
	}
}