package com.example.paid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.listado_cuentas_listview.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

class ListadoCuentasAdapter (private val mContext: Context, private val listadoCuentas: List<Cuentas>): ArrayAdapter<Cuentas>(mContext, 0, listadoCuentas)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layout = LayoutInflater.from(mContext).inflate(R.layout.listado_cuentas_listview, parent, false)

        val cuentas = listadoCuentas[position]

        // lclv es la abreviacion de listado_cuentas_listView
        layout.tv_lclv_nombre.text = cuentas.nombre
        layout.tv_lclv_desc.text = cuentas.descripcion
        layout.tv_lclv_monto.text = "$" + cuentas.monto.toString()
        layout.iv_lclv_image.setImageResource(cuentas.image)
        // daysLeft
        layout.daysLeft.text = daysBetween(listadoCuentas[position].fecha, todaysDate()).toString() + " días"

        return layout
    }

    // Para el formato de fecha que se necesita
    private fun formatoFecha(fecha: String, output: String): String {
        val formattedDate: String
        when (output) {
            "dd/MM" -> {
                val parser = SimpleDateFormat("dd/MM/yyyy")
                val formatter = SimpleDateFormat("dd/MM")
                formattedDate = formatter.format(parser.parse(fecha))
            }
            "dd" -> {
                val parser = SimpleDateFormat("dd/MM/yyyy")
                val formatter = SimpleDateFormat("dd")
                formattedDate = formatter.format(parser.parse(fecha))
            }
            "MM" -> {
                val parser = SimpleDateFormat("dd/MM/yyyy")
                val formatter = SimpleDateFormat("MM")
                formattedDate = formatter.format(parser.parse(fecha))
            }
            else -> {
                val parser = SimpleDateFormat("dd/MM/yyyy")
                val formatter = SimpleDateFormat("yyyy")
                formattedDate = formatter.format(parser.parse(fecha))
            }
        }
        return formattedDate
    }
    // Devuelve la fecha de hoy
    private fun todaysDate():String {
        val cal = Calendar.getInstance()
        val dd = cal.get(Calendar.DAY_OF_MONTH)
        val mm = cal.get(Calendar.MONTH) + 1
        val yy = cal.get(Calendar.YEAR)
        return "$dd/$mm/$yy"
    }
    // Devuelve la cantidad de dias entre dos fechas
    private fun daysBetween(fecha1: String, fecha2: String): Long {
        val dd1 = formatoFecha(fecha1, "dd")
        val dd2 = formatoFecha(fecha2, "dd")
        val mm1 = formatoFecha(fecha1, "MM")
        val mm2 = formatoFecha(fecha2, "MM")

        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()

        cal1.set(Calendar.YEAR, mm1.toInt(), dd1.toInt())
        cal2.set(Calendar.YEAR, mm2.toInt(), dd2.toInt())

        val millis1 = cal1.timeInMillis
        val millis2 = cal2.timeInMillis

        val diff = (millis2 - millis1).absoluteValue

        return diff / (24 * 60 * 60 * 1000)
    }
}