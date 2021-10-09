package com.example.paid

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CuentasDAO {

    @Query("SELECT * FROM Cuentas")
    fun getAll(): LiveData<List<Cuentas>>

    @Query("SELECT * FROM Cuentas WHERE idCuenta = :id")
    fun get(id: Int): LiveData<Cuentas>

    @Query("UPDATE Cuentas SET pago = :valor WHERE idCuenta = :id")
    fun updatePago(id: Int, valor: Boolean)

    @Insert
    fun insertAll(vararg cuenta: Cuentas)
    @Update
    fun update(cuenta: Cuentas)
    @Delete
    fun delete(cuenta: Cuentas)
}