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


/**
 * A class that stores a key-value pair 
 * 
 * @author Daniel Tamm
 *
 */
public class FieldRiderKeyValuePair {

	private String	key;
	private String	value;
	
	public FieldRiderKeyValuePair() {}
	
	public FieldRiderKeyValuePair(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Sets the value as an encrypted value using the given passkey.
	 * 
	 * @param passkey		The encryption key
	 * @param value			The value to encrypt
	 */
	public void setEncryptedBlowfishValue(String passkey, String value) {
		this.value = EncodeEncryptUtil.encrypt(passkey, value);
	}
	
	/**
	 * Returns the unencrypted value using the given key.
	 * 
	 * @param passkey	The key to use for unencryption.
	 * @return		The value with applied blowfish unencryption.
	 */
	public String getUnencryptedBlowfishValue(String passkey) {
		
		String result = EncodeEncryptUtil.decrypt(passkey, value);
		return result;
		
	}

	public String toString() {
		return ((key!=null ? key : "null") + " = " + (value!=null ? value : "null"));
	}
	
	/**
	 * Returns the key value formatted as content.
	 * 
	 * @param 	fieldIndicator		Field indicator to use.
	 * @return	Content
	 */
	public String toContent(String fieldIndicator) {
		return fieldIndicator + key + "=" + value + fieldIndicator;
	}
	
}
