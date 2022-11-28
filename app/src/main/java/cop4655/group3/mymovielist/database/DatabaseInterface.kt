package cop4655.group3.mymovielist.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import cop4655.group3.mymovielist.data.*
import java.util.*
import kotlin.collections.ArrayList

class DatabaseInterface(context: Context) : SQLiteOpenHelper(context, "mymovielist", null, 2) {
    override fun onCreate(db: SQLiteDatabase?) {
        val movieTable = """
            CREATE TABLE movie (
                ImdbId VARCHAR(16) PRIMARY KEY,
                Actors TEXT,
                Awards TEXT,
                BoxOffice TEXT,
                Country TEXT,
                Dvd TEXT,
                Director TEXT,
                Genre TEXT,
                Language TEXT,
                Metascore TEXT,
                Plot TEXT,
                Poster TEXT,
                Production TEXT,
                Rated TEXT,
                
                Released TEXT,
                Response TEXT,
                Runtime TEXT,
                Title TEXT,
                Type TEXT,
                Website TEXT,
                Writer TEXT,
                Year TEXT,
                ImdbRating TEXT,
                ImdbVotes TEXT
            );
        """.trimIndent()

        val myMovieDataTable = """
            CREATE TABLE my_movie_data (
                ImdbId VARCHAR(16) PRIMARY KEY,
                Stars INTEGER CHECK (Stars IN (1, 5)),
                Heart BOOLEAN NOT NULL CHECK (Heart IN (0, 1)),
                PlanAddDate VARCHAR(27) DEFAULT NULL,
                HistoryAddDate VARCHAR(27) DEFAULT NULL,
                FOREIGN KEY (ImdbId) REFERENCES movie(ImdbId)                
            )
        """.trimIndent()

        val ratingTable = """
            CREATE TABLE rating (
                RatingId INTEGER PRIMARY KEY AUTOINCREMENT,
                ImdbId VARCHAR(16) NOT NULL,
                Source TEXT,
                Value TEXT,
                FOREIGN KEY (ImdbId) REFERENCES movie(ImdbId)
            );
        """.trimIndent()

        val myRatingTable = """
            CREATE TABLE my_rating (
                ImdbId VARCHAR(16) PRIMARY KEY,
                Stars INTEGER CHECK (Stars IN (1, 5)),
                Hearts BOOLEAN NOT NULL CHECK (Hearts IN (0, 1)),
                FOREIGN KEY (ImdbId) REFERENCES movie(ImdbId)
            );
        """.trimIndent()

        val moviePlanTable = """
            CREATE TABLE movie_plan (
                ImdbId VARCHAR(16) NOT NULL,
                DateAdded VARCHAR(27),
                FOREIGN KEY (ImdbId) REFERENCES movie(ImdbId) 
            );
        """.trimIndent()

        val movieHistoryTable = """
            CREATE TABLE movie_history (
                ImdbId VARCHAR(16) NOT NULL,
                DateAdded VARCHAR(27),
                FOREIGN KEY (ImdbId) REFERENCES movie(ImdbId)
            );
        """.trimIndent()

        db?.let {
            db.execSQL(movieTable)
            db.execSQL(myMovieDataTable)
            db.execSQL(ratingTable)
            db.execSQL(myRatingTable)
            db.execSQL(moviePlanTable)
            db.execSQL(movieHistoryTable)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion == 1) {
            val oldMovieTable = """
                DROP TABLE movie;
            """.trimIndent()

            db?.execSQL(oldMovieTable)

            val newMovieTable = """
                CREATE TABLE movie (
                    ImdbId VARCHAR(16) PRIMARY KEY,
                    Actors TEXT,
                    Awards TEXT,
                    BoxOffice TEXT,
                    Country TEXT,
                    Dvd TEXT,
                    Director TEXT,
                    Genre TEXT,
                    Language TEXT,
                    Metascore TEXT,
                    Plot TEXT,
                    Poster TEXT,
                    Production TEXT,
                    Rated TEXT,
                    
                    Released TEXT,
                    Response TEXT,
                    Runtime TEXT,
                    Title TEXT,
                    Type TEXT,
                    Website TEXT,
                    Writer TEXT,
                    Year TEXT,
                    ImdbRating TEXT,
                    ImdbVotes TEXT
                );
            """.trimIndent()

            db?.execSQL(newMovieTable)
        }
    }

    fun insertRawMovie(rawMovieData: RawMovieData) {
        val database = this.writableDatabase
        val movieValues = ContentValues()

        movieValues.put("Actors", rawMovieData.Actors)
        movieValues.put("Awards", rawMovieData.Awards)
        movieValues.put("BoxOffice", rawMovieData.BoxOffice)
        movieValues.put("Country", rawMovieData.Country)
        movieValues.put("Dvd", rawMovieData.DVD)
        movieValues.put("Director", rawMovieData.Director)
        movieValues.put("Genre", rawMovieData.Genre)
        movieValues.put("Language", rawMovieData.Language)
        movieValues.put("Metascore", rawMovieData.Metascore)
        movieValues.put("Plot", rawMovieData.Plot)
        movieValues.put("Poster", rawMovieData.Poster)
        movieValues.put("Production", rawMovieData.Production)
        movieValues.put("Rated", rawMovieData.Rated)
        // ratings
        movieValues.put("Released", rawMovieData.Released)
        movieValues.put("Response", rawMovieData.Response)
        movieValues.put("Runtime", rawMovieData.Runtime)
        movieValues.put("Title", rawMovieData.Title)
        movieValues.put("Type", rawMovieData.Type)
        movieValues.put("Website", rawMovieData.Website)
        movieValues.put("Writer", rawMovieData.Writer)
        movieValues.put("Year", rawMovieData.Year)
        movieValues.put("ImdbId", rawMovieData.imdbID)
        movieValues.put("ImdbRating", rawMovieData.imdbRating)
        movieValues.put("ImdbVotes", rawMovieData.imdbVotes)

        database.insert("movie", null, movieValues)

        rawMovieData.Ratings?.let { ratings ->
            for (rating in ratings) {
                rating?.let {
                    val ratingValues = ContentValues()

                    ratingValues.put("ImdbId", rawMovieData.imdbID)
                    ratingValues.put("Source", rating.Source)
                    ratingValues.put("Source", rating.Value)

                    database.insert("rating", null, ratingValues)
                }
            }
        }
    }

    fun getRawMovie(imdbId: String): RawMovieData? {
        val database = this.writableDatabase

        val movieQuery = """
            SELECT
                Actors,
                Awards,
                BoxOffice,
                Country,
                Dvd,
                Director,
                Genre,
                Language,
                Metascore,
                Plot,
                Poster,
                Production,
                Rated,
                
                Released,
                Response,
                Runtime,
                Title,
                Type,
                Website,
                Writer,
                Year,
                ImdbId,
                ImdbRating,
                ImdbVotes
            FROM movie
            WHERE ImdbId = "$imdbId";
        """.trimIndent()

        val movieResult = database.rawQuery(movieQuery, null)
        if (movieResult.moveToNext()) {
            val rawMovieData = RawMovieData(
                movieResult.getStringOrNull(0),
                movieResult.getStringOrNull(1),
                movieResult.getStringOrNull(2),
                movieResult.getStringOrNull(3),
                movieResult.getStringOrNull(4),
                movieResult.getStringOrNull(5),
                movieResult.getStringOrNull(6),
                movieResult.getStringOrNull(7),
                movieResult.getStringOrNull(8),
                movieResult.getStringOrNull(9),
                movieResult.getStringOrNull(10),
                movieResult.getStringOrNull(11),
                movieResult.getStringOrNull(12),
                ArrayList<Rating?>(),
                movieResult.getStringOrNull(13),
                movieResult.getStringOrNull(14),
                movieResult.getStringOrNull(15),
                movieResult.getStringOrNull(16),
                movieResult.getStringOrNull(17),
                movieResult.getStringOrNull(18),
                movieResult.getStringOrNull(19),
                movieResult.getStringOrNull(20),
                movieResult.getStringOrNull(21),
                movieResult.getStringOrNull(22),
                movieResult.getStringOrNull(23)
            )

            val ratingsQuery = """
                SELECT
                    Source,
                    Value
                FROM rating
                WHERE ImdbId = "$imdbId";
            """.trimIndent()

            val ratingsResult = database.rawQuery(ratingsQuery, null)

            while (ratingsResult.moveToNext()) {
                val rating = Rating(
                    ratingsResult.getStringOrNull(0),
                    ratingsResult.getStringOrNull(1)
                )
                rawMovieData.Ratings?.add(rating)
            }

            return rawMovieData
        }
        return null
    }

    fun insertMyMovieData(
        imdbId: String,
        myMovieData: MyMovieData,
    ) {
        val database = this.writableDatabase
        val myMovieDataValues = ContentValues()

        myMovieDataValues.put("ImdbId", imdbId)
        myMovieDataValues.put("Stars", myMovieData.stars)
        myMovieDataValues.put("Heart", myMovieData.heart)
        myMovieData.planList?.let { planList ->
            myMovieDataValues.put("PlanAddDate", dateToString(planList))
        }
        myMovieData.watchList?.let { watchList ->
            myMovieDataValues.put("HistoryAddDate", dateToString(watchList))
        }

        database.insert("my_movie_data", null, myMovieDataValues)
    }

    fun getMyMovieData(imdbId: String): MyMovieData? {
        val database = this.writableDatabase

        val myRatingQuery = """
            SELECT
                Stars,
                Heart,
                PlanAddDate,
                HistoryAddDate
            FROM my_movie_data
            WHERE ImdbId = "$imdbId";
        """.trimIndent()

        val myMovieDataResult = database.rawQuery(myRatingQuery, null)

        if (myMovieDataResult.moveToNext()) {
            val myMovieData = MyMovieData(
                myMovieDataResult.getIntOrNull(0),
                myMovieDataResult.getInt(1) == 1,
                myMovieDataResult.getStringOrNull(2)?.let { string -> stringToDate(string) },
                myMovieDataResult.getStringOrNull(3)?.let { string -> stringToDate(string) }
            )
            myMovieDataResult.close()
            return myMovieData
        }
        myMovieDataResult.close()
        return null
    }

    fun insertMyRating(myRating: MyRating, imdbId: String) {
        val database = this.writableDatabase
        val myRatingValues = ContentValues()

        myRatingValues.put("ImdbId", imdbId)
        myRatingValues.put("Stars", myRating.stars)
        myRatingValues.put("Heart", myRating.heart)

        database.insert("myRating", null, myRatingValues)
    }

    fun getMyRating(imdbId: String): MyRating? {
        val database = this.writableDatabase

        val myRatingQuery = """
            SELECT
                Stars,
                Hearts
            FROM my_rating
            WHERE ImdbId = "$imdbId";
        """.trimIndent()

        val myRatingResult = database.rawQuery(myRatingQuery, null)

        if (myRatingResult.moveToNext()) {
            val myRating = MyRating(
                myRatingResult.getInt(0),
                myRatingResult.getInt(1) == 1
            )
            myRatingResult.close()
            return myRating
        }
        myRatingResult.close()
        return null
    }

    fun insertMoviePlan(imdbId: String, calendar: Calendar) {
        val database = this.writableDatabase

        val moviePlanValues = ContentValues()

        moviePlanValues.put("ImdbId", imdbId)
        moviePlanValues.put("DateAdded", dateToString(calendar))

        database.insert("movie_plan", null, moviePlanValues)
    }

    /**
     * Returns true if the movie is in the watch plan.
     * The date the movie was added will be inserted into the optional calendar parameter if it exists.
     */
    fun isInMoviePlan(imdbId: String, calendar: Calendar? = null): Boolean {
        val database = this.writableDatabase

        val moviePlanQuery = """
            SELECT DateAdded
            FROM movie_plan
            WHERE ImdbId = "$imdbId";
        """.trimIndent()

        val result = database.rawQuery(moviePlanQuery, null)

        if (result.moveToNext()) {
            if (calendar != null) {
                result.getStringOrNull(2)?.let { string -> stringToDate(string, calendar) }
            }
            return true
        }
        return false
    }

    fun insertMovieHistory(imdbId: String, calendar: Calendar) {
        val database = this.writableDatabase

        val movieHistoryValues = ContentValues()

        movieHistoryValues.put("ImdbId", imdbId)
        movieHistoryValues.put("DateAdded", dateToString(calendar))

        database.insert("movie_history", null, movieHistoryValues)
    }

    /**
     * Returns true if the movie is in the watch history.
     * The date the movie was added will be inserted into the optional calendar parameter if it exists.
     */
    fun isInMovieHistory(imdbId: String, calendar: Calendar? = null): Boolean {
        val database = this.writableDatabase

        val moviePlanQuery = """
            SELECT DateAdded
            FROM movie_plan
            WHERE ImdbId = "$imdbId";
        """.trimIndent()

        val result = database.rawQuery(moviePlanQuery, null)

        if (result.moveToNext()) {
            if (calendar != null) {
                result.getStringOrNull(2)?.let { string -> stringToDate(string, calendar) }
            }
            return true
        }
        return false
    }

    private fun dateToString(calendar: Calendar): String {
        return String.format(
            "%04d:%02d:%02d:%02d:%02d:%d",
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.ZONE_OFFSET)
        )
    }

    /**
     * If a Calendar is given as an optional parameter that calendar will have the data inserted into it and will be returned.
     * Otherwise a new calendar will be created and returned.
     */
    private fun stringToDate(string: String, calendar: Calendar? = null): Calendar {
        val output = calendar ?: Calendar.getInstance()
        val strings = string.split(":")
        val nums = strings.map { s -> s.toInt() }
        output.set(Calendar.YEAR, nums[0])
        output.set(Calendar.MONTH, nums[1])
        output.set(Calendar.DAY_OF_MONTH, nums[2])
        output.set(Calendar.HOUR_OF_DAY, nums[3])
        output.set(Calendar.MINUTE, nums[4])
        output.set(Calendar.ZONE_OFFSET, nums[5])
        return output
    }

    fun getFullMovieData(imdbId: String): MovieData? {
        return getRawMovie(imdbId)?.let { rawMovieData ->
            getMyMovieData(imdbId)?.let { myMovieData ->
                MovieData(rawMovieData, myMovieData)
            }
        }
    }

    fun insertFullMovieData(movieData: MovieData) {
        movieData.rawMovieData.imdbID?.let { imdbId ->
            insertRawMovie(movieData.rawMovieData)
            insertMyMovieData(
                imdbId,
                movieData.myMovieData,
            )
        }
    }

    // implement search queries
}