package com.example.paid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ElegirImagenCuenta : AppCompatActivity() {
    private lateinit var imagenAdapter: ImagenAdapter
    private var dataList = mutableListOf<Imagen>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elegir_imagen_cuenta)

        val recView = findViewById<RecyclerView>(R.id.rv_images)
        recView.layoutManager = GridLayoutManager(applicationContext, 3)
        imagenAdapter = ImagenAdapter(applicationContext)
        recView.adapter = imagenAdapter

        // Lista de imagenes a elegir
        dataList.add(Imagen("Netflix", R.drawable.netflix))
        dataList.add(Imagen("Youtube", R.drawable.youtube))
        dataList.add(Imagen("Discord", R.drawable.discord))
        dataList.add(Imagen("Disney+", R.drawable.disneyplus))
        dataList.add(Imagen("Amazon", R.drawable.amazon))
        dataList.add(Imagen("HBO", R.drawable.hbomax))
        dataList.add(Imagen("Movistar", R.drawable.movistar1))
        dataList.add(Imagen("Prime Video", R.drawable.primevideo))
        dataList.add(Imagen("Spotify", R.drawable.spotify))
        dataList.add(Imagen("VTR", R.drawable.vtr))
        dataList.add(Imagen("Twitch", R.drawable.twitch))
        dataList.add(Imagen("Default", R.drawable.bill))

        imagenAdapter.setDataList(dataList)

        // Se recibe intent con el objeto
        val cuenta = intent.getSerializableExtra("cuenta") as Cuentas
        //Toast.makeText(this, cuenta.nombre, Toast.LENGTH_SHORT).show()

        // Para saber que tipo de operacion se debe hacer
        val idCuenta = intent.getIntExtra("id", 0)

        // Se cambia la imagen y se devuelve el objeto con la activity
        imagenAdapter.onItemClick = { id ->
            cuenta.image = id
            val intent = Intent(this, ListadoCuentasIngresar::class.java)
            val extras = Bundle()
            extras.putSerializable("cuenta", cuenta)
            if (idCuenta != 0) {
                extras.putInt("id", idCuenta)
            }
            intent.putExtras(extras)
            startActivity(intent)
            finish()
        }
    }
}