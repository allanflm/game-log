package com.allan.gamelog.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

// Mudou a tabela? Suba o "version" e crie uma Migration. Nunca use
// fallbackToDestructiveMigration, porque apagaria os jogos do usuário.
@Database(entities = [GameEntity::class], version = 1)
abstract class GameLogDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    companion object {
        private const val DATABASE_NAME = "gamelog.db"

        fun create(context: Context): GameLogDatabase =
            Room.databaseBuilder(context, GameLogDatabase::class.java, DATABASE_NAME).build()
    }
}
