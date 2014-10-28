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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.AndroidTestCase;

import org.catrobat.catroid.R;
import org.catrobat.catroid.lego.mindstorm.MindstormConnection;
import org.catrobat.catroid.lego.mindstorm.nxt.LegoNXT;
import org.catrobat.catroid.lego.mindstorm.nxt.LegoNXTImpl;
import org.catrobat.catroid.lego.mindstorm.nxt.sensors.NXTI2CUltraSonicSensor;
import org.catrobat.catroid.lego.mindstorm.nxt.sensors.NXTLightSensor;
import org.catrobat.catroid.lego.mindstorm.nxt.sensors.NXTSoundSensor;
import org.catrobat.catroid.lego.mindstorm.nxt.sensors.NXTTouchSensor;
import org.catrobat.catroid.test.utils.Reflection;
import org.catrobat.catroid.ui.SettingsActivity;

public class LegoNXTImplTest extends AndroidTestCase {

	private Context applicationContext;
	private SharedPreferences preferences;

	private static final int PREFERENCES_SAVE_DELAY = 50;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		applicationContext = this.getContext().getApplicationContext();
		preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
	}

	private void setSensor(SharedPreferences.Editor editor, String sensor, int sensorType) {
		editor.putString(sensor, applicationContext.getString(sensorType));
	}

	public void testSensorAssignment() throws InterruptedException {
		LegoNXT nxt = new LegoNXTImpl(applicationContext);
		MindstormConnection connection = new MindstormTestConnection();
		Reflection.setPrivateField(nxt, "mindstormConnection", connection);

		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();

		setSensor(editor, SettingsActivity.NXT_SENSOR_1, R.string.nxt_sensor_light);
		setSensor(editor, SettingsActivity.NXT_SENSOR_2, R.string.nxt_sensor_sound);
		setSensor(editor, SettingsActivity.NXT_SENSOR_3, R.string.nxt_sensor_touch);
		setSensor(editor, SettingsActivity.NXT_SENSOR_4, R.string.nxt_sensor_ultrasonic);

		editor.apply();
		Thread.sleep(PREFERENCES_SAVE_DELAY); // Preferences need some time to get saved


		assertFalse("Connection should not yet be in connected state.", connection.isConnected());
		nxt.initialise();
		assertTrue("Connection should be in connected state.", connection.isConnected());

		assertNotNull("Motor A not initialized correctly", nxt.getMotorA());
		assertNotNull("Motor B not initialized correctly", nxt.getMotorB());
		assertNotNull("Motor C not initialized correctly", nxt.getMotorC());

		assertNotNull("Sensor 1 not initialized correctly", nxt.getSensor1());
		assertTrue("Sensor 1 is of wrong instance, SensorFactory may has an error",
				nxt.getSensor1() instanceof NXTLightSensor);

		assertNotNull("Sensor 2 not initialized correctly", nxt.getSensor2());
		assertTrue("Sensor 1 is of wrong instance, SensorFactory may has an error",
				nxt.getSensor2() instanceof NXTSoundSensor);

		assertNotNull("Sensor 3 not initialized correctly", nxt.getSensor3());
		assertTrue("Sensor 1 is of wrong instance, SensorFactory may has an error",
				nxt.getSensor3() instanceof NXTTouchSensor);

		assertNotNull("Sensor 4 not initialized correctly", nxt.getSensor4());
		assertTrue("Sensor 1 is of wrong instance, SensorFactory may has an error",
				nxt.getSensor4() instanceof NXTI2CUltraSonicSensor);
	}

	public void testSensorAssignmentChange() throws InterruptedException {
		LegoNXT nxt = new LegoNXTImpl(applicationContext);
		MindstormConnection connection = new MindstormTestConnection();
		Reflection.setPrivateField(nxt, "mindstormConnection", connection);

		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.apply();
		Thread.sleep(PREFERENCES_SAVE_DELAY); // Preferences need some time to get saved

		nxt.initialise();

		assertNull("Sensor 1 should not be initialized", nxt.getSensor1());

		editor = preferences.edit();
		setSensor(editor, SettingsActivity.NXT_SENSOR_1, R.string.nxt_sensor_light);
		editor.apply();
		Thread.sleep(PREFERENCES_SAVE_DELAY); // Preferences need some time to get saved

		assertNotNull("Sensor 1 not initialized correctly", nxt.getSensor1());
		assertTrue("Sensor 1 is of wrong instance, SensorFactory may has an error",
				nxt.getSensor1() instanceof NXTLightSensor);

		editor = preferences.edit();
		setSensor(editor, SettingsActivity.NXT_SENSOR_1, R.string.nxt_sensor_touch);
		editor.apply();
		Thread.sleep(PREFERENCES_SAVE_DELAY); // Preferences need some time to get saved

		assertNotNull("Sensor 1 not initialized correctly", nxt.getSensor1());
		assertTrue("Sensor 1 is of wrong instance, SensorFactory may has an error",
				nxt.getSensor1() instanceof NXTTouchSensor);
	}
}
