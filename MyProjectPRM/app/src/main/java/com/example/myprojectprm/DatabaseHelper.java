package com.example.myprojectprm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myapp.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_USERS = "users";
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_BILLS = "bills";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    private static final String COLUMN_FULLNAME = "fullName";

    private static final String COLUMN_ADDRESS = "address";

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
    private static final String COLUMN_BILL_ORDER_DATE = "order_date";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_PASSWORD + " TEXT," +
                COLUMN_FULLNAME + " TEXT," +
                COLUMN_ADDRESS + " TEXT" +
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
                COLUMN_BILL_ORDER_DATE + " TEXT," +
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

    //register ng dung trong ham register
    public void addUser(String username, String password, String fullName, String address, Context context) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, username);
            values.put(COLUMN_PASSWORD, password);
            values.put(COLUMN_FULLNAME, fullName);
            values.put(COLUMN_ADDRESS, address);

            db.insert(TABLE_USERS, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }


    //check username ton tai trong ham register
    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

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
    public long addBill(int productId, int quantity, int userId, double totalPrice, Date orderDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BILL_PRODUCT_ID, productId);
        values.put(COLUMN_BILL_QUANTITY, quantity);
        values.put(COLUMN_BILL_USER_ID, userId);
        values.put(COLUMN_BILL_TOTAL_PRICE, totalPrice);
        values.put(COLUMN_BILL_ORDER_DATE, orderDate.getTime());
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

    public String findProductNameByProductId(int productId) {
        String productName = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_PRODUCT_NAME +
                " FROM " + TABLE_PRODUCTS +
                " WHERE " + COLUMN_PRODUCT_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productId)});

        if (cursor.moveToFirst()) {
            productName = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME));
        }

        cursor.close();
        db.close();

        return productName;
    }


    public List<Bill> getBillsByUserId(int userId) {
        List<Bill> billList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_BILLS +
                " WHERE " + COLUMN_BILL_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int billId = cursor.getInt(cursor.getColumnIndex(COLUMN_BILL_ID));
                int productId = cursor.getInt(cursor.getColumnIndex(COLUMN_BILL_PRODUCT_ID));
                int quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_BILL_QUANTITY));
                double totalPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_BILL_TOTAL_PRICE));
                long orderDateTimestamp = cursor.getLong(cursor.getColumnIndex(COLUMN_BILL_ORDER_DATE));
                Date orderDate = new Date(orderDateTimestamp);

                // Create a Bill object with the retrieved data
                Bill bill = new Bill(billId, productId, quantity, userId, totalPrice, orderDate);
                billList.add(bill);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return billList;
    }



}

