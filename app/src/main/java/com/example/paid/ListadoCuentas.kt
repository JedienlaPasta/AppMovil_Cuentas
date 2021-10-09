package com.example.paid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_listado_cuentas.*

class ListadoCuentas : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_barra_tareas_agregar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_cuentas)

        var listaCuentasBD = emptyList<Cuentas>()
        val database = AppDataBase.getDataBase(this)

        database.cuentasFunc().getAll().observe(this, {listaCuentasBD = it
            val adapter = ListadoCuentasAdapter(this, listaCuentasBD)
            listadoCuentas.adapter = adapter
        })

        /*
        //No borré esta lista porque me sirve para ver que datos ingresar en caso de que tenga que borrar todos los datos

        val listaCuentas = listOf(
            Cuentas("Netflix", "Suscripcion Mensual", "2021-10-18", "2021-10-15", 10700, false, R.drawable.netflix),
            Cuentas("Spotify", "Suscripcion Mensual", "2021-10-18", "2021-10-15", 6290, true, R.drawable.spotify),
            Cuentas("Youtube Premium", "Suscripcion Mensual", "2021-10-18", "2021-10-15", 4100, false, R.drawable.youtube),
            Cuentas("Prime Video", "Suscripcion Mensual", "2021-10-18", "2021-10-15", 4165, true, R.drawable.primevideo),
            Cuentas("Disney+", "Suscripcion Anual", "2021-10-18", "2021-10-15", 64900, false, R.drawable.disneyplus),
            Cuentas("Discord Nitro", "Suscripcion Anual", "2021-10-18", "2021-10-15", 81083, true, R.drawable.discord),
            Cuentas("VTR", "Suscripcion Mensual", "2021-10-18", "2021-10-15", 51990, false, R.drawable.vtr),
            Cuentas("Movistar", "Suscripcion Mensual", "2021-10-18", "2021-10-15", 14990, true, R.drawable.movistar),
            Cuentas("HBO MAX", "Suscripcion Mensual", "2021-10-18", "2021-10-15", 3500, false, R.drawable.hbomax),
        )
        val adapter = ListadoCuentasAdapter(this, listaCuentas)
        listadoCuentas.adapter = adapter
        */

        listadoCuentas.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, ListadoCuentasOpcion::class.java)
            intent.putExtra("cuenta", listaCuentasBD[position])

            startActivity(intent)
            finish()
        }
    }
    // Menu Barra Tareas
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            // 1
            R.id.menu_volver -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            // 2
            R.id.menu_config -> {
                // Config
            }
            // 3
            R.id.menu_agregar -> {
                val intent = Intent(this, ListadoCuentasIngresar::class.java)
                val extras = Bundle()
                extras.putString("new", "NuevaCuenta")
                extras.putString("add", "Insertar") // me parece que este valor ya no lo estoy usando - - -

                intent.putExtras(extras)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}