package com.example.paid

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import kotlinx.android.synthetic.main.activity_listado_cuentas_opcion.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

class ListadoCuentasOpcion : AppCompatActivity() {
    private lateinit var database: AppDataBase
    private lateinit var cuenta: Cuentas
    private lateinit var cuentaUpdate: Cuentas
    private lateinit var cuentaLiveData: LiveData<Cuentas>

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_barra_tareas_editar_eliminar, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_cuentas_opcion)

        database = AppDataBase.getDataBase(this)
        val objCuenta = intent.getSerializableExtra("cuenta") as Cuentas
        val idCuenta = objCuenta.idCuenta
        cuentaUpdate = objCuenta

        cuentaLiveData = database.cuentasFunc().get(idCuenta)
        cuentaLiveData.observe(this, {cuenta = it
            tv_lcop_nombre.text = cuenta.nombre
            tv_lcop_desc.text = cuenta.descripcion + ":"
            tv_lcop_fecha.text = cuenta.fecha
            tv_lcop_monto.text = "$" + cuenta.monto.toString()
            //iv_lcop_image.setImageDrawable(ContextCompat.getDrawable(this, cuenta.image))
            iv_lcop_image.setImageResource(cuenta.image)
            tv_lcop_recordatorio.text = formatoFecha(cuenta.alerta, "dd/MM")
        })
        tv_bool.text = daysBetween(cuentaUpdate.fecha, todaysDate()).toString() // Solo temporal, sacar cuando no se necesite mas ------------------------------------
        // Aqui se actualiza en valor pago en la BD cada vez que se preciona el switch que indica si esta pagada la cuenta o no
        var pagoBool: Boolean = cuentaUpdate.pago
        switch_pago.isChecked = pagoBool
        estadoPagoBool(pagoBool)
        // Click listener
        switch_pago.setOnClickListener {
            val databaseLocal = AppDataBase.getDataBase(this)
            pagoBool = !pagoBool
            switch_pago.isChecked = pagoBool
            estadoPagoBool(pagoBool)
            // Actualizar campo pago
            CoroutineScope(Dispatchers.IO).launch {
                databaseLocal.cuentasFunc().updatePago(cuentaUpdate.idCuenta, pagoBool)
            }
            Toast.makeText(this, "Cuenta Actualizada", Toast.LENGTH_SHORT).show()
        }
    }
    // Menu Barra Tareas
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            // 1
            R.id.menu_volver -> {
                val intent = Intent(this, ListadoCuentas::class.java)
                startActivity(intent)
                finish()
            }
            // 2
            R.id.menu_config -> {
                // Config
            }
            // 3
            R.id.menu_eliminar -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(resources.getString(R.string.eliminar))
                builder.setMessage(resources.getString(R.string.seguroEliminar))
                builder.setPositiveButton(resources.getString(R.string.continuar), { dialogInterface: DialogInterface, i: Int -> eliminar() }) // Accion
                builder.setNegativeButton(resources.getString(R.string.cancelar), { dialogInterface: DialogInterface, i:Int -> "" })
                builder.show()
            }
            // 4
            R.id.menu_editar -> {
                val intent = Intent(this, ListadoCuentasIngresar::class.java)
                val extras = Bundle()
                // Se envia el objeto cuenta y la operacion
                extras.putString("update", "Actualizar")
                extras.putSerializable("cuenta", cuenta)

                intent.putExtras(extras)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    // Para eliminar una cuenta
    private fun eliminar () {
        cuentaLiveData.removeObservers(this)
        CoroutineScope(Dispatchers.IO).launch {
            database.cuentasFunc().delete(cuenta)
            this@ListadoCuentasOpcion.finish()
        }
        val intent = Intent(this, ListadoCuentas::class.java)
        startActivity(intent)
        finish()
    }
    // Color y valor del textView pago
    private fun estadoPagoBool(bool: Boolean) {
        if (bool) {
            tv_lcop_pago.text = resources.getString(R.string.pagado)
            tv_lcop_pago.setTextColor(resources.getColor(R.color.md_green_A400))
        }
        else {
            tv_lcop_pago.text = resources.getString(R.string.sinPagar)
            tv_lcop_pago.setTextColor(resources.getColor(R.color.md_red_500))
        }
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