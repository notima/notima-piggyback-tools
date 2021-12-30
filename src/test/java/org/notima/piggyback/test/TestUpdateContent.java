package org.notima.piggyback.test;

/**
 * 
 * Copyright 2020-2022 Notima System Integration AB (Sweden)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import static org.junit.Assert.fail;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.notima.piggyback.FieldRider;
import org.notima.piggyback.FieldRiderKeyValuePair;

/**
 * Class to test blowfish encryption	 
 * 
 * @author Daniel Tamm
 *
 */
public class TestUpdateContent extends PiggybackTest {

	// File to test
	private String	FILE_TO_TEST = "fieldToRide.txt";
	private String	KEY_TO_TEST = "riderkey";
	

	@Test
	public void testFieldRider() {
		
		// Get file to encrypt
		URL url = ClassLoader.getSystemResource(FILE_TO_TEST);
		
		if (url==null) {
			fail("The file " + FILE_TO_TEST + "  must exist in classpath for unit tests to work.");
		}

		String fieldToRide = "";
		
		try {
			
			fieldToRide = new String (Files.readAllBytes(Paths.get(url.getFile())));
			
			FieldRider rider = new FieldRider(fieldToRide);
			
			rider.setKeyValuePairs(rider.parseContent());
			FieldRiderKeyValuePair kv = rider.lookupKeyValuePair(KEY_TO_TEST);
			
			if (kv!=null) {
				kv.setValue("New value");
				StringBuffer buf = rider.updateContent();
				System.out.println(buf.toString());
			} else {
				fail("Missing key: " + KEY_TO_TEST);
			}
			
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
	}

}
