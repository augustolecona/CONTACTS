package auggie.misapps.contacts.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(
        context,
        TableName, null, 1
    ) {


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE " + TableName + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL1 + " TEXT NOT NULL," +
                    COL2 + " TEXT NOT NULL);"
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        val dropTable = "DROP TABLE IF EXISTS $TableName"
        db.execSQL(dropTable)
        onCreate(db)
    }


    fun SaveContact(contacts: models.Contacts): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL1, contacts.id)
        contentValues.put(COL2, contacts.name)
        val result = db.insert(TableName, null, contentValues)
        db.close()
        return result != -1L
    }


    fun deleteContact(contactID: String): Boolean {
        val db = this.writableDatabase
        //var result = db.execSQL("DELETE FROM $TableName") //delete all rows in a table
       var result = db.delete(TableName, "$COL1 = $contactID",null).toLong()
        db.close()
        return result != -1L
    }

    fun getContactFav(id: Int?): Cursor? {
        val db = this.writableDatabase
        val selectUsers = "SELECT * FROM $TableName WHERE $COL1 =${id} "
        return db.rawQuery(selectUsers, null)
    }

    fun existsContactsFav(id: Int?): Boolean {
        val db = this.writableDatabase
        val selectUsers = "SELECT * FROM $TableName WHERE $COL1 =${id} "
        var getContact: Cursor? = null
        try {
            getContact = db.rawQuery(selectUsers, null)

        } catch (e: SQLiteException) {

            return false
        }

        val restaurantExists = getContact.moveToFirst()
        getContact.close()
        return restaurantExists


    }

    companion object {
        const val TAG = "DatabaseHelper"
        private const val TableName = "Contaccts"
        private const val COL1 = "IDContact"
        private const val COL2 = "contactName"
    }
}
