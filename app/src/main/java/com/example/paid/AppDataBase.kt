package com.example.paid

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// [Cuentas::class, Menu::class]
@Database(entities =  [Cuentas::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun cuentasFunc(): CuentasDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDataBase(context: Context): AppDataBase {
            val tempInstance = INSTANCE

            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, AppDataBase::class.java, "app_database").build()

                INSTANCE = instance
                return instance
            }
        }
    }
}