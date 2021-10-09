package com.example.paid

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.example.paid.databinding.ActivityListadoCuentasIngresarBinding
import com.example.paid.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_listado_cuentas_ingresar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ListadoCuentasIngresar : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding: ActivityListadoCuentasIngresarBinding
    private lateinit var cuenta: Cuentas
    private var queryType: String? = "nada por ahora"
    private var key: String? = "nada por ahora"
    // imagen por defecto
    var imagenCuenta: Int = R.drawable.bill
    // valores date picker
    var dd = 0
    var mm = 0
    var yy = 0
    // s -> save
    var sdd = 0
    var smm = 0
    var syy = 0
    private var dateType: String = ""

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_barra_tareas, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListadoCuentasIngresarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_listado_cuentas_ingresar) // valor que estaba por defecto

        // Para saber que tipo de operacion se debe hacer
        if (intent.hasExtra("update")) {
            queryType = intent.getStringExtra("update")
            key = "update" // me parece que este valor ya no lo estoy usando - - -
        }
        // Para mantener el id en caso de que la cuenta tenga un id
        var idCuenta = 0
        if (intent.hasExtra("id")) {
            idCuenta = intent.getIntExtra("id", 0)
        }
        if(intent.hasExtra("cuenta")) {
            cuenta = intent.extras?.getSerializable("cuenta") as Cuentas
            if (queryType == "Actualizar") {
                idCuenta = cuenta.idCuenta
            }
            iet_nombre.setText(cuenta.nombre)
            atv_descripcion.setText(cuenta.descripcion)
            tv_fecha_pago.text = cuenta.fecha
            tv_fecha_recordar.text = cuenta.alerta
            iv_nueva_cuenta.setImageDrawable(AppCompatResources.getDrawable(this, cuenta.image))
            // Es necesario para cuando se cambia de activity entre ListadoCuentasIngresar y ElegirImagenCuenta
            if (cuenta.monto == 0) {
                iet_costo.setText("")
            }
            else {
                iet_costo.setText(cuenta.monto.toString())
            }
            imagenCuenta = cuenta.image
        }
        // Elegir imagen manteniendo el objeto y id
        iv_nueva_cuenta.setOnClickListener {
            populateCuenta()
            val intent = Intent(this, ElegirImagenCuenta::class.java)
            val extras = Bundle()
            extras.putSerializable("cuenta", cuenta)
            if (idCuenta != 0) {
                extras.putInt("id", idCuenta)
            }
            intent.putExtras(extras)
            startActivity(intent)
            finish()
        }
        // Falta verificar que los datos ingresados sean validos
        btn_agregar.setOnClickListener {
            populateCuenta()
            val database = AppDataBase.getDataBase(this)
            // Actualizar o Ingresar datos a la BD
            if (idCuenta != 0) {
                CoroutineScope(Dispatchers.IO).launch {
                    cuenta.idCuenta = idCuenta
                    database.cuentasFunc().update(cuenta)
                    this@ListadoCuentasIngresar.finish()
                }
            }
            else {
                CoroutineScope(Dispatchers.IO).launch {
                    database.cuentasFunc().insertAll(cuenta)
                    this@ListadoCuentasIngresar.finish()
                }
            }
            // Luego de ingresar o actualizar vuelve al listado de cuentas
            returnToLastActivity()
            Toast.makeText(this, "Valores Ingresados", Toast.LENGTH_SHORT).show()
        }
        // Botones Date Picker
        btn_fecha_pago.setOnClickListener {
            pickDate()
            dateType = "pago"
        }
        btn_recordar.setOnClickListener {
            pickDate()
            dateType = "recordar"
        }
        // Descripcion DropDown Menu
        val tipo = resources.getStringArray(R.array.tipo_suscripcion)
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, tipo)
        with(binding.atvDescripcion) {
            setAdapter(adapter)
        }
    }
    // Funciones ==================================================================================
    // Menu opciones
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            // 1
            R.id.menu_volver -> {
                returnToLastActivity()
            }
            // 2
            R.id.menu_config -> {
                // Config
            }
        }
        return super.onOptionsItemSelected(item)
    }
    // Para volver al activity anterior
    private fun returnToLastActivity(){
        val intent = Intent(this, ListadoCuentas::class.java)
        startActivity(intent)
        finish()
    }
    // Rellena los campos del objeto cuenta
    private fun populateCuenta() {
        val nombre = iet_nombre.text.toString()
        val descripcion = atv_descripcion.text.toString()
        val fechaPago = tv_fecha_pago.text.toString()
        val fechaRecordar = tv_fecha_recordar.text.toString()
        val costo: Int
        val image: Int
        val pago: Boolean
        // Verifica el valor del campo costo
        if (iet_costo.text.toString() == "") {
            costo = 0
        }
        else {
            costo = iet_costo.text.toString().toInt()
        }
        // Verifica el id de la imagen
        if (imagenCuenta != R.drawable.bill) {
            image = cuenta.image
        }
        else {
            image = R.drawable.bill
        }
        // Verifica si la cuenta es nueva o no, para asignar el valor de pago: Bool
        if (intent.hasExtra("new")) {
            pago = false
            //Toast.makeText(this, "Cuenta Nueva", Toast.LENGTH_SHORT).show()
        }
        else {
            pago = cuenta.pago
        }
        // Se agregan todos los valores a la cuenta
        cuenta = Cuentas(nombre, descripcion, fechaPago, fechaRecordar, costo, pago, image)
    }
    // Date Picker ================================================================================
    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        dd = cal.get(Calendar.DAY_OF_MONTH)
        mm = cal.get(Calendar.MONTH)
        yy = cal.get(Calendar.YEAR)
    }
    private fun pickDate() {
        getDateTimeCalendar()
        DatePickerDialog(this, this, yy, mm, dd).show()
    }
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        sdd = dayOfMonth
        smm = month + 1
        syy = year
        getDateTimeCalendar()
        displayDateValues(dateType)
    }
    private fun displayDateValues(type: String) {
        if (type == "pago") {
            tv_fecha_pago.text = "$sdd/$smm/$syy"
            dateType = ""
        }
        else {
            tv_fecha_recordar.text = "$sdd/$smm/$syy"
            dateType = ""
        }
    }
}