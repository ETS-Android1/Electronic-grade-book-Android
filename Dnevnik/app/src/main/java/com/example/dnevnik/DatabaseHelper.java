package com.example.dnevnik;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "baza";
    // tabela nastavnika
    private final static String TABLE_NAME = "table_nastavnik";
    private final static String COL1 = "ID_nastavnika";
    private final static String COL2 = "Ime";
    private final static String COL3 = "Prezime";
    private final static String COL4 = "Password";
    // tabela ucenika
    private final static String TABLE_NAME2 = "table_ucenik";
    private final static String COL1_2 = "ID_ucenika";
    private final static String COL2_2 = "Ime";
    private final static String COL3_2 = "Prezime";
    private final static String COL4_2 = "Password";
    // tabela premeta
    private final static String TABLE_NAME3 = "table_predmet";
    private final static String COL1_3 = "ID_predmeta";
    private final static String COL2_3 = "ID_nastavnika";
    private final static String COL3_3 = "Naziv_predmeta";
    // tabela ocena
    private final static String TABLE_NAME4 = "table_ocene";
    private final static String COL1_4 = "Datum";
    private final static String COL2_4 = "ID_ucenika";
    private final static String COL3_4 = "ID_predmeta";
    private final static String COL4_4 = "Ocena";

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    private Context context = null;

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + " ( "
                + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL2 + " TEXT not null, "
                + COL3 + " TEXT not null, "
                + COL4 + " TEXT not null)";
        String sql2 = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME2 + " ( "
                + COL1_2 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL2_2 + " TEXT not null, "
                + COL3_2 + " TEXT not null, "
                + COL4_2 + " TEXT not null)";
        String sql3 = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME3 + " ( "
                + COL1_3 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL2_3 + " INTEGER not null,"
                + COL3_3 + " TEXT not null, "
                + " FOREIGN KEY ("+COL2_3+") REFERENCES "+TABLE_NAME+"("+COL2_3+"),"
                + " UNIQUE("+COL3_3+"));";
        String sql4 = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME4 + " ( "
                + COL1_4 + " TEXT , "
                + COL2_4 + " INTEGER not null, "
                + COL3_4 + " INTEGER not null, "
                + COL4_4 + " INTEGER ,"
                + " FOREIGN KEY ("+COL2_4+") REFERENCES "+TABLE_NAME2+"("+COL1_2+"), FOREIGN KEY ("+COL3_4+") REFERENCES "+TABLE_NAME3+"("+COL1_3+"),"
                + " UNIQUE("+COL2_4+","+COL3_4+"));";
        db.execSQL(sql);
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME4);
        onCreate(db);
    }

    public Cursor getNastavnikData() throws SQLException
    {
        db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(sql, null);
    }

    public Cursor getUcenikData() throws SQLException
    {
        db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME2;
        return db.rawQuery(sql, null);
    }

    public Cursor getPredmetiData() throws SQLException
    {
        db = this.getWritableDatabase();
        String sql = "SELECT * FROM "+ TABLE_NAME3 +" a INNER JOIN "+TABLE_NAME+" b ON a."+ COL2_3 +" = b."+ COL1 +" ORDER BY "+ COL2_3;
        return db.rawQuery(sql, null);
    }

    public Cursor getMojiPredmetiData(String id) throws SQLException
    {
        db = this.getWritableDatabase();
        String sql = "SELECT * FROM "+ TABLE_NAME3 +" a INNER JOIN "+TABLE_NAME+" b ON a."+ COL2_3 +" = b."+ COL1 +" WHERE a."+ COL2_3 +" =?";
        return db.rawQuery(sql, new String[] {id});
    }

    public Cursor getOceneData(String id) throws SQLException
    {
        db = this.getWritableDatabase();
        String sql = "SELECT * FROM "+ TABLE_NAME4 +" a INNER JOIN "+TABLE_NAME3+" b ON a."+ COL3_4 +" = b."+ COL1_3 +" INNER JOIN "+TABLE_NAME2+" c ON a."+ COL2_4 +" = c."+ COL1_2 +" WHERE b."+ COL2_3 +"=? ORDER BY "+ COL2_4;
        return db.rawQuery(sql, new String[] {id});
    }

    public Cursor ispisiOcene(String ime, String prezime, String pass) throws SQLException
    {
        db = this.getWritableDatabase();
        String sql = "SELECT * FROM "+ TABLE_NAME4 +" a INNER JOIN "+ TABLE_NAME3 +" b ON a."+ COL3_4 +" = b."+ COL1_3
                +" INNER JOIN "+ TABLE_NAME2 +" c ON a."+ COL2_4 +" = c."+ COL1_2
                +" INNER JOIN "+ TABLE_NAME +" d ON b."+ COL2_3 +" = d."+ COL1
                +" WHERE c."+ COL2_2 +" =? AND c."+ COL3_2 +" =? AND c."+ COL4_2 + " =? ORDER BY d."+ COL1;
        return db.rawQuery(sql, new String[]{ime, prezime, pass});
    }

    public boolean LoginNastavnik(String username, String password) throws SQLException
    {
        Cursor mCursor = db.rawQuery("SELECT (Ime || ' ' || Prezime) AS username, password FROM " + TABLE_NAME + " WHERE username=? AND password=?", new String[]{username,password});
        if (mCursor != null) {
            if(mCursor.getCount() > 0)
            {
                return true;
            }
        }
        return false;
    }

    public boolean LoginUcenik(String username, String password) throws SQLException
    {
        Cursor mCursor = db.rawQuery("SELECT (Ime || ' ' || Prezime) AS username, password FROM " + TABLE_NAME2 + " WHERE username=? AND password=?", new String[]{username,password});
        if (mCursor != null) {
            if(mCursor.getCount() > 0)
            {
                return true;
            }
        }
        return false;
    }

    public boolean noviUcenik(String ime, String prezime, String sifra) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL2_2, ime);
        values.put(COL3_2, prezime);
        values.put(COL4_2, sifra);

        long result = db.insert(TABLE_NAME2, null, values);
        return result != -1;
    }

    public boolean noviNastavnik(String ime, String prezime, String sifra) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL2, ime);
        values.put(COL3, prezime);
        values.put(COL4, sifra);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public boolean noviPredmet(String nastavnikID, String naziv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL2_3, nastavnikID);
        values.put(COL3_3, naziv);

        long result = db.insert(TABLE_NAME3, null, values);
        return result != -1;
    }

    public boolean novaOcena(String predmetID, String ucenikID, String datum, String ocena) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL1_4, datum);
        values.put(COL2_4, ucenikID);
        values.put(COL3_4, predmetID);
        values.put(COL4_4, ocena);

        long result = db.insert(TABLE_NAME4, null, values);
        return result != -1;
    }

    public void updateOcena(int predmetID, int ucenikID, String datum, String ocena) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME4 + " SET "  + COL1_4 + "='" + datum + "', "  + COL2_4 + "='" + ucenikID + "', "
                + COL3_4 + " ='" + predmetID + "', " + COL4_4 + " ='" + ocena + "'"
                + " WHERE " + COL1_3 + "='" + predmetID + "' AND " + COL1_2 + "='" + ucenikID + "'" ;
        db.execSQL(sql);
    }

    public boolean obrisiUcenika(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME2, "ID_ucenika=?", new String[]{id});
        return result != -1;
    }

    public boolean obrisiNastavnika(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "ID_nastavnika=?", new String[]{id});
        return result != -1;
    }

    public boolean obrisiPredmet(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME3, "ID_predmeta=?", new String[]{id});
        return result != -1;
    }

    public Cursor nadjiUcenika(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME2 + " WHERE " + COL1_2 + " ='" + id + "'";
        return db.rawQuery(sql, null);
    }

    public Cursor nadjiPredmet(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME3 + " WHERE " + COL1_3 + " ='" + id + "'";
        return db.rawQuery(sql, null);
    }

    public Cursor nadjiMojPredmet(int id, String nastavnikID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME3 + " WHERE " + COL1_3 + " ='" + id + "' AND " + COL2_3 + " = '" + nastavnikID + "'";
        return db.rawQuery(sql, null);
    }

    public Cursor nadjiNastavnika(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL1 + " ='" + id + "'";
        return db.rawQuery(sql, null);
    }

    public Cursor nadjiIDNastavnika(String ime, String prezime, String pass) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL2 + " =? AND " + COL3 + "= ? AND " + COL4 + "=?";
        return db.rawQuery(sql, new String[]{ime, prezime, pass});
    }

    public Cursor nadjiOcenu(int predmetID, int ucenikID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME4 + " WHERE " + COL3_4 + " ='" + predmetID + "' AND "+ COL2_4 +" ='"+ ucenikID+"'";
        return db.rawQuery(sql, null);
    }
}
