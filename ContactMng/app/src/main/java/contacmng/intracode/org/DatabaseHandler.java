package contacmng.intracode.org;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String
            DATABASE_NAME  = "contactManager",
            TABLE_CONTACTS = "contacts",
            KEY_ID         = "id",
            KEY_NAME       = "name",
            KEY_PHONE      = "phone",
            KEY_EMAIL      = "email",
            KEY_ADDRESS    = "address",
            KEY_IMAGEURI   = "imageUri";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(String.format("CREATE TABLE %s( " +
                "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TEXT )",TABLE_CONTACTS,KEY_ID, KEY_NAME, KEY_PHONE, KEY_EMAIL,KEY_ADDRESS, KEY_IMAGEURI));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS"+ TABLE_CONTACTS);
        onCreate(db);
    }

    public void createContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME,contact.getNameUser());
        values.put(KEY_PHONE,contact.getPhone());
        values.put(KEY_EMAIL,contact.getEmail());
        values.put(KEY_ADDRESS,contact.getAddress());
        values.put(KEY_IMAGEURI,contact.getImgURI().toString());
        db.insert(TABLE_CONTACTS,null,values);
        db.close();
    }

    public Contact getContact(int id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS,new String[] {KEY_ID,KEY_NAME,KEY_PHONE,KEY_EMAIL,KEY_ADDRESS,KEY_IMAGEURI},KEY_ID +"=?",new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor != null) {
            cursor.moveToFirst();
            Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), Uri.parse(cursor.getString(5)));
            cursor.close();
            db.close();
            return contact;
        }
        db.close();
        return null;
    }

    public void deleteContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CONTACTS,KEY_ID + "=?",new String[]{String.valueOf(contact.getId())});
        db.close();
        db.close();
    }

    public int getContactsCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_CONTACTS,null);
        int cnt = cursor.getCount();
        cursor.close();
        db.close();
        return cnt;
    }

    public int updateContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME,contact.getNameUser());
        values.put(KEY_PHONE,contact.getPhone());
        values.put(KEY_EMAIL,contact.getEmail());
        values.put(KEY_ADDRESS,contact.getAddress());
        values.put(KEY_IMAGEURI,contact.getImgURI().toString());
        int ret = db.update(TABLE_CONTACTS,values,KEY_ID + "=?", new String[]{String.valueOf(contact.getId())});
        db.close();
        return ret;
    }

    public List<Contact> getAllContacts(){
        List<Contact> contacts = new ArrayList<Contact>();
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_CONTACTS, null);
        if(cursor.moveToFirst()){
            do{
                Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), Uri.parse(cursor.getString(5)));
                contacts.add(contact);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return  contacts;
    }

}
