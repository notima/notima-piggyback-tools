package org.notima.piggyback.test;

/**
 * 
 * Copyright 2020 Notima System Integration AB (Sweden)
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
import java.util.List;

import org.junit.Test;
import org.notima.piggyback.FieldRider;
import org.notima.piggyback.FieldRiderKeyValuePair;

/**
 * Class to test blowfish encryption	 
 * 
 * @author Daniel Tamm
 *
 */
public class TestFieldRider extends PiggybackTest {

	// File to test
	private String	FILE_TO_TEST = "fieldToRide.txt";
	

	@Test
	public void testFieldRider() {
		
		// Get file to encrypt
		URL url = ClassLoader.getSystemResource(FILE_TO_TEST);
		
		if (url==null) {
			fail("The file " + FILE_TO_TEST + "  must exist in classpath for unit tests to work.");
		}

		String fieldToRide = "";
		List<FieldRiderKeyValuePair> result = null;
		
		try {
			
			fieldToRide = new String (Files.readAllBytes(Paths.get(url.getFile())));
			
			FieldRider rider = new FieldRider(fieldToRide);
			
			result = rider.parseContent();
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
	}

}
