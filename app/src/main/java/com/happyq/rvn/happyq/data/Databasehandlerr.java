package com.happyq.rvn.happyq.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by RVN on 12/14/2016.
 */
public class Databasehandlerr extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private Context context;

    private static final int DATABASE_VERSION =1;
    private static final String DATABASE_NAME = "apprvndb";
    private static final String TABLE_FB_USER = "users";
    private static final String TABLE_gMAIL_USER = "users";
    private static final String TABLE_USER = "users";
    private static final String TAG = SQLiteOpenHelper.class.getSimpleName();

    //get String
    int _id;
    String _username;
    String _name;
    String _email;
    String _birthday;
    String _gender;
    String _invitecode;
    String _password;
    String _gmail_ID;
    String _gmail_name;
    String _gmail_email;
    String _gmail_firstname;
    String _gmail_lastname;
    String _fb_id;
    String _fb_firstname;
    String _fb_middlename;
    String _fb_lastname;

    String _pwd;

    //table for fb users
    private static final String KEY_FBUID           ="FB_uid";
    private static final String KEY_fb_id           ="FB_ID";
    private static final String KEY_fb_firstname    ="FB_firstname";
    private static final String KEY_fb_middlename   ="FB_middlename";
    private static final String KEY_fb_lastname     ="FB_lastname";

    //table for gmail users

    private static final String KEY_GUID           ="G_uid";
    private static final String KEY_G_id           ="G_ID";
    private static final String KEY_G_name         ="G_name";
    private static final String KEY_G_email        ="G_email";
    private static final String KEY_G_firstname        ="G_firstname";
    private static final String KEY_G_lastname        ="G_lastname";



    //table columns for TABLE_USER
        private static final String KEY_ID          ="user_id";
        private static final String uid             = "uid";
        private static final String KEY_username    ="user_username";
        private static final String KEY_name        ="user_name";
        private static final String KEY_gender      ="user_gender";
        private static final String KEY_birthday    ="user_birthday";
        private static final String KEY_email       ="user_email";
        private static final String KEY_invitecode  ="user_invitecode";
        private static final String KEY_password    ="user_password";
        private static final String KEY_GMAILID     ="user_gmail_id";

    public Databasehandlerr(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableuserlogin(db);
        createTablegmailuserlogin(db);
        createTablefuserlogin(db);

    }


//    private void createTablefbuserlogin(SQLiteDatabase db) {
//        String CREATE_TABLE = "CREATE TABLE " + TABLE_FB_USER + " ( "
//                + KEY_FBUID    + " INTEGER PRIMARY KEY, "
//                + KEY_fb_firstname + " TEXT, "
//                + KEY_fb_middlename + " TEXT, "
//                + KEY_fb_lastname + " TEXT, "
//                + KEY_fb_id + " TEXT "
//                + ")";
//        db.execSQL(CREATE_TABLE);
//    }

    private void createTablefuserlogin(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_FB_USER + " ( "
                + KEY_FBUID + " INTEGER PRIMARY KEY, "
                + KEY_fb_id + " TEXT, "
                + KEY_fb_firstname + " TEXT, "
                +  KEY_fb_lastname + " TEXT "
                + ")";
        db.execSQL(CREATE_TABLE);
    }


    private void createTablegmailuserlogin(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_gMAIL_USER + " ( "
                + KEY_GUID      + " INTEGER PRIMARY KEY, "
                + KEY_G_id      + " TEXT, "
                + KEY_G_email   + " TEXT "
                + KEY_G_firstname   + " TEXT, "
                + KEY_G_lastname   + " TEXT "

                + ")";
        db.execSQL(CREATE_TABLE);
    }

    private void createTableuserlogin(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_USER + " ( "
                + KEY_ID        + " INTEGER PRIMARY KEY, "
                + KEY_username  + " TEXT, "
                + KEY_password  + " TEXT, "
                + KEY_name      + " TEXT, "
                + KEY_gender    + "TEXT, "
                + KEY_birthday  + "TEXT, "
                + KEY_GMAILID   + "TEXT, "
                + KEY_email     + "TEXT, "
                + KEY_invitecode+ "TEXT "
                + ")";
        db.execSQL(CREATE_TABLE);
    }


    public void truncateDB(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER + TABLE_FB_USER + TABLE_gMAIL_USER);
        // Create tables again
        onCreate(db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER + TABLE_FB_USER + TABLE_gMAIL_USER);

        onCreate(db);
    }

    /**
     * Storing user details in database
     * */


    public void addUserGMAILlogin(String email, String gmail_id, String gmail_lastname, String gmail_firstname) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(email, get_gmail_email()); // Email
        //values.put(name, get_gmail_name()); // name
        values.put(gmail_id, get_gmail_ID()); // Created At
        values.put(gmail_firstname, get_gmail_firstname());
        values.put(gmail_lastname, get_gmail_lastname());

        // Inserting Row
        long id = db.insert(TABLE_gMAIL_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void addUserfblogin(String firstname, String lastname, String fb_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(firstname, get_fb_firstname()); //firstname
        values.put(lastname, get_fb_lastname()); // lastname
        values.put(fb_id, get_fb_id()); // facebook_id


        // Inserting Row
        long id = db.insert(TABLE_FB_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void addUserlogin(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(email, getEmail()); // Email
        values.put(password, getPassword()); // password


        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void addUser(String name, String email, String birthday, String gender, String invitecode, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(username, getUsername()); // UserName
        values.put(name, getName()); // Name
        values.put(email, getEmail()); // Email
        values.put(birthday, getBirthday()); // birthday
        values.put(gender, getGender()); // gender
        values.put(invitecode, getInvitecode()); // invite code
        values.put(password, getPassword()); // password ID

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }


    public String getregister(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + KEY_username + " = ? ", new String[]{username});

        if (cursor.getCount() < 1) {
            cursor.close();
            return "Not Exist";
        } else if (cursor.getCount() > 0 && cursor.moveToFirst()) {

            _pwd = cursor.getString(cursor.getColumnIndex(KEY_password));
            cursor.close();
        }
        return _pwd;
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("username", cursor.getString(1));
            user.put("name", cursor.getString(2));
            user.put("email", cursor.getString(3));
            user.put("birthday", cursor.getString(4));
            user.put("gender", cursor.getString(5));
            user.put("invitecode", cursor.getString(6));

            //user.put("created_at", cursor.getString(8));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    public String getUsername() {
        // TODO Auto-generated method stub
        return _username;
    }

    // setting  first name
    public void setUsername(String username){
        this._username =username;
    }


    public String getName() {
        // TODO Auto-generated method stub
        return _username;
    }

    // setting  name
    public void setName(String name){
        this._name =name;
    }


    public String getEmail() {
        // TODO Auto-generated method stub
        return _email;
    }

    public void setEmail(String email){
        this._email = email;
    }

    public String getBirthday() {
        // TODO Auto-generated method stub
        return _birthday;
    }

    public void setBirthday(String birthday){
        this._birthday = birthday;
    }

    public String getGender() {
        // TODO Auto-generated method stub
        return _gender;
    }

    public void setGender(String gender){
        this._gender = gender;
    }

    public String getInvitecode() {
        // TODO Auto-generated method stub
        return _invitecode;
    }

    public void setInvitecode(String password){
        this._password = password;
    }

    public String getPassword() {
        // TODO Auto-generated method stub
        return _invitecode;
    }

    public void setPassword(String password){
        this._password = password;
    }

    public void set_gmail_ID(String gmail_id){
        this._gmail_ID = gmail_id;
    }
    public String get_gmail_ID() {
        // TODO Auto-generated method stub
        return _gmail_ID;
    }


    public void set_gmail_name(String gmail_name){
        this._gmail_name = gmail_name;
    }
    public String get_gmail_name() {
        // TODO Auto-generated method stub
        return _gmail_name;
    }

    public void set_gmail_email(String gmail_email){
        this._gmail_email = gmail_email;
    }
    public String get_gmail_email() {
        // TODO Auto-generated method stub
        return _gmail_email;
    }


    public void set_gmail_firstname(String gmail_firstname){
        this._gmail_email = gmail_firstname;
    }
    public String get_gmail_firstname() {
        // TODO Auto-generated method stub
        return _gmail_firstname;
    }


    public void set_gmail_lastname(String gmail_lastname){
        this._gmail_email = gmail_lastname;
    }
    public String get_gmail_lastname() {
        // TODO Auto-generated method stub
        return _gmail_lastname;
    }


    public void set_fb_id(String fb_id){
        this._fb_id = fb_id;
    }
    public String get_fb_id() {
        // TODO Auto-generated method stub
        return _fb_id;
    }

    public void set_fb_firstname(String fb_firstname){
            this._fb_firstname = fb_firstname;
    }
    public String get_fb_firstname() {
        // TODO Auto-generated method stub
        return _fb_firstname;
    }

    public void set_fb_middlename(String fb_middlename){
        this._fb_middlename = fb_middlename;
    }
    public String get_fb_middlename() {
        // TODO Auto-generated method stub
        return _fb_middlename;
    }

    public void set_fb_lastname(String fb_lastname){
        this._fb_lastname = fb_lastname;
    }
    public String get_fb_lastname() {
        // TODO Auto-generated method stub
        return _fb_lastname;
    }

}
