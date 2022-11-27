package cop4655.group3.mymovielist.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import cop4655.group3.mymovielist.data.MyRating
import cop4655.group3.mymovielist.data.Rating
import cop4655.group3.mymovielist.data.RawMovieData
import java.util.*
import kotlin.collections.ArrayList

class DatabaseInterface(context: Context) : SQLiteOpenHelper(context, "mymovielist", null, 1) {
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
                FullPlot TEXT,
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
            db.execSQL(ratingTable)
            db.execSQL(myRatingTable)
            db.execSQL(moviePlanTable)
            db.execSQL(movieHistoryTable)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

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
                FullPlot,
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
            WHERE ImdbId = $imdbId;
        """.trimIndent()

        val movieResult = database.rawQuery(movieQuery, null)
        if (movieResult.moveToNext()) {
            val rawMovieData = RawMovieData(
                movieResult.getString(0),
                movieResult.getString(1),
                movieResult.getString(2),
                movieResult.getString(3),
                movieResult.getString(4),
                movieResult.getString(5),
                movieResult.getString(6),
                movieResult.getString(7),
                movieResult.getString(8),
                movieResult.getString(9),
                movieResult.getString(10),
                movieResult.getString(11),
                movieResult.getString(12),
                ArrayList<Rating?>(),
                movieResult.getString(13),
                movieResult.getString(14),
                movieResult.getString(15),
                movieResult.getString(16),
                movieResult.getString(17),
                movieResult.getString(18),
                movieResult.getString(19),
                movieResult.getString(20),
                movieResult.getString(21),
                movieResult.getString(22),
                movieResult.getString(23)
            )

            val ratingsQuery = """
                SELECT
                    Source,
                    Value
                FROM rating
                WHERE ImdbId = $imdbId;
            """.trimIndent()

            val ratingsResult = database.rawQuery(ratingsQuery, null)

            while (ratingsResult.moveToNext()) {
                val rating = Rating(
                    ratingsResult.getString(0),
                    ratingsResult.getString(1)
                )
                rawMovieData.Ratings?.add(rating)
            }

            return rawMovieData
        }
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
            WHERE ImdbId = $imdbId;
        """.trimIndent()

        val myRatingResult = database.rawQuery(myRatingQuery, null)

        if (myRatingResult.moveToNext()) {
            val myRating = MyRating(
                myRatingResult.getInt(0),
                myRatingResult.getInt(1) == 1
            )
            return myRating
        }
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
            WHERE ImdbId = $imdbId;
        """.trimIndent()

        val result = database.rawQuery(moviePlanQuery, null)

        if (result.moveToNext()) {
            if (calendar != null) {
                stringToDate(result.getString(0), calendar)
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
            WHERE ImdbId = $imdbId;
        """.trimIndent()

        val result = database.rawQuery(moviePlanQuery, null)

        if (result.moveToNext()) {
            if (calendar != null) {
                stringToDate(result.getString(0), calendar)
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
}