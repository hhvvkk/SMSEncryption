package com.cos730.database;

import java.util.ArrayList;
import java.util.List;

import com.cos730.encryption.AppSecurity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_NAME = "contactDatabase";

	private static final String TABLE_CONTACTS = "contacts";
	private static final String TABLE_USERS = "users";
	
	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_PHONE_NUMBER = "phone_number";
	private static final String KEY_HIS_SEED = "his_seed";
	private static final String KEY_MY_SEED = "my_seed";
	
	//User TABLE column names
	//private static final String KEY_ID = "id";
	private static final String KEY_USERNAME = "name";
	private static final String KEY_PASSWORD_HASH = "passwordHash";
	
	//user info for encryption
	private static String USERNAME="";
	private static String PASSWORD="";
	
	private AppSecurity security=new AppSecurity();
	
	private Context _context;
	
	public DatabaseHandler(Context context, String name, CursorFactory factory,
			int version) {		
		super(context, name, factory, version);
		_context=context;
	}
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public void setEncryption(String username,String password)
	{
		System.out.println("encryption init");
		System.out.println("intit USERNAME "+username);
		System.out.println("intit PASSWORD "+password);
		USERNAME=username;
		PASSWORD=password;
		
		security.setUsername(USERNAME);
		security.setPassword(PASSWORD);
		
		security.EncryptSetup();
		security.DecryptSetup();
	}
	
	public String getUsername(String password)
	{
		return USERNAME;		
	}
	
	public String getPassword(String password)
	{
			return PASSWORD;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_PHONE_NUMBER + " TEXT,"
				+ KEY_HIS_SEED + " TEXT,"
				+ KEY_MY_SEED + " TEXT"
				+")";
		db.execSQL(CREATE_CONTACTS_TABLE);
		
		String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " TEXT,"
				+ KEY_PASSWORD_HASH + " TEXT"
				+")";
		db.execSQL(CREATE_USER_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		
		
		// Create tables again
		onCreate(db);
	}
	
	// Adding new contact
	public void addContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME,security.Encrypt(contact.getName())); // Contact Name
		values.put(KEY_PHONE_NUMBER,security.Encrypt(contact.getPhoneNumber())); // Contact Phone Number
		values.put(KEY_HIS_SEED,security.Encrypt(contact.getHisSeed())); // his seed
		values.put(KEY_MY_SEED,security.Encrypt(contact.getMySeed())); // my seed

		// Inserting Row
		db.insert(TABLE_CONTACTS, null, values);
		db.close(); // Closing database connection
		
		ContactContent cc=new ContactContent(_context);
		cc.UpdateChanges();
		
	}

	// Adding new contact
	public void addUser(User user) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, user.getName()); // username
		values.put(KEY_PASSWORD_HASH, user.getPasswordHash()); //user password hash value

		// Inserting Row
		db.insert(TABLE_USERS, null, values);
		db.close(); // Closing database connection
	}
	
	// Getting single contact
	public Contact getContact(int id) {
		List<Contact> contactList = getAllContacts();
		
		for(int i = 0; i < contactList.size(); i++){
			if(contactList.get(i).getID() == id)
				return contactList.get(i);
		}
		
		// return contact
		return null;
		
	}

	// Getting All Contacts
	public List<Contact> getAllContacts() {
		List<Contact> contactList = new ArrayList<Contact>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Contact contact = new Contact();
				contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.setName(security.Decrypt(cursor.getString(1)));
				contact.setPhoneNumber(security.Decrypt(cursor.getString(2)));
				contact.setHisSeed(security.Decrypt(cursor.getString(3)));
				contact.setMySeed(security.Decrypt(cursor.getString(4)));
				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}

		//close the database
		db.close();
		
		// return contact list
		return contactList;
		
	}
	
	// Getting All users
	public List<User> getAllUsers() {
			List<User> userList = new ArrayList<User>();
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_USERS;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					User user = new User();
					user.setID(Integer.parseInt(cursor.getString(0)));
					user.setName(cursor.getString(1));
					user.setPasswordHash(cursor.getString(2));
					// Adding contact to list
					userList.add(user);
				} while (cursor.moveToNext());
			}

			//close the database
			db.close();
			
			// return contact list
			return userList;
			
		}

	// Getting contacts Count
	public int getContactsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		//close the db
		db.close();
		
		// return count
		return cursor.getCount();
	}
	
	// Getting contacts Count
	/**
	 * Gets the amount of users in the database
	 * @param closeConnection Closes the connection if set to tru
	 * @return Return the amount of users in the database
	 */
	public int getUsersCount(boolean closeConnection) {
		String countQuery = "SELECT  * FROM " + TABLE_USERS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		//close the db
		if(closeConnection)
			db.close();
		
		// return count
		return cursor.getCount();
	}
	
	// Updating single contact
	public int updateContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME,security.Encrypt(contact.getName()));
		values.put(KEY_PHONE_NUMBER,security.Encrypt( contact.getPhoneNumber()));
		values.put(KEY_HIS_SEED,security.Encrypt( contact.getHisSeed()));
		values.put(KEY_MY_SEED,security.Encrypt( contact.getMySeed()));
		
		// updating row
		int update = db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(contact.getID()) });
		
		//close database
		db.close();
		
		ContactContent cc=new ContactContent(_context);
		cc.UpdateChanges();

		return update;
	}

	// Deleting single contact
	public void deleteContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
				new String[] { String.valueOf(contact.getID()) });
		db.close();
		
		ContactContent cc=new ContactContent(_context);
		cc.UpdateChanges();
		
	}
	
	//change the username and passwordhash
	public void UpdateSettings(User user)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, user.getName()); // username
		values.put(KEY_PASSWORD_HASH, user.getPasswordHash()); //user password hash value
		
		// updating row
		int update = db.update(TABLE_USERS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(user.getID()) });
		
		//close database
		db.close();
	}
	
	public List<String> getAllLabels(){
		List<Contact> list = getAllContacts();
		List<String> stringList = new ArrayList();
		
		for(int i = 0; i < list.size(); i++){
			stringList.add(list.get(i).getName());
		}
		
		return stringList;
	}

}
