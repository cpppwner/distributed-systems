'use strict';

var MovieDatabase = (function () {

    // sole instance of the movie database
    var instance;
    
    function init() {

        // movies
        const movies = [
            {
                'title' : 'movie1',
                'year' : '2000',
                'movieInfo' : 'This movie won 5 oscars ...',
                'numRents' : 0
            },
            {
                'title' : 'movie2',
                'year' : '2008',
                'movieInfo' : 'Another great movie ...',
                'numRents' : 2
            },
            {
                'title' : 'The Hitchhiker\'s Guide to the Galaxy',
                'year' : '2005',
                'movieInfo' : 'the answer to life the universe and everything',
                'numRents' : 42
            }
        ]

        // Get all movie titles
        const getMovieTitles = function() {
            return movies.map(movie => movie.title);
        }

        // get movie details for a single movie
        const getMovieDetails = function(movieTitle) {
            var results = movies.filter(movie => movie.title === movieTitle);
            if (results.length > 1) {
                console.error('Unique constraint violation in movie database');
            }
            return results.length === 1 ? results[0] : null;
        }

        // rent a movie
        const rentMovie = function(movieTitle) {

            for (var i in movies) {
                if (movies[i].title === movieTitle) {
                    movies[i].numRents += 1;
                    return true;
                }
            }

            return false;
        }
    
        return {
            getMovieTitles : getMovieTitles,
            getMovieDetails : getMovieDetails,
            rentMovie : rentMovie
        };
    };

    return {
        getInstance: function() {
            if (!instance) {
                instance = init();
            }
            return instance;
        }
    }
})();

module.exports = MovieDatabase;
