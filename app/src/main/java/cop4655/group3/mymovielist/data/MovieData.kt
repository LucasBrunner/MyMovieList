package cop4655.group3.mymovielist.data

class MovieData (
    rawMovieData: RawMovieData,
    myMovieData: MyMovieData = MyMovieData()
) {
    var rawMovieData: RawMovieData = rawMovieData
    val myMovieData: MyMovieData = MyMovieData()
    var hasFullData: Boolean = false

    init {

    }
}
