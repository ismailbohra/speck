package com.example.webview

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.speck.credential

class CredentialDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_SQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DELETE_TABLE_SQL)
        onCreate(db)
    }

    fun insertData(data: credential) {
        val values = ContentValues()
        values.put(COLUMN_NAME, data.username)
        values.put(COLUMN_AGE, data.password)
        writableDatabase.insert(TABLE_NAME, null, values)
    }
    fun updateData(data: credential) {
        val values = ContentValues()
        values.put(COLUMN_NAME, data.username)
        values.put(COLUMN_AGE, data.password)
        writableDatabase.update(TABLE_NAME, values, "_id = ?", arrayOf("1"))
    }

    fun queryData(): List<credential> {
        val dataList = mutableListOf<credential>()
        val cursor = readableDatabase.rawQuery(SELECT_ALL_SQL, null)
        val nameIndex = cursor.getColumnIndex(COLUMN_NAME)
        val ageIndex = cursor.getColumnIndex(COLUMN_AGE)
        while (cursor.moveToNext()) {
            if (nameIndex != -1 && ageIndex != -1) {
                val username = cursor.getString(nameIndex)
                val psd = cursor.getString(ageIndex)
                dataList.add(credential(username, psd))
            }
        }
        cursor.close()
        return dataList
    }

    fun clearData() {
        writableDatabase.execSQL(DELETE_TABLE_SQL)
    }

    companion object {
        const val DATABASE_NAME = "my_cred_database.db"
        const val DATABASE_VERSION = 1

        const val TABLE_NAME = "credential"
        const val COLUMN_NAME = "username"
        const val COLUMN_AGE = "password"

        const val CREATE_TABLE_SQL =
            "CREATE TABLE $TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NAME TEXT, $COLUMN_AGE TEXT)"

        const val DELETE_TABLE_SQL = "DELETE FROM $TABLE_NAME"

        const val SELECT_ALL_SQL = "SELECT * FROM $TABLE_NAME"
    }
}
