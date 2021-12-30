# notima-piggyback-tools
Piggy-backing is a way of using resources meant for something else for your own purpose.

This library aims to make it easier to embed your own data in fields that might be used for something else. This can be useful when you're integrating with systems where you can't easily store data in that system in other fields than the provided fields.

One example is to store own data in a comments or description field on a customer or vendor record in the target system.

To identify your data from the system's own data this library uses a field indicator.

The default field indicator is a ¤ sign. That means that whatever is surrounded by the field indicator are your piggy-backed data.

If you want to store sensitive data in a field that's openly available for users to read (such as a comment field), there's an option to encrypt the data using two-way encryption (blowfish). 

## Usage

Include the following in your pom.xml

```
<dependency>
  <groupId>org.notima</groupId>
  <artifactId>piggyback-tools</artifactId>
  <version>0.0.4-SNAPSHOT</version>
</dependency>
```

The FieldRider class is the class to use to embed your fields in existing fields.

### Read your fields

Create a FieldRider instance using the content of the field as constructor argument. Get my fields as a map.

	FieldRider rider = new FieldRider(descriptionField);
	
	Map<String, FieldRiderKeyValuePair> myFields = rider.getSettingsMap();


More code examples are found in the junit-tests.