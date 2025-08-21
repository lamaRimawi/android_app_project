package com.grocery.store.a1213515_1200209_andriod;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "grocery_store.db";
    private static final String COLUMN_USER_ROLE = "user_role";

    private static final int DATABASE_VERSION = 4;

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_PROFILE_IMAGE = "profile_image";

    // Products table
    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_DISCOUNT = "discount_percentage";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_STOCK = "stock";
    private static final String COLUMN_BRAND = "brand";
    private static final String COLUMN_THUMBNAIL = "thumbnail";

    // Favorites table
    private static final String TABLE_FAVORITES = "favorites";
    private static final String COLUMN_FAV_ID = "fav_id";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_FAV_PRODUCT_ID = "product_id";
    private static final String COLUMN_DATE_ADDED = "date_added";

    // Orders table
    private static final String TABLE_ORDERS = "orders";
    private static final String COLUMN_ORDER_ID = "order_id";
    private static final String COLUMN_ORDER_USER_EMAIL = "user_email";
    private static final String COLUMN_ORDER_PRODUCT_ID = "product_id";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_DELIVERY_METHOD = "delivery_method";
    private static final String COLUMN_ORDER_DATE = "order_date";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_TOTAL_PRICE = "total_price";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EMAIL + " TEXT UNIQUE,"
                + COLUMN_FIRST_NAME + " TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_GENDER + " TEXT,"
                + COLUMN_CITY + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_PROFILE_IMAGE + " TEXT,"
                + COLUMN_USER_ROLE + " TEXT DEFAULT 'user'" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Create products table
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_PRICE + " REAL,"
                + COLUMN_DISCOUNT + " REAL,"
                + COLUMN_RATING + " REAL,"
                + COLUMN_STOCK + " INTEGER,"
                + COLUMN_BRAND + " TEXT,"
                + COLUMN_THUMBNAIL + " TEXT" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        // Create favorites table
        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + COLUMN_FAV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_EMAIL + " TEXT,"
                + COLUMN_FAV_PRODUCT_ID + " INTEGER,"
                + COLUMN_DATE_ADDED + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_FAV_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + ")" + ")";
        db.execSQL(CREATE_FAVORITES_TABLE);

        // Create orders table
        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + "("
                + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ORDER_USER_EMAIL + " TEXT,"
                + COLUMN_ORDER_PRODUCT_ID + " INTEGER,"
                + COLUMN_QUANTITY + " INTEGER,"
                + COLUMN_DELIVERY_METHOD + " TEXT,"
                + COLUMN_ORDER_DATE + " TEXT,"
                + COLUMN_STATUS + " TEXT,"
                + COLUMN_TOTAL_PRICE + " REAL,"
                + "FOREIGN KEY(" + COLUMN_ORDER_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + ")" + ")";
        db.execSQL(CREATE_ORDERS_TABLE);

    }


    // Statistics methods for admin dashboard
    public int getTotalUsersCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_USERS;
        Cursor cursor = db.rawQuery(query, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }
    public boolean addAdmin(String email, String firstName, String lastName, String password,
                            String gender, String city, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_PASSWORD, encryptPassword(password));
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_CITY, city);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_USER_ROLE, "admin"); // This makes them admin

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public int getTotalProductsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_PRODUCTS;
        Cursor cursor = db.rawQuery(query, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    public int getTotalOrdersCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_ORDERS;
        Cursor cursor = db.rawQuery(query, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    public int getPendingOrdersCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TABLE_ORDERS + " WHERE " + COLUMN_STATUS + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{"Pending"});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    // Admin authentication method

    public boolean isUserAdmin(String email, String password) {
        // First check hardcoded admin (keep your existing logic)
        String adminEmail = "admin@admin.com";
        String adminPassword = encryptPassword("Admin123!");
        if (email.equals(adminEmail) && encryptPassword(password).equals(adminPassword)) {
            return true;
        }

        // Then check database for admin role
        if (!checkUser(email, password)) {
            return false; // Invalid credentials
        }

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ROLE};
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        boolean isAdmin = false;
        if (cursor.moveToFirst()) {
            String role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE));
            isAdmin = "admin".equals(role);
        }

        cursor.close();
        db.close();
        return isAdmin;
    }
    // Customer management methods
    public List<UserProfile> getAllCustomers() {
        List<UserProfile> customers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {COLUMN_EMAIL, COLUMN_FIRST_NAME, COLUMN_LAST_NAME,
                COLUMN_PHONE, COLUMN_GENDER, COLUMN_CITY};

        Cursor cursor = db.query(TABLE_USERS, columns, null, null, null, null,
                COLUMN_FIRST_NAME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                UserProfile customer = new UserProfile();
                customer.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
                customer.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)));
                customer.setLastName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)));
                customer.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
                customer.setGender(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER)));
                customer.setCity(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITY)));
                customers.add(customer);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return customers;
    }

    public boolean deleteCustomer(String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();

            // Delete user's orders first (foreign key constraint)
            db.delete(TABLE_ORDERS, COLUMN_ORDER_USER_EMAIL + " = ?", new String[]{email});

            // Delete user's favorites
            db.delete(TABLE_FAVORITES, COLUMN_USER_EMAIL + " = ?", new String[]{email});

            // Delete user
            int result = db.delete(TABLE_USERS, COLUMN_EMAIL + " = ?", new String[]{email});

            db.setTransactionSuccessful();
            return result > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Product management methods
    public boolean deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();

            // Delete related orders first
            db.delete(TABLE_ORDERS, COLUMN_ORDER_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});

            // Delete related favorites
            db.delete(TABLE_FAVORITES, COLUMN_FAV_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});

            // Delete product
            int result = db.delete(TABLE_PRODUCTS, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});

            db.setTransactionSuccessful();
            return result > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        } else if (oldVersion < 3) {
            // Add profile_image column to existing users table
            try {
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_PROFILE_IMAGE + " TEXT");
            } catch (Exception e) {
                // Column might already exist, ignore
            }
        } else if (oldVersion < 4) {
            // Add user_role column to existing users table
            try {
                db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_USER_ROLE + " TEXT DEFAULT 'user'");
            } catch (Exception e) {
                // Column might already exist, ignore
            }
        }
    }


    // User methods
    public boolean addUser(String email, String firstName, String lastName, String password,
                           String gender, String city, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_PASSWORD, encryptPassword(password));
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_CITY, city);
        values.put(COLUMN_PHONE, phone);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_EMAIL + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, encryptPassword(password)};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }

    // Product methods
    public boolean addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PRODUCT_ID, product.getId());
        values.put(COLUMN_TITLE, product.getTitle());
        values.put(COLUMN_DESCRIPTION, product.getDescription());
        values.put(COLUMN_CATEGORY, product.getCategory());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_DISCOUNT, product.getDiscountPercentage());
        values.put(COLUMN_RATING, product.getRating());
        values.put(COLUMN_STOCK, product.getStock());
        values.put(COLUMN_BRAND, product.getBrand());
        values.put(COLUMN_THUMBNAIL, product.getThumbnail());

        long result = db.replace(TABLE_PRODUCTS, null, values);
        db.close();
        return result != -1;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PRODUCTS, null, null, null, null, null, COLUMN_TITLE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
                product.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                product.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                product.setDiscountPercentage(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISCOUNT)));
                product.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_RATING)));
                product.setStock(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STOCK)));
                product.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND)));
                product.setThumbnail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_THUMBNAIL)));
                products.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return products;
    }
    public void updateProductCategories() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // MEAT CATEGORY
            ContentValues meatValues = new ContentValues();
            meatValues.put(COLUMN_CATEGORY, "Meat");
            db.update(TABLE_PRODUCTS, meatValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Beef%"});
            db.update(TABLE_PRODUCTS, meatValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Steak%"});
            db.update(TABLE_PRODUCTS, meatValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Chicken%"});
            db.update(TABLE_PRODUCTS, meatValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Fish%"});

            // FRUITS CATEGORY
            ContentValues fruitValues = new ContentValues();
            fruitValues.put(COLUMN_CATEGORY, "Fruits");
            db.update(TABLE_PRODUCTS, fruitValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Apple%"});
            db.update(TABLE_PRODUCTS, fruitValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Banana%"});
            db.update(TABLE_PRODUCTS, fruitValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Orange%"});
            db.update(TABLE_PRODUCTS, fruitValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Kiwi%"});
            db.update(TABLE_PRODUCTS, fruitValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Lemon%"});
            db.update(TABLE_PRODUCTS, fruitValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Strawberry%"});
            db.update(TABLE_PRODUCTS, fruitValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Mulberry%"});
            db.update(TABLE_PRODUCTS, fruitValues, COLUMN_TITLE + " = ?", new String[]{"panana"}); // Fix typo

            // VEGETABLES CATEGORY
            ContentValues vegValues = new ContentValues();
            vegValues.put(COLUMN_CATEGORY, "Vegetables");
            db.update(TABLE_PRODUCTS, vegValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Cucumber%"});
            db.update(TABLE_PRODUCTS, vegValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Bell Pepper%"});
            db.update(TABLE_PRODUCTS, vegValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Green Pepper%"});
            db.update(TABLE_PRODUCTS, vegValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Chili%"});
            db.update(TABLE_PRODUCTS, vegValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Potato%"});
            db.update(TABLE_PRODUCTS, vegValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Onion%"});
            db.update(TABLE_PRODUCTS, vegValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Carrot%"});
            db.update(TABLE_PRODUCTS, vegValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Broccoli%"});
            db.update(TABLE_PRODUCTS, vegValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Spinach%"});

            // DAIRY CATEGORY
            ContentValues dairyValues = new ContentValues();
            dairyValues.put(COLUMN_CATEGORY, "Dairy");
            db.update(TABLE_PRODUCTS, dairyValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Milk%"});
            db.update(TABLE_PRODUCTS, dairyValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Egg%"});
            db.update(TABLE_PRODUCTS, dairyValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Cheese%"});
            db.update(TABLE_PRODUCTS, dairyValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Yogurt%"});
            db.update(TABLE_PRODUCTS, dairyValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Butter%"});

            // BEVERAGES CATEGORY
            ContentValues beverageValues = new ContentValues();
            beverageValues.put(COLUMN_CATEGORY, "Beverages");
            db.update(TABLE_PRODUCTS, beverageValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Juice%"});
            db.update(TABLE_PRODUCTS, beverageValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Water%"});
            db.update(TABLE_PRODUCTS, beverageValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Coffee%"});
            db.update(TABLE_PRODUCTS, beverageValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Nescafe%"});
            db.update(TABLE_PRODUCTS, beverageValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Soda%"});

            // FROZEN CATEGORY
            ContentValues frozenValues = new ContentValues();
            frozenValues.put(COLUMN_CATEGORY, "Frozen");
            db.update(TABLE_PRODUCTS, frozenValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Ice Cream%"});
            db.update(TABLE_PRODUCTS, frozenValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Frozen%"});

            // CANNED/PACKAGED CATEGORY
            ContentValues cannedValues = new ContentValues();
            cannedValues.put(COLUMN_CATEGORY, "Canned");
            db.update(TABLE_PRODUCTS, cannedValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Rice%"});
            db.update(TABLE_PRODUCTS, cannedValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Honey%"});
            db.update(TABLE_PRODUCTS, cannedValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Soup%"});
            db.update(TABLE_PRODUCTS, cannedValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Beans%"});
            db.update(TABLE_PRODUCTS, cannedValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Sauce%"});

            // HEALTH & WELLNESS CATEGORY (for supplements)
            ContentValues healthValues = new ContentValues();
            healthValues.put(COLUMN_CATEGORY, "Health");
            db.update(TABLE_PRODUCTS, healthValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Protein%"});
            db.update(TABLE_PRODUCTS, healthValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Whey%"});
            db.update(TABLE_PRODUCTS, healthValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Supplement%"});

            // HOUSEHOLD ITEMS CATEGORY
            ContentValues householdValues = new ContentValues();
            householdValues.put(COLUMN_CATEGORY, "Household");
            db.update(TABLE_PRODUCTS, householdValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Tissue%"});
            db.update(TABLE_PRODUCTS, householdValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Paper%"});
            db.update(TABLE_PRODUCTS, householdValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Cleaning%"});

            // PET FOOD CATEGORY
            ContentValues petValues = new ContentValues();
            petValues.put(COLUMN_CATEGORY, "Pet Food");
            db.update(TABLE_PRODUCTS, petValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Cat Food%"});
            db.update(TABLE_PRODUCTS, petValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Dog Food%"});
            db.update(TABLE_PRODUCTS, petValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Pet%"});
            db.update(TABLE_PRODUCTS, petValues, COLUMN_TITLE + " LIKE ?", new String[]{"%Whiskas%"});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
    public List<Product> searchProducts(String query) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_TITLE + " LIKE ? OR " + COLUMN_CATEGORY + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%", "%" + query + "%"};

        Cursor cursor = db.query(TABLE_PRODUCTS, null, selection, selectionArgs, null, null, COLUMN_TITLE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
                product.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                product.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                product.setDiscountPercentage(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISCOUNT)));
                product.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_RATING)));
                product.setStock(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STOCK)));
                product.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND)));
                product.setThumbnail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_THUMBNAIL)));
                products.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return products;
    }

    public List<Product> filterProducts(String category, double minPrice, double maxPrice) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_PRICE + " >= ? AND " + COLUMN_PRICE + " <= ?";
        String[] selectionArgs;

        if (category != null && !category.equals("All Categories")) {
            selection += " AND " + COLUMN_CATEGORY + " = ?";
            selectionArgs = new String[]{String.valueOf(minPrice), String.valueOf(maxPrice), category};
        } else {
            selectionArgs = new String[]{String.valueOf(minPrice), String.valueOf(maxPrice)};
        }

        Cursor cursor = db.query(TABLE_PRODUCTS, null, selection, selectionArgs, null, null, COLUMN_TITLE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
                product.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                product.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                product.setDiscountPercentage(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISCOUNT)));
                product.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_RATING)));
                product.setStock(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STOCK)));
                product.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND)));
                product.setThumbnail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_THUMBNAIL)));
                products.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return products;
    }
    public List<Product> getProductsByCategory(String category) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_CATEGORY + " = ?";
        String[] selectionArgs = {category};

        Cursor cursor = db.query(TABLE_PRODUCTS, null, selection, selectionArgs, null, null, COLUMN_TITLE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
                product.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                product.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                product.setDiscountPercentage(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISCOUNT)));
                product.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_RATING)));
                product.setStock(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STOCK)));
                product.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND)));
                product.setThumbnail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_THUMBNAIL)));
                products.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return products;
    }

    // Favorites methods
    public boolean addToFavorites(String userEmail, int productId) {
        if (isFavorite(userEmail, productId)) {
            return false; // Already in favorites
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_EMAIL, userEmail);
        values.put(COLUMN_FAV_PRODUCT_ID, productId);
        values.put(COLUMN_DATE_ADDED, System.currentTimeMillis());

        long result = db.insert(TABLE_FAVORITES, null, values);
        db.close();
        return result != -1;
    }

    public boolean removeFromFavorites(String userEmail, int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ? AND " + COLUMN_FAV_PRODUCT_ID + " = ?";
        String[] selectionArgs = {userEmail, String.valueOf(productId)};

        int result = db.delete(TABLE_FAVORITES, selection, selectionArgs);
        db.close();
        return result > 0;
    }

    public boolean isFavorite(String userEmail, int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ? AND " + COLUMN_FAV_PRODUCT_ID + " = ?";
        String[] selectionArgs = {userEmail, String.valueOf(productId)};

        Cursor cursor = db.query(TABLE_FAVORITES, null, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }

    public List<Product> getUserFavorites(String userEmail) {
        List<Product> favorites = new ArrayList<>();
        if (userEmail == null || userEmail.isEmpty()) {
            return favorites;
        }

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();

            String query = "SELECT p." + COLUMN_PRODUCT_ID + ", p." + COLUMN_TITLE +
                    ", p." + COLUMN_DESCRIPTION + ", p." + COLUMN_CATEGORY +
                    ", p." + COLUMN_PRICE + ", p." + COLUMN_DISCOUNT +
                    ", p." + COLUMN_RATING + ", p." + COLUMN_STOCK +
                    ", p." + COLUMN_BRAND + ", p." + COLUMN_THUMBNAIL +
                    ", f." + COLUMN_DATE_ADDED +
                    " FROM " + TABLE_FAVORITES + " f" +
                    " INNER JOIN " + TABLE_PRODUCTS + " p ON f." + COLUMN_FAV_PRODUCT_ID +
                    " = p." + COLUMN_PRODUCT_ID +
                    " WHERE f." + COLUMN_USER_EMAIL + " = ?" +
                    " ORDER BY f." + COLUMN_DATE_ADDED + " DESC";

            cursor = db.rawQuery(query, new String[]{userEmail});

            if (cursor.moveToFirst()) {
                do {
                    Product product = new Product();
                    product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
                    product.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                    product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                    product.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                    product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                    product.setDiscountPercentage(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISCOUNT)));
                    product.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_RATING)));
                    product.setStock(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STOCK)));
                    product.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND)));

                    String thumbnail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_THUMBNAIL));
                    product.setThumbnail(thumbnail != null ? thumbnail : "");

                    favorites.add(product);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return favorites;
    }

    // Orders methods
    public boolean addOrder(String userEmail, int productId, int quantity, String deliveryMethod, double totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ORDER_USER_EMAIL, userEmail);
        values.put(COLUMN_ORDER_PRODUCT_ID, productId);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_DELIVERY_METHOD, deliveryMethod);
        values.put(COLUMN_ORDER_DATE, System.currentTimeMillis());
        values.put(COLUMN_STATUS, "Pending");
        values.put(COLUMN_TOTAL_PRICE, totalPrice);

        long result = db.insert(TABLE_ORDERS, null, values);
        db.close();
        return result != -1;
    }

    public List<Order> getUserOrders(String userEmail) {
        List<Order> orders = new ArrayList<>();
        if (userEmail == null || userEmail.isEmpty()) {
            return orders;
        }

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();

            String query = "SELECT o." + COLUMN_ORDER_ID + ", o." + COLUMN_ORDER_USER_EMAIL +
                    ", o." + COLUMN_ORDER_PRODUCT_ID + ", COALESCE(p." + COLUMN_TITLE + ", 'Unknown Product') as product_title" +
                    ", o." + COLUMN_QUANTITY + ", o." + COLUMN_DELIVERY_METHOD +
                    ", o." + COLUMN_ORDER_DATE + ", o." + COLUMN_STATUS +
                    ", o." + COLUMN_TOTAL_PRICE + ", p." + COLUMN_THUMBNAIL +
                    " FROM " + TABLE_ORDERS + " o" +
                    " LEFT JOIN " + TABLE_PRODUCTS + " p ON o." + COLUMN_ORDER_PRODUCT_ID +
                    " = p." + COLUMN_PRODUCT_ID +
                    " WHERE o." + COLUMN_ORDER_USER_EMAIL + " = ?" +
                    " ORDER BY o." + COLUMN_ORDER_DATE + " DESC";

            cursor = db.rawQuery(query, new String[]{userEmail});

            if (cursor.moveToFirst()) {
                do {
                    Order order = new Order();
                    order.setOrderId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID)));
                    order.setUserEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_USER_EMAIL)));
                    order.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PRODUCT_ID)));
                    order.setProductName(cursor.getString(cursor.getColumnIndexOrThrow("product_title")));
                    order.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)));
                    order.setDeliveryMethod(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DELIVERY_METHOD)));
                    order.setOrderDate(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE)));
                    order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                    order.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_PRICE)));

                    String thumbnail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_THUMBNAIL));
                    order.setProductThumbnail(thumbnail != null ? thumbnail : "");

                    orders.add(order);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return orders;
    }
    public boolean updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, product.getTitle());
        values.put(COLUMN_DESCRIPTION, product.getDescription());
        values.put(COLUMN_CATEGORY, product.getCategory());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_DISCOUNT, product.getDiscountPercentage());
        values.put(COLUMN_RATING, product.getRating());
        values.put(COLUMN_STOCK, product.getStock());
        values.put(COLUMN_BRAND, product.getBrand());
        values.put(COLUMN_THUMBNAIL, product.getThumbnail());

        String selection = COLUMN_PRODUCT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(product.getId())};

        int result = db.update(TABLE_PRODUCTS, values, selection, selectionArgs);
        db.close();
        return result > 0;
    }


    public Product getProductById(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Product product = null;

        String selection = COLUMN_PRODUCT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(productId)};

        Cursor cursor = db.query(TABLE_PRODUCTS, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            product = new Product();
            product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
            product.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
            product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
            product.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
            product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
            product.setDiscountPercentage(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISCOUNT)));
            product.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_RATING)));
            product.setStock(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STOCK)));
            product.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND)));
            product.setThumbnail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_THUMBNAIL)));
        }

        cursor.close();
        db.close();
        return product;
    }
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT o." + COLUMN_ORDER_ID + ", o." + COLUMN_ORDER_USER_EMAIL +
                ", o." + COLUMN_ORDER_PRODUCT_ID + ", p." + COLUMN_TITLE +
                ", o." + COLUMN_QUANTITY + ", o." + COLUMN_DELIVERY_METHOD +
                ", o." + COLUMN_ORDER_DATE + ", o." + COLUMN_STATUS +
                ", o." + COLUMN_TOTAL_PRICE + ", p." + COLUMN_THUMBNAIL +
                " FROM " + TABLE_ORDERS + " o" +
                " LEFT JOIN " + TABLE_PRODUCTS + " p ON o." + COLUMN_ORDER_PRODUCT_ID +
                " = p." + COLUMN_PRODUCT_ID +
                " ORDER BY o." + COLUMN_ORDER_DATE + " DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setOrderId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID)));
                order.setUserEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_USER_EMAIL)));
                order.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PRODUCT_ID)));
                order.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                order.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)));
                order.setDeliveryMethod(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DELIVERY_METHOD)));
                order.setOrderDate(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE)));
                order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)));
                order.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_PRICE)));
                order.setProductThumbnail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_THUMBNAIL)));
                orders.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orders;
    }

    public boolean updateOrderStatus(int orderId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, newStatus);

        String selection = COLUMN_ORDER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(orderId)};

        int result = db.update(TABLE_ORDERS, values, selection, selectionArgs);
        db.close();
        return result > 0;
    }

    // Method to add sample orders for testing
    public void addSampleOrders(String userEmail) {
        long currentTime = System.currentTimeMillis();

        // Order 1 - Pending
        addTestOrder(userEmail, 1, 2, "Home Delivery", currentTime - (1000 * 60 * 60 * 2), "Pending", 25.99);

        // Order 2 - Approved
        addTestOrder(userEmail, 2, 1, "Pickup", currentTime - (1000 * 60 * 60 * 24), "Approved", 15.50);

        // Order 3 - Delivered
        addTestOrder(userEmail, 3, 3, "Home Delivery", currentTime - (1000 * 60 * 60 * 24 * 3), "Delivered", 42.75);
    }

    private void addTestOrder(String userEmail, int productId, int quantity, String deliveryMethod,
                              long orderDate, String status, double totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ORDER_USER_EMAIL, userEmail);
        values.put(COLUMN_ORDER_PRODUCT_ID, productId);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_DELIVERY_METHOD, deliveryMethod);
        values.put(COLUMN_ORDER_DATE, orderDate);
        values.put(COLUMN_STATUS, status);
        values.put(COLUMN_TOTAL_PRICE, totalPrice);

        db.insert(TABLE_ORDERS, null, values);
        db.close();
    }

    // Method to add sample favorites for testing
    public void addSampleFavorites(String userEmail) {
        addToFavorites(userEmail, 1);
        addToFavorites(userEmail, 2);
        addToFavorites(userEmail, 3);
    }

    public List<Product> getDiscountedProducts() {
        List<Product> discountedProducts = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();

            String selection = COLUMN_DISCOUNT + " > ?";
            String[] selectionArgs = {"0"};

            cursor = db.query(TABLE_PRODUCTS, null, selection, selectionArgs, null, null, COLUMN_DISCOUNT + " DESC");

            if (cursor.moveToFirst()) {
                do {
                    Product product = new Product();
                    product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_ID)));
                    product.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                    product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                    product.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                    product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)));
                    product.setDiscountPercentage(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DISCOUNT)));
                    product.setRating(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_RATING)));
                    product.setStock(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STOCK)));
                    product.setBrand(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND)));

                    String thumbnail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_THUMBNAIL));
                    product.setThumbnail(thumbnail != null ? thumbnail : "");

                    discountedProducts.add(product);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return discountedProducts;
    }

    // Method to add sample discounts for testing
    public void addSampleDiscounts() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            String updateQuery1 = "UPDATE " + TABLE_PRODUCTS + " SET " + COLUMN_DISCOUNT + " = ? WHERE " + COLUMN_PRODUCT_ID + " = ?";
            db.execSQL(updateQuery1, new Object[]{15.0, 1});
            db.execSQL(updateQuery1, new Object[]{25.0, 2});
            db.execSQL(updateQuery1, new Object[]{10.0, 3});
            db.execSQL(updateQuery1, new Object[]{30.0, 4});
            db.execSQL(updateQuery1, new Object[]{20.0, 5});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    // Profile management methods
    public UserProfile getUserProfile(String email) {
        UserProfile userProfile = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            String[] columns = {COLUMN_EMAIL, COLUMN_FIRST_NAME, COLUMN_LAST_NAME,
                    COLUMN_PHONE, COLUMN_GENDER, COLUMN_CITY, COLUMN_PROFILE_IMAGE};
            String selection = COLUMN_EMAIL + " = ?";
            String[] selectionArgs = {email};

            cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                userProfile = new UserProfile();
                userProfile.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)));
                userProfile.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)));
                userProfile.setLastName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)));
                userProfile.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
                userProfile.setGender(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER)));
                userProfile.setCity(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITY)));

                String profileImage = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROFILE_IMAGE));
                userProfile.setProfileImagePath(profileImage != null ? profileImage : "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return userProfile;
    }

    public boolean updateUserProfile(String email, String firstName, String lastName,
                                     String phone, String newPassword, String profileImagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_PASSWORD, encryptPassword(newPassword));
        if (profileImagePath != null) {
            values.put(COLUMN_PROFILE_IMAGE, profileImagePath);
        }

        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};

        int result = db.update(TABLE_USERS, values, selection, selectionArgs);
        db.close();
        return result > 0;
    }

    public boolean updateUserProfileWithoutPassword(String email, String firstName, String lastName,
                                                    String phone, String profileImagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_PHONE, phone);
        if (profileImagePath != null) {
            values.put(COLUMN_PROFILE_IMAGE, profileImagePath);
        }

        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};

        int result = db.update(TABLE_USERS, values, selection, selectionArgs);
        db.close();
        return result > 0;
    }

    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT DISTINCT " + COLUMN_CATEGORY + " FROM " + TABLE_PRODUCTS;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return categories;
    }

    private String encryptPassword(String password) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : password.toCharArray()) {
            encrypted.append((char) (c + 3));
        }
        return encrypted.toString();
    }
}