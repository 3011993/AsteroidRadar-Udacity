package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM AsteroidDatabaseEntities ")
    fun getAllAsteroids(): Flow<List<AsteroidDatabaseEntities>>

    @Query("SELECT * FROM AsteroidDatabaseEntities WHERE closeApproachDate >= :startDate and closeApproachDate <= :endDate ORDER BY closeApproachDate")
    fun getAsteroidOfRange(startDate : String , endDate : String) : Flow<List<AsteroidDatabaseEntities>?>

    @Insert(onConflict =OnConflictStrategy.REPLACE)
    fun insertAllAsteroids(vararg asteroids: AsteroidDatabaseEntities)

    //methods for image
    @Query("SELECT * FROM DatabaseForImages")
    fun getImage () : LiveData<DatabaseForImages>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(images: DatabaseForImages)
}



@Database(entities = [AsteroidDatabaseEntities::class,DatabaseForImages::class], version = 2)
abstract class AsteroidDatabase : RoomDatabase(){

    abstract val asteroidDao : AsteroidDao

}

private lateinit var INSTANCE: AsteroidDatabase


fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java, "asteroidDatabase"
            ).fallbackToDestructiveMigration().build()
        }
    }
    return INSTANCE
}
