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

	public void testSimpleMotorTest() {
		MindstormTestConnection connection = new MindstormTestConnection();
		NXTMotor motor = new NXTMotor(0, connection);

		motor.move(50,360);
		MindstormCommand command = connection.getLastSentCommand();
		//CommandByte.java
		assertEquals((byte)(CommandType.DIRECT_COMMAND.getByte() | 0x80),command.getRawCommand()[0]);
		assertEquals(CommandByte.SET_OUTPUT_STATE.getByte(),command.getRawCommand()[1]);



	}
}
