package db;

public class MyConst {
    public static final String DATABASE_NAME = "treks.db";
    public static final int VERSION = 1;
    public static final String TABLE_NAME= "trek";
    public static final String ID = "id";
    public static final String EXECUTOR = "executor";
    public static final String TITLE = "title";
    public static final String DATE = "date";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY,"
            + EXECUTOR + " TEXT,"
            + TITLE+ " TEXT,"
            + DATE + " TEXT)";
    public static final String SQL_DROP_TABLE = "DROP TABLE IF NOT EXISTS " +
            TABLE_NAME;

}
