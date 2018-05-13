'use strict';

process.title = 'MovieDatabase';

// import Server
const RpcServer = require('node-json-rpc2').Server;
const MovieDatabase = require('./moviedb').getInstance();

// server options for configuring the server
const options = {
    protocol : 'http',
    path : '/rpc',
    host : '127.0.0.1',
    port : 1337,
    method : 'POST'
};

// create server
const server = new RpcServer(options);

// method retrieving all titles as string array
server.addMethod('titles', function(parameters, id) {
    console.log('titles method called ...')
    if (parameters.length !== 0) {
        console.log('Invalid number of arguments: Expected 0, got ' + parameters.length + ' instead.');
        return {id:id, error:{code:-32602, message:'Invalid params', data:'Too many arguments'}};
    }
    const titles = MovieDatabase.getMovieTitles();
    console.log("Retrieved titles from DB: " + JSON.stringify(titles))
    return {id:id, result:titles}
});
// method retrieving movie details for a given movie name
server.addMethod('details', function(parameters, id) {
    console.log('details method called ...')
    if (parameters.length !== 1) {
        console.log('Invalid number of arguments: Expected 1, got ' + parameters.length + ' instead.');
        return {id:id, error:{code:-32602, message:'Invalid params', data:'Invalid number of arguments'}};
    }
    const details = MovieDatabase.getMovieDetails(parameters[0]);
    console.log("Retrieved details from DB: " + JSON.stringify(details))
    return {id:id, result:details};
});
// method for renting a movie
server.addMethod('rent', function(parameters, id) {
    console.log('rent method called ...')
    if (parameters.length !== 1) {
        console.log('Invalid number of arguments: Expected 1, got ' + parameters.length + ' instead.');
        return {id:id, error:{code:-32602, message:'Invalid params', data:'Invalid number of arguments'}};
    }
    const result = MovieDatabase.rentMovie(parameters[0]);
    console.log("Renting returned: " + result)
    return {id:id, result:result};
});

console.log('Server is up & running ...');
