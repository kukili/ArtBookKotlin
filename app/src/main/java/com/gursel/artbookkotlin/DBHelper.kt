package com.gursel.artbookkotlin

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory

class DBHelper(context: Context) {
    private val database: SQLiteDatabase = context.openOrCreateDatabase("Arts", Context.MODE_PRIVATE, null).apply {
        execSQL("CREATE TABLE IF NOT EXISTS arts (id INTEGER PRIMARY KEY, artname VARCHAR, paintername VARCHAR, year VARCHAR, image BLOB)")
    }

    fun getAllArts(): List<Art> {
        val list = mutableListOf<Art>()
        val cursor = database.rawQuery("SELECT * FROM arts", null)
        val nameIx = cursor.getColumnIndex("artname")
        val idIx = cursor.getColumnIndex("id")
        val painterIx = cursor.getColumnIndex("paintername")
        val yearIx = cursor.getColumnIndex("year")
        val imageIx = cursor.getColumnIndex("image")
        while (cursor.moveToNext()) {
            val id = cursor.getInt(idIx)
            val name = cursor.getString(nameIx)
            val painter = if (painterIx >= 0) cursor.getString(painterIx) else null
            val year = if (yearIx >= 0) cursor.getString(yearIx) else null
            val image = if (imageIx >= 0) cursor.getBlob(imageIx) else null
            list.add(Art(id, name ?: "", painter, year, image))
        }
        cursor.close()
        return list
    }

    fun getArtById(artId: Int): Art? {
        val cursor = database.rawQuery("SELECT * FROM arts WHERE id = ?", arrayOf(artId.toString()))
        val art = if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("artname"))
            val painter = cursor.getString(cursor.getColumnIndex("paintername"))
            val year = cursor.getString(cursor.getColumnIndex("year"))
            val image = cursor.getBlob(cursor.getColumnIndex("image"))
            Art(id, name, painter, year, image)
        } else null
        cursor.close()
        return art
    }

    fun insertArt(artName: String, painterName: String, year: String, imageBytes: ByteArray) {
        val sqlString = "INSERT INTO arts (artname, paintername, year, image) VALUES (?, ?, ?, ?)"
        val statement = database.compileStatement(sqlString)
        statement.bindString(1, artName)
        statement.bindString(2, painterName)
        statement.bindString(3, year)
        statement.bindBlob(4, imageBytes)
        statement.execute()
    }
}

