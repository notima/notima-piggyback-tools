package org.notima.piggyback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;

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


/**
 * Class used to simplify free riding data in a text field.
 * 
 * @author Daniel Tamm
 *
 */
public class FieldRider {

	private String defaultFieldIndicator = "¤";
	private StringBuffer content;
	
	private List<FieldRiderKeyValuePair>	keyValuePairs;
	
	private Map<String, FieldRiderKeyValuePair> 	keys;	
	
	protected Logger	log = org.slf4j.LoggerFactory.getLogger(this.getClass());	
	
	/**
	 * Empty constructor
	 */
	public FieldRider() {}
	
	/**
	 * Creates a new FieldRider instance with given content, parses the content
	 * and sets the key value pairs.
	 * 
	 * @param fieldContent		The contents of the field.
	 */
	public FieldRider(String fieldContent) {
		content = new StringBuffer(fieldContent);
		setKeyValuePairs(parseContent());
	}

	public String getDefaultFieldIndicator() {
		return defaultFieldIndicator;
	}

	public void setDefaultFieldIndicator(String defaultFieldIndicator) {
		this.defaultFieldIndicator = defaultFieldIndicator;
	}

	public StringBuffer getContent() {
		return content;
	}

	public void setContent(StringBuffer content) {
		this.content = content;
	}

	public List<FieldRiderKeyValuePair> getKeyValuePairs() {
		return keyValuePairs;
	}

	public void setKeyValuePairs(List<FieldRiderKeyValuePair> keyValuePairs) {
		this.keyValuePairs = keyValuePairs;
	}

	
	/**
	 * Updates internal key map if keys are null.
	 * 
	 * @param  refresh	Refresh map.
	 */
	private void updateKeys(boolean refresh) {
		if (keys==null) {
			keys = new TreeMap<String, FieldRiderKeyValuePair>();
			refresh = true;
		}
		if (keyValuePairs!=null) {
			if (refresh) {
				for (FieldRiderKeyValuePair kv : keyValuePairs) {
					keys.put(kv.getKey(), kv);
				}
			}
		} else {
			keys.clear();
		}
		
	}

	/**
	 * Adds a key value pair to this instance.
	 * 
	 * @param kvp		The key value pair to add
	 */
	public void addKeyValuePair(FieldRiderKeyValuePair kvp) {
		if (keyValuePairs==null) {
			keyValuePairs = new ArrayList<FieldRiderKeyValuePair>();
		}
		keyValuePairs.add(kvp);
	}
	
	/**
	 * Lookup key value pair.
	 * 
	 * @param key		The key to look for
	 * @return	The pair if found, otherwise null.
	 */
	public FieldRiderKeyValuePair lookupKeyValuePair(String key) {
		updateKeys(false);
		FieldRiderKeyValuePair result = keys.get(key);
		return result;
	}
	
	/**
	 * Creates an updated content with current key value pairs.
	 * The actual content in getContent is not touched. Use setContent to set the content.
	 * 
	 * @return		Content with updates
	 */
	public StringBuffer updateContent() {

		List<String> unrelatedContent = new ArrayList<String>();
		List<Integer> fieldIndexes = findFieldIndexes(defaultFieldIndicator, content.toString());
		
		// Update the key tree with the new fields
		updateKeys(true);
		
		// Save unrelated content
		int startPos = 0;
		int endPos;
		boolean startsWithUnrelated = true;
		for (int i = 0; i < fieldIndexes.size() + 1 ; i += 2) {
			if (fieldIndexes.size()==0 || i == fieldIndexes.size()) {
				endPos = content.length();
			} else {
				endPos = fieldIndexes.get(i)-1;
			}
			if (endPos>0) {
				unrelatedContent.add(content.substring(startPos, endPos));
			} else {
				startsWithUnrelated = false;
			}
			if (i < fieldIndexes.size() )
				startPos = fieldIndexes.get(i+1) + 1;
		}
		
		int u = 0;	// Unrelated counter
		
		StringBuffer updatedContent = new StringBuffer();
		if (startsWithUnrelated) {
			while( u < unrelatedContent.size() )
				updatedContent.append(unrelatedContent.get(u++));
		}
		for (FieldRiderKeyValuePair kv : keyValuePairs) {
			if (updatedContent.length()>0) {
				updatedContent.append("\n");
			}
			updatedContent.append(kv.toContent(defaultFieldIndicator));
		}
		if (!startsWithUnrelated) {
			while( u < unrelatedContent.size() )
				updatedContent.append(unrelatedContent.get(u++));
		}
		
		return updatedContent;
	}
	

	/**
	 * Find field indicator indexes.
	 * 
	 * @param fieldIndicator	The indicator of beginning and end of field.
	 * @param txt				The text to search
	 * @return					A list indexes where there are indicators.
	 */
	private List<Integer> findFieldIndexes(String fieldIndicator, String txt) {

		int fromIndex = 0;
		List<Integer> fieldIndexes = new ArrayList<Integer>();
		
		while ( (fromIndex = txt.indexOf(fieldIndicator, fromIndex)) >= 0 ) {
			fieldIndexes.add(fromIndex);
			fromIndex++;
		}
		
		// Adjust if there are unbalanced indicators
		if (fieldIndexes.size() % 2 == 1) {
			// Remove last
			fieldIndexes.remove(fieldIndexes.size()-1);
			log.warn("Unbalanced field indicators");
		}
		
		return fieldIndexes;
	}
	
	/**
	 * Returns current settings as a map with keys as keys and values as values.
	 * 
	 * @return	A map of settings.
	 */
	public Map<String, String> getSettingsMap() {
		
		Map<String, String> result = new TreeMap<String,String>();
		
		updateKeys(true);
		
		for (String key : keys.keySet()) {
			result.put(key, keys.get(key).getValue());
		}
		
		return result;
	}
	
	/**
	 * Parses the content for free riding key-value pairs
	 * This method doesn't touch the data in getKeyValuePairs
	 * 
	 * @return	A list of key value pairs (if any).
	 */
	public List<FieldRiderKeyValuePair> parseContent() {
		
		List<FieldRiderKeyValuePair> result = new ArrayList<FieldRiderKeyValuePair>();;

		if (content==null) {
			log.debug("Content is empty");
			return keyValuePairs;
		}

		// Find field indicator indexes
		List<Integer> fieldIndexes = findFieldIndexes(defaultFieldIndicator, content.toString());

		// If no indicators are found, return empty result
		if (fieldIndexes.size()==0) {
			return result;
		}

		List<String> fields = new ArrayList<String>();
		String field;
		
		for (int i = 0 ; i < fieldIndexes.size(); i += 2) {
			field = content.substring(fieldIndexes.get(i), fieldIndexes.get(i+1)+1);
			fields.add(field);
		}
		
		Pattern scanPattern = getScanPattern(); 
		Matcher m;
		FieldRiderKeyValuePair kv;
		
		for (String f : fields) {
			m = scanPattern.matcher(f);
	
			if (m.matches()) {
				kv = new FieldRiderKeyValuePair(m.group(1), m.group(2));
				result.add(kv);
			}
		}
		
		return result;
	}
	
	/**
	 * Compile scan pattern according to current field indicator
	 * 
	 * @return	A regex pattern.
	 */
	private Pattern getScanPattern() {
		return Pattern.compile(defaultFieldIndicator + "(\\w+)[=](.*?)" + defaultFieldIndicator);
	}
	
}
