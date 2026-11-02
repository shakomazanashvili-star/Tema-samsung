package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [FavoriteThemeEntity::class, FavoriteWallpaperSetEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteThemeDao(): FavoriteThemeDao
    abstract fun favoriteWallpaperSetDao(): FavoriteWallpaperSetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE favorite_themes ADD COLUMN primaryCategory TEXT NOT NULL DEFAULT 'Minimalist'")
                db.execSQL("ALTER TABLE favorite_themes ADD COLUMN generatedTagsCsv TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE favorite_themes ADD COLUMN vibeDescription TEXT NOT NULL DEFAULT ''")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS favorite_wallpaper_sets (
                        id TEXT PRIMARY KEY NOT NULL,
                        title TEXT NOT NULL,
                        titleGe TEXT NOT NULL,
                        category TEXT NOT NULL,
                        lockscreenResId INTEGER NOT NULL,
                        homescreenResId INTEGER NOT NULL,
                        accentColorHex TEXT NOT NULL,
                        isQuickAccess INTEGER NOT NULL DEFAULT 1,
                        savedAtTimestamp INTEGER NOT NULL DEFAULT 0,
                        tagsCsv TEXT NOT NULL DEFAULT ''
                    )
                """.trimIndent())
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aesthetic_s24_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
