package com.example.projectprm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myapp.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_USERS = "users";
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_BILLS = "bills";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    // Product table columns
    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_PRODUCT_NAME = "product_name";
    private static final String COLUMN_PRODUCT_PRICE = "product_price";
    private static final String COLUMN_PRODUCT_IMAGE = "product_image";
    private static final String COLUMN_PRODUCT_DESCRIPTION = "product_description";

    // Bill table columns
    private static final String COLUMN_BILL_ID = "bill_id";
    private static final String COLUMN_BILL_PRODUCT_ID = "product_id";
    private static final String COLUMN_BILL_QUANTITY = "quantity";
    private static final String COLUMN_BILL_USER_ID = "user_id";
    private static final String COLUMN_BILL_TOTAL_PRICE = "total_price";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_PASSWORD + " TEXT" +
                ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Create the products table
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS +
                "(" +
                COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_PRODUCT_NAME + " TEXT," +
                COLUMN_PRODUCT_IMAGE + " INTEGER," +
                COLUMN_PRODUCT_PRICE + " TEXT," +
                COLUMN_PRODUCT_DESCRIPTION + " TEXT" +
                ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        // Create the bills table
        String CREATE_BILLS_TABLE = "CREATE TABLE " + TABLE_BILLS +
                "(" +
                COLUMN_BILL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_BILL_PRODUCT_ID + " INTEGER," +
                COLUMN_BILL_QUANTITY + " INTEGER," +
                COLUMN_BILL_USER_ID + " INTEGER," +
                COLUMN_BILL_TOTAL_PRICE + " REAL," +
                "FOREIGN KEY(" + COLUMN_BILL_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + ")," +
                "FOREIGN KEY(" + COLUMN_BILL_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" +
                ")";
        db.execSQL(CREATE_BILLS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public boolean checkUser(String username, String password) {
        String[] columns = {COLUMN_USER_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(
                TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }
    public long addBill(int productId, int quantity, int userId, double totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BILL_PRODUCT_ID, productId);
        values.put(COLUMN_BILL_QUANTITY, quantity);
        values.put(COLUMN_BILL_USER_ID, userId);
        values.put(COLUMN_BILL_TOTAL_PRICE, totalPrice);
        long billId = db.insert(TABLE_BILLS, null, values);
        db.close();
        return billId;
    }

    public int getUserIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = -1; // Default value if no user found

        String query = "SELECT " + COLUMN_USER_ID +
                " FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
        }

        cursor.close();
        db.close();

        return userId;
    }


}

