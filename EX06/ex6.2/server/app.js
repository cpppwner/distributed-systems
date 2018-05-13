'use strict';

process.title = 'ChatServer';

const webSocketServer = require('websocket').server;
const http = require('http');
const Chat = require('./chat');

const webSocketsServerPort = 1337;

/**
 * HTTP server
 */
const server = http.createServer(function(request, response) {
    // Not important for us. We're writing WebSocket server,
    // not HTTP server
});
server.listen(webSocketsServerPort, function() {
    console.log('Server is listening on port ' + webSocketsServerPort);
});

/**
 * WebSocket server
 */
const wsServer = new webSocketServer({
    // WebSocket server is tied to a HTTP server. WebSocket
    // request is just an enhanced HTTP request. For more info 
    // http://tools.ietf.org/html/rfc6455#page-6
    httpServer: server
});


// This callback function is called every time someone
// tries to connect to the WebSocket server
wsServer.on('request', function(request) {
    console.log(' Connection from origin ' + request.origin + '.');
    // accept connection
    // 'request.origin' should be checked to make sure that client is connecting from your website
    // (http://en.wikipedia.org/wiki/Same_origin_policy)
    var connection = request.accept(null, request.origin);
    var clientId = Chat.onConnectionAccepted(connection);
    console.log(' Connection accepted from ' + request.origin + '.');

    connection.on('message', function(message) {
        Chat.onMessageReceived(clientId, message);
    });

      // user disconnected
    connection.on('close', function(connection) {
        console.log('oncloseevent');
        Chat.onClientDisconnected(clientId);
    });
});
