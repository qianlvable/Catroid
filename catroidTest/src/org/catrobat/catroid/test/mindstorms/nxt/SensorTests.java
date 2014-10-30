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
import org.catrobat.catroid.lego.mindstorm.nxt.sensors.NXTI2CUltraSonicSensor;
import org.catrobat.catroid.lego.mindstorm.nxt.sensors.NXTLightSensor;
import org.catrobat.catroid.lego.mindstorm.nxt.sensors.NXTSensor;
import org.catrobat.catroid.lego.mindstorm.nxt.sensors.NXTSensorMode;
import org.catrobat.catroid.lego.mindstorm.nxt.sensors.NXTSensorType;
import org.catrobat.catroid.lego.mindstorm.nxt.sensors.NXTSoundSensor;
import org.catrobat.catroid.lego.mindstorm.nxt.sensors.NXTTouchSensor;

public class SensorTests extends AndroidTestCase {

	private static final byte DIRECT_COMMAND_WITHOUT_REPLY = (byte) 0x80;
	private static final byte DIRECT_COMMAND_WITH_REPLY = (byte) 0x00;
	private static final byte PORT_NR_0 = 0;
	private static final byte PORT_NR_1 = 1;
	private static final byte PORT_NR_2 = 2;
	private static final byte PORT_NR_3 = 3;

	public void testGetSensorValue() {
		MindstormTestConnection connection = new MindstormTestConnection();
		NXTSensor sensor = new NXTTouchSensor(PORT_NR_0, connection);

		sensor.getValue();
		MindstormCommand command = connection.getLastSentCommand();

		byte[] rawCommand = command.getRawCommand();

		assertEquals("Incorrect Header", DIRECT_COMMAND_WITH_REPLY, rawCommand[0]);
		assertEquals("Wrong Command Byte", CommandByte.GET_INPUT_VALUES.getByte(), rawCommand[1]);
		assertEquals("Wrong port", PORT_NR_0, rawCommand[2]);
	}

	public void testSetSensorModeTouch() {
		MindstormTestConnection connection = new MindstormTestConnection();
		NXTSensor sensor = new NXTTouchSensor(PORT_NR_0, connection);

		sensor.getValue();

		MindstormCommand command = null;
		MindstormCommand firstCommand = connection.getLastSentCommand();
		while(firstCommand != null) {
			command = firstCommand;
			firstCommand = connection.getLastSentCommand();
		}

		byte[] rawCommand = command.getRawCommand();

//		assertEquals(DIRECT_COMMAND_WITHOUT_REPLY, rawCommand[0]);
		assertEquals("Wrong CommandByte", CommandByte.SET_INPUT_MODE.getByte(), rawCommand[1]);
		assertEquals("Wrong Port", PORT_NR_0, rawCommand[2]);
		assertEquals("Wrong sensor type", NXTSensorType.TOUCH.getByte(), rawCommand[3]);
		assertEquals("Wrong sensor mode", NXTSensorMode.BOOL.getByte(), rawCommand[4]);
	}

	public void testSetSensorModeSound() {
		MindstormTestConnection connection = new MindstormTestConnection();
		NXTSensor sensor = new NXTSoundSensor(PORT_NR_1, connection);

		sensor.getValue();

		MindstormCommand command = null;
		MindstormCommand firstCommand = connection.getLastSentCommand();
		while(firstCommand != null) {
			command = firstCommand;
			firstCommand = connection.getLastSentCommand();
		}

		byte[] rawCommand = command.getRawCommand();

//		assertEquals(DIRECT_COMMAND_WITHOUT_REPLY, rawCommand[0]);
		assertEquals("Wrong CommandByte", CommandByte.SET_INPUT_MODE.getByte(), rawCommand[1]);
		assertEquals("Wrong port", PORT_NR_1, rawCommand[2]);
		assertEquals("Wrong sensor Type", NXTSensorType.SOUND_DBA.getByte(), rawCommand[3]);
		assertEquals("Wrong sensor mode", NXTSensorMode.Percent.getByte(), rawCommand[4]);
	}

	public void testSetSensorModeLight() {
		MindstormTestConnection connection = new MindstormTestConnection();
		NXTSensor sensor = new NXTLightSensor(PORT_NR_2, connection);

		sensor.getValue();

		MindstormCommand command = null;
		MindstormCommand firstCommand = connection.getLastSentCommand();
		while(firstCommand != null) {
			command = firstCommand;
			firstCommand = connection.getLastSentCommand();
		}

		byte[] rawCommand = command.getRawCommand();

//		assertEquals(DIRECT_COMMAND_WITHOUT_REPLY, rawCommand[0]);
		assertEquals("Wrong CommandByte", CommandByte.SET_INPUT_MODE.getByte(), rawCommand[1]);
		assertEquals("Wrong port", PORT_NR_2, rawCommand[2]);
		assertEquals("Wrong sensor type", NXTSensorType.LIGHT_INACTIVE.getByte(), rawCommand[3]);
		assertEquals("Wrong sensor mode", NXTSensorMode.Percent.getByte(), rawCommand[4]);
	}

	public void testSetSensorModeUltraSonic() {
		MindstormTestConnection connection = new MindstormTestConnection();
		NXTI2CUltraSonicSensor sensor = new NXTI2CUltraSonicSensor(connection);

		sensor.singleShot(true);
		sensor.getValue();

		MindstormCommand command = null;
		MindstormCommand firstCommand = connection.getLastSentCommand();
		while(firstCommand != null) {
			command = firstCommand;
			firstCommand = connection.getLastSentCommand();
		}

		byte[] rawCommand = command.getRawCommand();

//		assertEquals(DIRECT_COMMAND_WITHOUT_REPLY, rawCommand[0]);
		assertEquals("Wrong command byte", CommandByte.SET_INPUT_MODE.getByte(), rawCommand[1]);
		assertEquals("Wrong port", PORT_NR_3, rawCommand[2]);
		assertEquals("Wrong sensor type", NXTSensorType.LOW_SPEED_9V.getByte(), rawCommand[3]);
		assertEquals("Wrong sensor mode", NXTSensorMode.RAW.getByte(), rawCommand[4]);
	}
}