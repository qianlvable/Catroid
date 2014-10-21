/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2014 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.test.mindstorms.nxt;

import android.test.AndroidTestCase;

import org.catrobat.catroid.lego.mindstorm.MindstormCommand;
import org.catrobat.catroid.lego.mindstorm.nxt.CommandByte;
import org.catrobat.catroid.lego.mindstorm.nxt.CommandType;
import org.catrobat.catroid.lego.mindstorm.nxt.NXTMotor;

public class MotorTest extends AndroidTestCase {

	private NXTMotor motor;
	private MindstormTestConnection connection;
	private static final byte DIRECT_COMMAND_HEADER = (byte)(CommandType.DIRECT_COMMAND.getByte() | 0x80);

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.connection = new MindstormTestConnection();
		this.motor = new NXTMotor(0, connection);
	}

	public void testSimpleMotorTest() {

		motor.move(50,360); //calls send
		//CommandByte.java
		//Set_Output_State überprüfen... aus PDF
		MindstormCommand command = connection.getLastSentCommand();
		assertEquals(DIRECT_COMMAND_HEADER,command.getRawCommand()[0]);
		assertEquals(CommandByte.SET_OUTPUT_STATE.getByte(),command.getRawCommand()[1]);
	}

	public void testMotorTestNegativeSpeed() {

		motor.move(-30,360);
		MindstormCommand command = connection.getLastSentCommand();
		assertEquals(DIRECT_COMMAND_HEADER,command.getRawCommand()[3]);
		assertEquals(CommandByte.SET_OUTPUT_STATE.getByte(), command.getRawCommand()[3]);
	}
	//Tests: move mit 0, negativer Geschwindigkeit, > 100 usw.

	public void testMotorWithZeroValues() {

		motor.move(0,0);
		MindstormCommand command = connection.getLastSentCommand();
		assertEquals(DIRECT_COMMAND_HEADER,command.getRawCommand()[0]);
		assertEquals(CommandByte.SET_OUTPUT_STATE.getByte(), command.getRawCommand()[1]);
	}

	public void testMotorWithSpeedOverHundredPercent() {
		motor.move(200,360);
		MindstormCommand command = connection.getLastSentCommand();
		assertEquals(DIRECT_COMMAND_HEADER,command.getRawCommand()[0]);
		assertEquals(CommandByte.SET_OUTPUT_STATE.getByte(), command.getRawCommand()[1]);

	}
}
