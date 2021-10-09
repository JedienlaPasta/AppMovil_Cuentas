package com.example.paid

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.RoomDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KClass
import kotlin.reflect.typeOf

class MainActivity : AppCompatActivity() {
    private lateinit var menuAdapter: MainMenuAdapter
    private lateinit var dataList: List<MainMenu>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 2)
        menuAdapter = MainMenuAdapter(applicationContext)
        recyclerView.adapter = menuAdapter

        dataList = listOf(
            MainMenu(resources.getString(R.string.cuentas), resources.getString(R.string.registros), R.drawable.accounting2),
            MainMenu(resources.getString(R.string.contactos), resources.getString(R.string.importantes), R.drawable.profile),
            MainMenu(resources.getString(R.string.reportes), resources.getString(R.string.gastos), R.drawable.report),
            MainMenu(resources.getString(R.string.calendario), resources.getString(R.string.alertas), R.drawable.schedule),
            MainMenu(resources.getString(R.string.listas), resources.getString(R.string.notas), R.drawable.lista),
            MainMenu(resources.getString(R.string.config), resources.getString(R.string.personalizar), R.drawable.config)
        )

        menuAdapter.setDataList(dataList)

        menuAdapter.onItemClick = { position ->
            when (position) {
                0 -> {
                    val intent = Intent(this, ListadoCuentas::class.java)
                    val extras = Bundle()
                    addToIntent(dataList, position, extras)

                    intent.putExtras(extras)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    val intent = Intent(this, MainMenuOpcion::class.java)
                    val extras = Bundle()
                    addToIntent(dataList, position, extras)

                    intent.putExtras(extras)
                    startActivity(intent)
                    finish()
                }
            }
        }
        // Eliminar todos los datos
        /*
        val database = AppDataBase.getDataBase(this)
        CoroutineScope(Dispatchers.IO).launch {
            database.clearAllTables()
        }*/
    }
    // Se agregan los parametros en extras para pasarlos al intent
    private fun addToIntent(lista: List<MainMenu>, pos: Int, extras: Bundle){
        extras.putString("opcion", lista[pos].texto)
        extras.putString("descripcion", lista[pos].descripcion)
        extras.putInt("img", lista[pos].img)
    }
}