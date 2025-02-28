package mx.edu.utng.registro

import DatabaseHandler
import DiarioModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class DiarioAdapter(
    private val listaEntradas: MutableList<DiarioModel>,
    private val databaseHandler: DatabaseHandler,
    private val onEdit: (DiarioModel) -> Unit
) : RecyclerView.Adapter<DiarioAdapter.DiarioViewHolder>() {

    class DiarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(R.id.tvTitulo)
        val tvEstadoAnimo: TextView = view.findViewById(R.id.tvEstadoAnimo)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_blog, parent, false)
        return DiarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiarioViewHolder, position: Int) {
        val entrada = listaEntradas[position]
        holder.tvTitulo.text = entrada.titulo
        holder.tvEstadoAnimo.text = entrada.estadoAnimo

        // Mostrar detalles al hacer clic en el ítem
        holder.itemView.setOnClickListener {
            val dialog = DetallesDialogFragment(entrada)
            dialog.show((holder.itemView.context as AppCompatActivity).supportFragmentManager, "DetallesDialog")
        }

        // Botón Editar
        holder.btnEditar.setOnClickListener {
            onEdit(entrada)
        }

        // Botón Eliminar
        holder.btnEliminar.setOnClickListener {
            databaseHandler.deleteEntrada(entrada.id) // Elimina de la BD
            listaEntradas.removeAt(position) // Elimina de la lista
            notifyItemRemoved(position) // Actualiza el RecyclerView
        }
    }

    override fun getItemCount(): Int {
        return listaEntradas.size
    }

    fun actualizarLista(nuevaLista: List<DiarioModel>) {
        listaEntradas.clear()
        listaEntradas.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}