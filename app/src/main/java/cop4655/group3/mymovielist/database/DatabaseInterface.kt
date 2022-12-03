package cop4655.group3.mymovielist.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import cop4655.group3.mymovielist.data.*
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

private val RAWMOVIEDATA_FIELDS = """
    `movie`.`Actors`,
    `movie`.`Awards`,
    `movie`.`BoxOffice`,
    `movie`.`Country`,
    `movie`.`Dvd`,
    `movie`.`Director`,
    `movie`.`Genre`,
    `movie`.`Language`,
    `movie`.`Metascore`,
    `movie`.`Plot`,
    `movie`.`Poster`,
    `movie`.`Production`,
    `movie`.`Rated`,

    `movie`.`Released`,
    `movie`.`Response`,
    `movie`.`Runtime`,
    `movie`.`Title`,
    `movie`.`Type`,
    `movie`.`Website`,
    `movie`.`Writer`,
    `movie`.`Year`,
    `movie`.`ImdbId`,
    `movie`.`ImdbRating`,
    `movie`.`ImdbVotes`
""".trimIndent()
private val MYMOVIEDATA_FIELDS = """
                `my_movie_data`.`Stars`,
                `my_movie_data`.`Heart`,
                `my_movie_data`.`PlanAddDate`,
                `my_movie_data`.`HistoryAddDate`
""".trimIndent()
private val RATING_FIELDS = """
    Source,
    Value
""".trimIndent()

class DatabaseInterface(context: Context) : SQLiteOpenHelper(context, "mymovielist", null, 5) {
    // region createAndUpdate
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
                Stars INTEGER NOT NULL CHECK (Stars >= 0 AND Stars <= 5) DEFAULT 0,
                Heart VARCHAR(27) DEFAULT NULL,
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
        if (oldVersion < 2) {
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

        if (oldVersion < 3) {
            val renameOldMyMovieDataTable = """
            ALTER TABLE my_movie_data 
            RENAME TO old_my_movie_data;
            """.trimIndent()

            val newMyMovieDataTable = """
            CREATE TABLE my_movie_data (
                ImdbId VARCHAR(16) PRIMARY KEY,
                Stars INTEGER CHECK ((Stars > 0 AND Stars < 6) OR Stars IS NULL) DEFAULT NULL,
                Heart BOOLEAN NOT NULL CHECK (Heart IN (0, 1)) DEFAULT 0,
                PlanAddDate VARCHAR(27) DEFAULT NULL,
                HistoryAddDate VARCHAR(27) DEFAULT NULL,
                FOREIGN KEY (ImdbId) REFERENCES movie(ImdbId)                
            );
            """.trimIndent()

            val copyData = """
            INSERT INTO my_movie_data
            SELECT *
            FROM old_my_movie_data;
            """.trimIndent()

            val dropOldTable = """
                DROP TABLE old_my_movie_data;
            """.trimIndent()

            db?.execSQL(renameOldMyMovieDataTable)
            db?.execSQL(newMyMovieDataTable)
            db?.execSQL(copyData)
            db?.execSQL(dropOldTable)
        }

        if (oldVersion < 4) {
            val renameOldMyMovieDataTable = """
            ALTER TABLE my_movie_data 
            RENAME TO old_my_movie_data;
            """.trimIndent()

            val newMyMovieDataTable = """
            CREATE TABLE my_movie_data (
                ImdbId VARCHAR(16) PRIMARY KEY,
                Stars INTEGER NOT NULL CHECK (Stars >= 0 AND Stars <= 5) DEFAULT 0,
                Heart BOOLEAN NOT NULL CHECK (Heart IN (0, 1)) DEFAULT 0,
                PlanAddDate VARCHAR(27) DEFAULT NULL,
                HistoryAddDate VARCHAR(27) DEFAULT NULL,
                FOREIGN KEY (ImdbId) REFERENCES movie(ImdbId)                
            )
        """.trimIndent()

            val copyData = """
            INSERT INTO my_movie_data (
                ImdbId,
                Stars,
                Heart,
                PlanAddDate,
                HistoryAddDate
            )                
            SELECT
                ImdbId,
                IFNULL(Stars, 0),
                Heart,
                PlanAddDate,
                HistoryAddDate
            FROM old_my_movie_data;
            """.trimIndent()

            val dropOldTable = """
                DROP TABLE old_my_movie_data;
            """.trimIndent()

            db?.execSQL(renameOldMyMovieDataTable)
            db?.execSQL(newMyMovieDataTable)
            db?.execSQL(copyData)
            db?.execSQL(dropOldTable)
        }

        if (oldVersion < 5) {
            val renameOldMyMovieDataTable = """
            ALTER TABLE my_movie_data 
            RENAME TO old_my_movie_data;
            """.trimIndent()

            val newMyMovieDataTable = """
            CREATE TABLE my_movie_data (
                ImdbId VARCHAR(16) PRIMARY KEY,
                Stars INTEGER NOT NULL CHECK (Stars >= 0 AND Stars <= 5) DEFAULT 0,
                Heart VARCHAR(27) DEFAULT NULL,
                PlanAddDate VARCHAR(27) DEFAULT NULL,
                HistoryAddDate VARCHAR(27) DEFAULT NULL,
                FOREIGN KEY (ImdbId) REFERENCES movie(ImdbId)                
            )
        """.trimIndent()

            val copyData = """
            INSERT INTO my_movie_data (
                ImdbId,
                Stars,
                Heart,
                PlanAddDate,
                HistoryAddDate
            )                
            SELECT
                ImdbId,
                Stars,
                PlanAddDate,
                PlanAddDate,
                HistoryAddDate
            FROM old_my_movie_data;
            """.trimIndent()

            val dropOldTable = """
                DROP TABLE old_my_movie_data;
            """.trimIndent()

            db?.execSQL(renameOldMyMovieDataTable)
            db?.execSQL(newMyMovieDataTable)
            db?.execSQL(copyData)
            db?.execSQL(dropOldTable)
        }
    }
    // endregion createAndUpdate

    private fun insertRawMovie(rawMovieData: RawMovieData) {
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

        rawMovieData.Ratings.let { ratings ->
            for (rating in ratings) {
                rating.let {
                    val ratingValues = ContentValues()

                    ratingValues.put("ImdbId", rawMovieData.imdbID)
                    ratingValues.put("Source", rating.Source)
                    ratingValues.put("Source", rating.Value)

                    database.insert("rating", null, ratingValues)
                }
            }
        }
    }

    private fun getRawMovieData(imdbId: String): RawMovieData? {
        val movieQuery = """
            SELECT
                $RAWMOVIEDATA_FIELDS
            FROM movie
            WHERE ImdbId = "$imdbId";
        """.trimIndent()

        val movieResult = this.writableDatabase.rawQuery(movieQuery, null)
        if (movieResult.moveToNext()) {
            val rawMovieData = extractRawMovieData(movieResult)
            rawMovieData.imdbID?.let { imdbId ->
                rawMovieData.Ratings = getRatings(imdbId)
            }
            return rawMovieData
        }
        movieResult.close()
        return null
    }

    private fun getRatings(imdbId: String): MutableList<Rating> {
        val ratingsQuery = """
                SELECT
                    $RATING_FIELDS
                FROM rating
                WHERE ImdbId = "$imdbId";
            """.trimIndent()

        val ratingsResult = this.writableDatabase.rawQuery(ratingsQuery, null)
        val ratings = ArrayList<Rating>()
        while (ratingsResult.moveToNext()) {
            val rating = extractRating(ratingsResult)
            ratings.add(rating)
        }
        ratingsResult.close()
        return ratings
    }

    fun insertMyMovieData(
        imdbId: String,
        myMovieData: MyMovieData,
    ) {
        val database = this.writableDatabase
        val myMovieDataValues = ContentValues()

        myMovieDataValues.put("ImdbId", imdbId)
        myMovieDataValues.put("Stars", myMovieData.stars)
        myMovieData.heart?.let { heart ->
            myMovieDataValues.put("PlanAddDate", dateToString(heart))
        }
        myMovieData.planList?.let { planList ->
            myMovieDataValues.put("PlanAddDate", dateToString(planList))
        }
        myMovieData.historyList?.let { watchList ->
            myMovieDataValues.put("HistoryAddDate", dateToString(watchList))
        }

        database.insert("my_movie_data", null, myMovieDataValues)
    }

    fun getMyMovieData(imdbId: String): MyMovieData? {
        val myRatingQuery = """
            SELECT
                $MYMOVIEDATA_FIELDS
            FROM my_movie_data
            WHERE ImdbId = "$imdbId";
        """.trimIndent()

        val myMovieDataResult = this.writableDatabase.rawQuery(myRatingQuery, null)

        if (myMovieDataResult.moveToNext()) {
            return extractMyMovieData(myMovieDataResult)
        }
        myMovieDataResult.close()
        return null
    }

    // region setMyMovieData
    fun setPlan(
        addDate: Calendar?,
        imdbId: String,
    ) {
        var update = ""

        addDate?.also { addDate ->
            val calendarString = dateToString(addDate)
            update = """
            UPDATE my_movie_data
            SET PlanAddDate = "$calendarString"
            WHERE ImdbId = "$imdbId";
        """.trimIndent()
        } ?: run {
            update = """
            UPDATE my_movie_data
            SET PlanAddDate = NULL
            WHERE ImdbId = "$imdbId";
            """.trimIndent()
        }

        this.writableDatabase.execSQL(update)
    }

    fun setHistory(
        addDate: Calendar?,
        imdbId: String,
    ) {
        var update = ""

        addDate?.also { addDate ->
            val calendarString = dateToString(addDate)
            update = """
            UPDATE my_movie_data
            SET HistoryAddDate = "$calendarString"
            WHERE ImdbId = "$imdbId";
        """.trimIndent()
        } ?: run {
            update = """
            UPDATE my_movie_data
            SET HistoryAddDate = NULL
            WHERE ImdbId = "$imdbId";
            """.trimIndent()
        }

        this.writableDatabase.execSQL(update)
    }

    fun setRating(
        stars: Int,
        imdbId: String,
    ) {
        var update = ""

        stars?.also { stars ->
            update = """
            UPDATE my_movie_data
            SET Stars = $stars
            WHERE ImdbId = "$imdbId";
        """.trimIndent()
        }

        this.writableDatabase.execSQL(update)
    }

    fun setHeart(
        heart: Calendar?,
        imdbId: String,
    ) {
        var update = ""

        heart?.also { heart ->
            val calendarString = dateToString(heart)
            update = """
            UPDATE my_movie_data
            SET Heart = "$calendarString"
            WHERE ImdbId = "$imdbId";
        """.trimIndent()
        } ?: run {
            update = """
            UPDATE my_movie_data
            SET Heart = NULL
            WHERE ImdbId = "$imdbId";
            """.trimIndent()
        }

        this.writableDatabase.execSQL(update)
    }
    // endregion setMyMovieData

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
        return getRawMovieData(imdbId)?.let { rawMovieData ->
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

    // region searchMovies
    fun getMoviesInPlan(): MutableList<MovieData> {
        return getMovies("WHERE PlanAddDate NOT NULL")
    }

    fun getMoviesInHistory(): MutableList<MovieData> {
        return getMovies("WHERE HistoryAddDate NOT NULL")
    }

    /**
     * All movies with a heart
     */
    fun getMoviesHearted(): MutableList<MovieData> {
        return getMovies("WHERE Heart NOT NULL")
    }

    /**
     * All movies without a heart
     */
    fun getMoviesNotHearted(): MutableList<MovieData> {
        return getMovies("WHERE Heart IS NULL")
    }

    /**
     * All movies with more than this amount of stars
     */
    fun getMoviesMinStars(stars: Int): MutableList<MovieData> {
        return getMovies("WHERE Stars > $stars")
    }

    /**
     * All movies with fewer than this amount of stars
     */
    fun getMoviesMaxStars(stars: Int): MutableList<MovieData> {
        return getMovies("WHERE Stars < $stars")
    }

    /**
     * All movies with this amount of stars
     */
    fun getMoviesWithStars(stars: Int): MutableList<MovieData> {
        return getMovies("WHERE Stars = $stars")
    }

    fun getMoviesWithName(name: String): MutableList<MovieData> {
        val conditionString = StringBuilder()
        conditionString.append("WHERE 1 = 1\n")
        for (nameBit in name.split(" ")) {
            conditionString.append("AND Title LIKE \"%$nameBit%\"\n")
        }
        return getMovies(conditionString.toString())
    }

    private fun getMovies(conditions: String): MutableList<MovieData> {
        val query = """
            SELECT
                $RAWMOVIEDATA_FIELDS,
                $MYMOVIEDATA_FIELDS
            FROM 
                my_movie_data
                INNER JOIN movie USING(ImdbId)
            $conditions;
        """.trimIndent()

        val results = this.writableDatabase.rawQuery(query, null)

        val movies = ArrayList<MovieData>()
        while (results.moveToNext()) {
            val rawMovieData = extractRawMovieData(results)
            rawMovieData.imdbID?.let { imdbId ->
                rawMovieData.Ratings = getRatings(imdbId)
                val myMovieData = extractMyMovieData(results)
                movies.add(
                    MovieData(
                        rawMovieData,
                        myMovieData
                    )
                )
            }
        }
        results.close()
        return movies
    }
    // endregion searchMovies

    // region extractMethods
    private fun extractRawMovieData(cursor: Cursor): RawMovieData {
        return RawMovieData(
            cursor.getStringOrNull(cursor.getColumnIndex("Actors")),
            cursor.getStringOrNull(cursor.getColumnIndex("Awards")),
            cursor.getStringOrNull(cursor.getColumnIndex("BoxOffice")),
            cursor.getStringOrNull(cursor.getColumnIndex("Country")),
            cursor.getStringOrNull(cursor.getColumnIndex("Dvd")),
            cursor.getStringOrNull(cursor.getColumnIndex("Director")),
            cursor.getStringOrNull(cursor.getColumnIndex("Genre")),
            cursor.getStringOrNull(cursor.getColumnIndex("Language")),
            cursor.getStringOrNull(cursor.getColumnIndex("Metascore")),
            cursor.getStringOrNull(cursor.getColumnIndex("Plot")),
            cursor.getStringOrNull(cursor.getColumnIndex("Poster")),
            cursor.getStringOrNull(cursor.getColumnIndex("Production")),
            cursor.getStringOrNull(cursor.getColumnIndex("Rated")),
            ArrayList<Rating>(),
            cursor.getStringOrNull(cursor.getColumnIndex("Released")),
            cursor.getStringOrNull(cursor.getColumnIndex("Response")),
            cursor.getStringOrNull(cursor.getColumnIndex("Runtime")),
            cursor.getStringOrNull(cursor.getColumnIndex("Title")),
            cursor.getStringOrNull(cursor.getColumnIndex("Type")),
            cursor.getStringOrNull(cursor.getColumnIndex("Website")),
            cursor.getStringOrNull(cursor.getColumnIndex("Writer")),
            cursor.getStringOrNull(cursor.getColumnIndex("Year")),
            cursor.getStringOrNull(cursor.getColumnIndex("ImdbId")),
            cursor.getStringOrNull(cursor.getColumnIndex("ImdbRating")),
            cursor.getStringOrNull(cursor.getColumnIndex("ImdbVotes")),
        )
    }

    private fun extractRating(cursor: Cursor): Rating {
        return Rating(
            cursor.getStringOrNull(cursor.getColumnIndex("Source")),
            cursor.getStringOrNull(cursor.getColumnIndex("Value"))
        )
    }

    private fun extractMyMovieData(cursor: Cursor): MyMovieData {
        return MyMovieData(
            cursor.getIntOrNull(cursor.getColumnIndex("Stars")) ?: 0,
            cursor.getStringOrNull(cursor.getColumnIndex("Heart"))?.let { string -> stringToDate(string) },
            cursor.getStringOrNull(cursor.getColumnIndex("PlanAddDate"))?.let { string -> stringToDate(string) },
            cursor.getStringOrNull(cursor.getColumnIndex("HistoryAddDate"))?.let { string -> stringToDate(string) }
        )
    }
    // endregion extractMethods
    // implement search queries
}