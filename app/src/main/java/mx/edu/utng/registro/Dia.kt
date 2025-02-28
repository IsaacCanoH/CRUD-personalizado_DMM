package mx.edu.utng.registro

import DatabaseHandler
import DiarioModel
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Dia : AppCompatActivity() {
    private lateinit var edFecha: EditText
    private lateinit var edTitulo: EditText
    private lateinit var edContenido: EditText
    private lateinit var spEstadoAnimo: Spinner
    private lateinit var edActividadDia: EditText
    private lateinit var edReflexion: EditText
    private lateinit var btnPublicar: Button

    private lateinit var databaseHandler: DatabaseHandler
    private var entradaId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dia)

        edFecha = findViewById(R.id.edFecha)
        edTitulo = findViewById(R.id.edTitulo)
        edContenido = findViewById(R.id.edContenido)
        spEstadoAnimo = findViewById(R.id.spEstadoAnimo)
        edActividadDia = findViewById(R.id.edActividadDia)
        edReflexion = findViewById(R.id.edReflexion)
        btnPublicar = findViewById(R.id.btnPublicar)

        databaseHandler = DatabaseHandler(this)

        val estadosAnimo = arrayOf("Feliz", "Triste", "Enojado", "Motivado", "Cansado", "Enamorado", "Estresado")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estadosAnimo)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spEstadoAnimo.adapter = adapter

        entradaId = intent.getIntExtra("id", -1)

        if (entradaId == -1) {
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
            edFecha.setText(currentDate)
        } else {
            loadEntradaData(entradaId)
        }

        btnPublicar.setOnClickListener {
            val fecha = edFecha.text.toString()
            val titulo = edTitulo.text.toString()
            val contenido = edContenido.text.toString()
            val estadoAnimo = spEstadoAnimo.selectedItem.toString()
            val actividadDia = edActividadDia.text.toString()
            val reflexion = edReflexion.text.toString()

            if (fecha.isNotEmpty() && titulo.isNotEmpty() && contenido.isNotEmpty() && estadoAnimo.isNotEmpty() &&
                actividadDia.isNotEmpty() && reflexion.isNotEmpty()) {

                val entrada = DiarioModel(
                    id = entradaId,
                    fecha = fecha,
                    titulo = titulo,
                    contenido = contenido,
                    estadoAnimo = estadoAnimo,
                    actividadDia = actividadDia,
                    reflexion = reflexion
                )

                val result: Long = if (entradaId == -1) {
                    databaseHandler.addEntrada(entrada)
                } else {
                    databaseHandler.updateEntrada(entrada).toLong()
                }

                if (result > 0) {
                    Toast.makeText(this, "Entrada publicada exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al publicar la entrada", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnRegresar = findViewById<Button>(R.id.btnReg)
        btnRegresar.setOnClickListener {
            val intento = Intent(this, Contenido::class.java)
            startActivity(intento)
        }
    }

    private fun loadEntradaData(id: Int) {
        val entrada = databaseHandler.viewEntradas().find { it.id == id }
        entrada?.let {
            edFecha.setText(it.fecha)
            edTitulo.setText(it.titulo)
            edContenido.setText(it.contenido)
            edActividadDia.setText(it.actividadDia)
            edReflexion.setText(it.reflexion)

            val estadosAnimo = arrayOf("Feliz", "Triste", "Enojado", "Motivado", "Cansado", "Enamorado", "Estresado")
            val index = estadosAnimo.indexOf(it.estadoAnimo)
            if (index >= 0) {
                spEstadoAnimo.setSelection(index)
            }
        }
    }
}
