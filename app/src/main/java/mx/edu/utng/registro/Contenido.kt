package mx.edu.utng.registro

import DatabaseHandler
import DiarioModel
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Contenido : AppCompatActivity() {

    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var recyclerView: RecyclerView
    private lateinit var diarioAdapter: DiarioAdapter
    private lateinit var listaEntradas: MutableList<DiarioModel>
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contenido)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        databaseHandler = DatabaseHandler(this)
        listaEntradas = databaseHandler.viewEntradas().toMutableList()

        diarioAdapter = DiarioAdapter(listaEntradas, databaseHandler) { entrada ->
            val intent = Intent(this, Dia::class.java).apply {
                putExtra("id", entrada.id) // Pasar el ID del ítem a editar
            }
            startActivity(intent)
        }
        recyclerView.adapter = diarioAdapter

        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filtrarEntradas(newText.orEmpty())
                return true
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        btnAgregar.setOnClickListener {
            val intento = Intent(this, Dia::class.java)
            startActivity(intento)
        }

        val btnInicio = findViewById<Button>(R.id.btnInicioReg)
        btnInicio.setOnClickListener {
            val intento = Intent(this, MainActivity::class.java)
            startActivity(intento)
        }
    }

    override fun onResume() {
        super.onResume()
        actualizarLista()
    }

    private fun actualizarLista() {
        listaEntradas.clear()
        listaEntradas.addAll(databaseHandler.viewEntradas())
        diarioAdapter.notifyDataSetChanged()
    }

    private fun filtrarEntradas(texto: String) {
        val todasLasEntradas = databaseHandler.viewEntradas()
        val listaFiltrada = todasLasEntradas.filter { it.titulo.contains(texto, ignoreCase = true) }
        diarioAdapter.actualizarLista(listaFiltrada)
    }

}