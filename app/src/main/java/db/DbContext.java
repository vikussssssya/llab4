package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DbContext {
    private Context context;
    private MyDbHelper myDbHelper;
    private SQLiteDatabase db;

    public DbContext(Context context){
        this.context = context;
        myDbHelper = new MyDbHelper(context);
    }

    // Открытие базы данных
    public void OpenDb(){
        db = myDbHelper.getWritableDatabase();
    }

    // Закрытие базы данных
    public void CloseDb(){
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    // Добавление трека в базу данных
    public void Add(Trak trak){
        ContentValues cv = new ContentValues();
        cv.put(MyConst.EXECUTOR, trak.getExecutor());
        cv.put(MyConst.TITLE, trak.getTitle());
        cv.put(MyConst.DATE, trak.getDate());
        db.insert(MyConst.TABLE_NAME, null, cv);
    }

    // Получение всех треков
    public List<Trak> GetTraks(){
        List<Trak> traks = new ArrayList<>();
        Cursor cursor = db.query(MyConst.TABLE_NAME, null, null, null, null, null, null);

        while(cursor.moveToNext()){
            Trak trak = new Trak();
            trak.setId(cursor.getString(0));
            trak.setExecutor(cursor.getString(1));
            trak.setTitle(cursor.getString(2));
            trak.setDate(cursor.getString(3));
            traks.add(trak);
        }
        cursor.close();
        return traks;
    }

    public Trak getLastTrack() {
        Trak lastTrack = null;
        Cursor cursor = null;

        try {
            // Выполняем SQL-запрос для получения последнего трека
            String query = "SELECT * FROM " + MyConst.TABLE_NAME + " ORDER BY " + MyConst.ID + " DESC LIMIT 1";
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Если курсор не пустой, извлекаем данные последнего трека
                lastTrack = new Trak();
                lastTrack.setId(cursor.getString(cursor.getColumnIndex(MyConst.ID)));
                lastTrack.setExecutor(cursor.getString(cursor.getColumnIndex(MyConst.EXECUTOR)));
                lastTrack.setTitle(cursor.getString(cursor.getColumnIndex(MyConst.TITLE)));
                lastTrack.setDate(cursor.getString(cursor.getColumnIndex(MyConst.DATE)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close(); // Закрытие курсора
            }
        }

        return lastTrack; // Возвращаем последний трек
    }

}
