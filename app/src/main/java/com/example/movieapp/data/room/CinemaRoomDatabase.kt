package com.example.movieapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(entities = [Cinema::class, MovieEntity::class], version = 3, exportSchema = false)
abstract class CinemaRoomDatabase: RoomDatabase() {
    abstract fun cinemaDao(): CinemaDao

    companion object {
        @Volatile
        private var INSTANCE: CinemaRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): CinemaRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CinemaRoomDatabase::class.java,
                    "cinema_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(CinemaDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }

    }
    private class CinemaDatabaseCallback(
        private val scope: CoroutineScope
    ): RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.cinemaDao())
                }
            }
        }

        suspend fun populateDatabase(cinemaDao: CinemaDao) {
            cinemaDao.deleteAll()

            var cinema = Cinema(
                1,
                "Chaplin Mega Alma-Ata",
                "г. Алматы, Розыбакиева, 263, ТРЦ MEGA Alma-Ata, 2 этаж",
                "со стороны Розыбакиева",
                "со стороны улицы Сатпаева",
                "+7 747 174 06 07",
                "имеется",
                "/i/files/media/chaplin-mega-almaty.jpg?h=300",
                43.264039,
                76.929475
                )
            cinemaDao.insert(cinema)
            cinema = Cinema(
                2,
                "Bekmambetov Cinema",
                "г. Алматы пр. Абая, 109, МФК «Globus»",
                "по пр. Абая пересечение улицы Ауэзова",
                "со стороны проспекта Абая",
                "8(727) 356-98-78",
                "имеется",
                "/i/files/media/bekmambetov-cinema.jpg?h=300",
                43.240263,
                76.905654
            )
            cinemaDao.insert(cinema)
            cinema = Cinema (
                3,
                "Киноцентр Арман (ТРЦ Asia Park)",
                "г. Алматы, пр. Райымбека 514а, уг. ул. Саина, Торгово-развлекательный центр «Asia Park»",
                "наземная, заезд со стороны проспекта Райымбека и улицы Саина",
                "со стороны проспекта Райымбека и со стороны улицы Саина",
                "+7 727 343 51 00, +7 727 343 51 11",
                "имеется",
                "/i/files/media/kinocentr-arman-trc-asia-park.jpg?h=300",
                43.242221,
                76.957644
            )

            cinemaDao.insert(cinema)
            cinema = Cinema (
                4,
                "LUMIERA Cinema",
                "г. Алматы, пр. Абылай хана, 62, «Арбат»",
                "за ЦУМом по улице Алимжанова",
                "со стороны улицы Сейфулина и со стороны улицы Макатаева",
                "+7 727 222 23 23, +7 707 782 82 11",
                "имеется",
                "/i/files/media/kinoteatr-lumiera-almaty-0.jpg?h=300",
                43.262118,
                76.941373
            )

            cinemaDao.insert(cinema)
            cinema = Cinema (
                5,
                "CINEMAX Dostyk Multiplex / Dolby Atmos 3D",
                "г. Алматы, Самал-2, пр. Достык 111, уг. ул. Жолдасбекова, ТРЦ Dostyk Plaza",
                "заезд на парковку со стороны проспекта Достык и со стороны улицы Жолдасбекова",
                "со стороны улицы Сейфулина и со стороны улицы Макатаева",
                "+7 727 222 00 77, +7 727 225 39 01, +7 701 026 73 69",
                "имеется",
                "/i/files/media/cinemax-dostyk-plaza2.jpg?h=300",
                43.233015,
                76.955765
            )
            cinemaDao.insert(cinema)
            cinema = Cinema (
                6,
                "Кинотеатр Kinoplexx Sary Arka 3D (г. Алматы)",
                "г. Алматы, ул. Алтынсарина, 24",
                "имется наземная Парковка для посетителей платная — 200 тг",
                "со стороны проспекта Алтынсарина",
                "8 727 277 0038",
                "имеется",
                "/i/files/media/kinoplexx sary arka2019.jpg?h=300",
                43.228496,
                76.857868
            )
            cinemaDao.insert(cinema)
            cinema = Cinema (
                7,
                "ТТИ «Алатау» 3D (г. Алматы)",
                "г. Алматы, микрорайон Нұркент, 6.",
                "наземная парковка на 50 мест. Бесплатная. Въезд со стороны улицы Бауыржана Момышулы",
                "со стороны улицы Бауыржана Момышулы",
                "8 (727) 398–85–36, 8 (727) 224–89–77",
                "имеется",
                "/i/files/media/tti-alatau-3d.jpg?h=300",
                43.260613,
                76.820057
            )
            cinemaDao.insert(cinema)
            cinema = Cinema (
                8,
                "Kinopark 11 (Есентай) IMAX (г. Алматы)",
                "г. Алматы, пр. Аль-Фараби, 77/8, ТЦ «Есентай-Молл»",
                "имеется подземная, платная — первые 20 минут - бесплатно; 1 час и каждые последующие часы – по 150 тенге",
                "со стороны Аль-Фараби",
                "+7-701-762-45-11",
                "имеется",
                "/i/files/media/kinopark-11-jesentaj-imax.jpg?h=300",
                43.218290,
                76.927638
            )
            cinemaDao.insert(cinema)
            cinema = Cinema (
                9,
                "Kinopark 8 Moskva (г. Алматы)",
                "г. Алматы, пр. Абая, уг. Алтынсарина, ТРЦ MOSKVA Metropolitan",
                "имется наземная и подземная . Первые 15 минут бесплатно, час 200 тенге",
                "со стороны проспекта Алтынсарина и проспекта Абая",
                "+7 778 099 09 17, + 7727 331 76 99",
                "имеется",
                "/i/files/media/kinopark-8-moskva.jpg?h=300",
                43.226886,
                76.864135
            )
            cinemaDao.insert(cinema)
            cinema = Cinema (
                10,
                "Кинотеатр «Цезарь 3D»",
                "г. Алматы, ул. Фурманова, 50, уг. ул. Гоголя",
                "Парковка кинотеатра временно не работает",
                "со стороны проспекта Назарбаева",
                "+7 727 273-63-93",
                "кафе (300 метров от кинотеатра)",
                "/i/files/media/caesar2019.jpg?h=300",
                43.261020,
                76.946446
            )
            cinemaDao.insert(cinema)
        }
    }
}