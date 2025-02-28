package mx.edu.utng.registro

import DiarioModel
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class DetallesDialogFragment(private val entrada: DiarioModel) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            setStyle(STYLE_NORMAL, R.style.CustomDialog)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_detalles_dialog_fragment, container, false)

        val tvTitulo: TextView = view.findViewById(R.id.tvTitulo)
        val tvFecha: TextView = view.findViewById(R.id.tvFecha)
        val tvContenido: TextView = view.findViewById(R.id.tvContenido)
        val tvEstadoAnimo: TextView = view.findViewById(R.id.tvEstadoAnimo)
        val tvActividadDia: TextView = view.findViewById(R.id.tvActividadDia)
        val tvReflexion: TextView = view.findViewById(R.id.tvReflexion)
        val btnCerrar: Button = view.findViewById(R.id.btnCerrar)

        tvTitulo.text = entrada.titulo
        tvFecha.text = entrada.fecha
        tvContenido.text = entrada.contenido
        tvEstadoAnimo.text = entrada.estadoAnimo
        tvActividadDia.text = entrada.actividadDia
        tvReflexion.text = entrada.reflexion

        btnCerrar.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}