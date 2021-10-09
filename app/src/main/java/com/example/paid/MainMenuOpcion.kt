package com.example.paid

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import kotlinx.android.synthetic.main.activity_main_menu_opcion.*

class MainMenuOpcion : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_barra_tareas, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu_opcion)

        // Se reciben los valores pasados por el intent y se asignan a los campos del activity

        val textoOpcion = intent.getStringExtra("opcion")
        val descripcion = intent.getStringExtra("descripcion")
        val img = intent.getIntExtra("img", R.drawable.accounting)

        tv_opcion.text = textoOpcion
        tv_descripcion.text = descripcion
        iv_image.setImageDrawable(ContextCompat.getDrawable(this, img))
    }

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
        }
        return super.onOptionsItemSelected(item)
    }
}