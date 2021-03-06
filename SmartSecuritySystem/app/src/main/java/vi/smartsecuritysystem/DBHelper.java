package vi.smartsecuritysystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.provider.BaseColumns;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by victoria on 9/7/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6;

    private static final String DATABASE_NAME = "usersManager.db";
    private static final String TABLE_USERS = "users";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADMIN = "admin";
    private static final String KEY_FAMILY = "family";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IMAGE = "image";

    public static final String SALT = "my-salt-text";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_ADMIN + " INTEGER," + KEY_FAMILY + " INTEGER," + KEY_EMAIL
                + " TEXT UNIQUE," + KEY_PASSWORD + " TEXT," + KEY_IMAGE + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again
        onCreate(db);
    }


    // Adding new user
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getID());
        values.put(KEY_NAME, user.getName()); // User Name
        if(user.isAdmin()==false) {
            values.put(KEY_ADMIN, 0); // Is Admin
        }
        else{
            values.put(KEY_ADMIN, 1); // Is Admin
        }
        if(user.isFamily()==false) {
            values.put(KEY_FAMILY, 0); // Is Admin
        }
        else{
            values.put(KEY_FAMILY, 1); // Is Admin
        }
        values.put(KEY_EMAIL,user.getEmail());
        values.put(KEY_PASSWORD,user.getPassword());
        values.put(KEY_IMAGE,user.getImage());

        long rowID = db.insert(TABLE_USERS, null, values);
        //db.close(); // Closing database connection
    }

    // Getting single user
    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID, KEY_NAME, KEY_ADMIN,
                KEY_FAMILY,KEY_EMAIL,KEY_PASSWORD,KEY_IMAGE}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        boolean admin = (cursor.getInt(2) != 0);
        boolean family = (cursor.getInt(3) != 0);

        User user = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), admin, family, cursor.getString(4),cursor.getString(5),cursor.getString(6));
        // return user
        return user;
    }

    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE EMAIL='"+email+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        boolean admin = (cursor.getInt(2) != 0);
        boolean family = (cursor.getInt(3) != 0);

        User user = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), admin, family, cursor.getString(4),cursor.getString(5),cursor.getString(6));
        // return user
        return user;
    }

    // Getting All Users
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setID(Integer.parseInt(cursor.getString(0)));
                user.setName(cursor.getString(1));
                user.setAdmin(Integer.parseInt(cursor.getString(2)) != 0);
                user.setFamily(Integer.parseInt(cursor.getString(3)) != 0);
                user.setEmail(cursor.getString(4));
                user.setPassword(cursor.getString(5));
                user.setImage(cursor.getString(6));
                // Adding contact to list
                userList.add(user);
            } while (cursor.moveToNext());
        }

        // return contact list
        return userList;
    }

    public int getID(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT MAX(id) FROM " + TABLE_USERS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        int id = cursor.getInt(0);
        return id+1;
    }

    // Getting users Count
    public int getUsersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating single user
    public long updateUser(User user) {
        // Updating single user
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //might need to put ID in values too
        values.put(KEY_NAME, user.getName());
        if(user.isAdmin()==false) {
            values.put(KEY_ADMIN, 0); // Is Admin
        }
        else{
            values.put(KEY_ADMIN, 1); // Is Admin
        }
        if(user.isFamily()==false) {
            values.put(KEY_FAMILY, 0); // Is Admin
        }
        else{
            values.put(KEY_FAMILY, 1); // Is Admin
        }
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_IMAGE, user.getImage());

        // updating row
        long rowID =  db.update(TABLE_USERS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(user.getID())});

        return rowID;
    }

    // Deleting single user
    public void deleteUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_EMAIL + " = ?",
                new String[] { String.valueOf(email) });
        //db.close();
    }

    public boolean emailExists(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT email FROM " + TABLE_USERS + " WHERE email='"+email+"'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        //cursor.close();

        if(cursor.getCount()!=0){
            return true;
        }
        else{
            return false;
        }
    }

    public static String generateHash(String input) {
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(input.getBytes());
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f' };
            for (int idx = 0; idx < hashedBytes.length; ++idx) {
                byte b = hashedBytes[idx];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            // handle error here.
        }

        return hash.toString();
    }

}
