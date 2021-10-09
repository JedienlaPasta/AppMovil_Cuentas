package com.example.paid

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Cuentas")
class Cuentas (
    val nombre: String,
    val descripcion: String,
    val fecha: String,
    val alerta: String,
    val monto: Int,
    var pago: Boolean,
    var image: Int,
    @PrimaryKey(autoGenerate = true)
    var idCuenta: Int = 0
    ): Serializable