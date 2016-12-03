/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.example.echo.bring2me.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.echo.bring2me.model.Pedido;
import com.example.echo.bring2me.model.User;
import com.example.echo.bring2me.model.Viagem;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "user";
    private static final String TABLE_VIAGEM = "trip";
    private static final String TABLE_PEDIDO = "order";
    private static final String TABLE_PAGAMENTO = "pagamento";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";

    // Trip table columns names
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_PAIS_ORIGEM = "paisAt";
    private static final String KEY_PAIS_DESTINO = "paisDest";
    private static final String KEY_MIN_TAXA = "mintax";
    private static final String KEY_MAX_VALOR = "maxval";
    private static final String KEY_RETORNO= "retorno";

    // Order table columns names
    private static final String KEY_VALOR = "valor";
    private static final String KEY_NOME_PRODUTO = "nome_produto";
    private static final String KEY_LINK = "link";
    private static final String KEY_EMAIL_USR = "email_usuario";
    private static final String KEY_ID_VIAGEM = "id_viagem";
    private static final String KEY_ID_PEDIDO = "id_pedido";
    private static final String KEY_EMPACOTADO = "empacotado";
    private static final String KEY_ADDRESS = "Adress";
    private static final String KEY_ENTREGA = "entrega";
    private static final String KEY_AVALIADO = "avaliado";
    private static final String KEY_ACEITO = "aceito";

    private static final String KEY_NOME_CARTAO = "nomeCartao";
    private static final String KEY_NUMERO_CARTAO = "numeroCartao";
    private static final String KEY_CVV = "cvv";
    private static final String KEY_MES_ANO_EXPIRA = "mesAnoExpira";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        String CREATE_TRIP_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_VIAGEM + "(" +
                KEY_USER_ID + " TEXT NOT NULL," +
                KEY_PAIS_ORIGEM + " TEXT NOT NULL," +
                KEY_PAIS_DESTINO + " TEXT NOT NULL," +
                KEY_MAX_VALOR + " FLOAT DEFAULT '500'," +
                KEY_MIN_TAXA + " FLOAT NOT NULL DEFAULT '0'," +
                KEY_RETORNO + " TEXT NOT NULL," +
                " PRIMARY KEY ("+ KEY_PAIS_DESTINO +","+ KEY_PAIS_ORIGEM +","+ KEY_USER_ID +"))";
        db.execSQL(CREATE_TRIP_TABLE);

        String CREATE_ORDER_TABLE = "CREATE TABLE IF NOT EXISTS "+ TABLE_PEDIDO +" (" +
                KEY_VALOR + " FLOAT NOT NULL," +
                KEY_NOME_PRODUTO + " TEXT NOT NULL," +
                KEY_LINK + "TEXT ," +
                KEY_EMAIL_USR + " TEXT NOT NULL," +
                KEY_ID_VIAGEM + " TEXT NOT NULL," +
                KEY_ID_PEDIDO + "INTEGER NOT NULL ," +
                KEY_EMPACOTADO + "INTEGER DEFAULT NULL," +
                KEY_ADDRESS + "TEXT NOT NULL," +
                KEY_ENTREGA + "INTEGER NOT NULL," +
                KEY_AVALIADO + "INTEGER NOT NULL DEFAULT 0," +
                KEY_ACEITO + "INTEGER NOT NULL," +
                "PRIMARY KEY ("+ KEY_ID_PEDIDO +","+ KEY_ID_VIAGEM +","+ KEY_EMAIL_USR +"))";
        db.execSQL(CREATE_TRIP_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIAGEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEDIDO);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     *
     * @param user*/
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName()); // Name
        values.put(KEY_EMAIL, user.getEmail()); // Email
        values.put(KEY_UID, user.getUid()); // Email
        values.put(KEY_CREATED_AT, user.getCreated_at()); // Created At


        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
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
            user.put("id", cursor.getString(0));
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
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

    public void addViagem(String user_id, String paisAtual, String paisDestino, String maxVal, String mintax, String retorno) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, user_id);
        values.put(KEY_PAIS_ORIGEM, paisAtual);
        values.put(KEY_PAIS_DESTINO, paisDestino);
        values.put(KEY_MAX_VALOR, maxVal);
        values.put(KEY_MIN_TAXA, mintax);
        values.put(KEY_RETORNO, retorno);

        // Inserting Row
        db.insert(TABLE_VIAGEM, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "Nova viagem inserida ");
    }

    public void addPagamento(String  id_pedido, String nomeCartao, String numeroCartao, String cvv, String mesAnoExpira) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_PEDIDO, id_pedido);
        values.put(KEY_NOME_CARTAO, nomeCartao);
        values.put(KEY_NUMERO_CARTAO, numeroCartao);
        values.put(KEY_CVV, cvv);
        values.put(KEY_MES_ANO_EXPIRA, mesAnoExpira);

        // Inserting Row
        db.insert(TABLE_PAGAMENTO, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "Novo pagamento realizado ");
    }

    //TODO
    public void deleteViagens(){}
    public ArrayList<Viagem> getTripDetails() {
        return null;
    }
    //TODO
    public void deletePedidos(){}
    public ArrayList<Pedido> getOrderDetails() {
        return null;
    }
}