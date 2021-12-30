package org.notima.piggyback;

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

import java.util.Base64;

import net.sourceforge.blowfishj.BlowfishEasy;

/**
 * Convenience class for encrypting / decrypting using blowfish / base64.
 * 
 * @author Daniel Tamm
 *
 */
public class EncodeEncryptUtil {

	/**
	 * Decrypts a string using specified key
	 * 
	 * @param key					The key for decryption
	 * @param encryptedMessage		The message
	 * @return						The decrypted message
	 */
	public static String decrypt(String key, String encryptedMessage) {
		
		BlowfishEasy fish = new BlowfishEasy( key.toCharArray() );
		String result = fish.decryptString( encryptedMessage );
		return result;
	}

	/**
	 * Encrypts a string using specified key
	 * 
	 * @param key			The key for encryptiong
	 * @param message		The message to be encrypted
	 * @return				The encrypted message
	 */
	public static String encrypt(String key, String message) {

		BlowfishEasy fish = new BlowfishEasy( key.toCharArray() );
		String result = fish.encryptString(message);
		
		return result;
		
	}
	
	/**
	 * Encodes given message to Base64
	 * 
	 * @param message		An XML-message (plain text) to be encoded.
	 * @return				A base64 encoded message.
	 */
	
	public static String base64encodeMsg(String message) {
		return Base64.getEncoder().encodeToString(message.getBytes());
	}
	
	
	/**
	 * Decodes a base64 message. 
	 * 
	 * @param message		The base64 message.
	 * @return		The message decoded
	 */
	public static String base64decodeMsg(String message) {
		
		StringBuffer buf = new StringBuffer();
		// Strip message from spaces, newlines etc
		char c;
		for (int i = 0; i<message.length(); i++) {
			c = message.charAt(i);
			switch(c) {
				case ' ' :
				case '\n' :
				case '\r' :
				case '\t' :
					continue;
				default:
					buf.append(c);
			}
		}
		String result = new String(Base64.getDecoder().decode(buf.toString())); 
		
		return result;
		
	}
	
}
