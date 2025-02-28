import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "DiarioDatabase"
        private const val TABLE_ENTRADAS = "Diario"

        private const val KEY_ID = "id"
        private const val KEY_FECHA = "fecha"
        private const val KEY_TITULO = "titulo"
        private const val KEY_CONTENIDO = "contenido"
        private const val KEY_ESTADO_ANIMO = "estado_animo"
        private const val KEY_ACTIVIDAD_DIA = "actividad_dia"
        private const val KEY_REFLEXION = "reflexion"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Creación de la tabla con los campos especificados
        val CREATE_TABLE = ("CREATE TABLE $TABLE_ENTRADAS ("
                + "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$KEY_FECHA TEXT, "
                + "$KEY_TITULO TEXT, "
                + "$KEY_CONTENIDO TEXT, "
                + "$KEY_ESTADO_ANIMO TEXT, "
                + "$KEY_ACTIVIDAD_DIA TEXT, "
                + "$KEY_REFLEXION TEXT)"
                )
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ENTRADAS")
        onCreate(db)
    }

    // Método para insertar datos
    fun addEntrada(entrada: DiarioModel): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(KEY_FECHA, entrada.fecha)
            put(KEY_TITULO, entrada.titulo)
            put(KEY_CONTENIDO, entrada.contenido)
            put(KEY_ESTADO_ANIMO, entrada.estadoAnimo)
            put(KEY_ACTIVIDAD_DIA, entrada.actividadDia)
            put(KEY_REFLEXION, entrada.reflexion)
        }
        val success = db.insert(TABLE_ENTRADAS, null, contentValues)
        db.close()
        return success
    }

    // Método para leer datos
    fun viewEntradas(): List<DiarioModel> {
        val listaEntradas = ArrayList<DiarioModel>()
        val selectQuery = "SELECT * FROM $TABLE_ENTRADAS"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do {
                val entrada = DiarioModel(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FECHA)),
                    titulo = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITULO)),
                    contenido = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CONTENIDO)),
                    estadoAnimo = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ESTADO_ANIMO)),
                    actividadDia = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ACTIVIDAD_DIA)),
                    reflexion = cursor.getString(cursor.getColumnIndexOrThrow(KEY_REFLEXION))
                )
                listaEntradas.add(entrada)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return listaEntradas
    }

    // Método para actualizar datos
    fun updateEntrada(entrada: DiarioModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(KEY_FECHA, entrada.fecha)
            put(KEY_TITULO, entrada.titulo)
            put(KEY_CONTENIDO, entrada.contenido)
            put(KEY_ESTADO_ANIMO, entrada.estadoAnimo)
            put(KEY_ACTIVIDAD_DIA, entrada.actividadDia)
            put(KEY_REFLEXION, entrada.reflexion)
        }
        val success = db.update(TABLE_ENTRADAS, contentValues, "$KEY_ID=?", arrayOf(entrada.id.toString()))
        db.close()
        return success
    }

    // Método para eliminar datos
    fun deleteEntrada(id: Int): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_ENTRADAS, "$KEY_ID=?", arrayOf(id.toString()))
        db.close()
        return success
    }


}

// Modelo de datos
data class DiarioModel(
    val id: Int = 0,
    val fecha: String,
    val titulo: String,
    val contenido: String,
    val estadoAnimo: String,
    val actividadDia: String,
    val reflexion: String
)
