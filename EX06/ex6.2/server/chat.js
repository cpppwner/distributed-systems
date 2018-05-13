'use strict';

var Chat = (function() {

    var clientIdCounter = 0;
    var clients = [];
    var history = [];

    var createMessageObject = function(from, message) {

        var message = {
            type: 'message',
            data: {
                date: new Date(),
                from: from,
                message: message
            }
        };

        return message;
    }

    // broadcast a list of all users to all authenticated clients
    var broadcastUsernames = function() {
        var usernames = clients
            .filter(client => client.username !== null)
            .map(client => client.username);
        
        var message = JSON.stringify({
            type: 'users',
            data: usernames
        });
        clients.forEach(client => {
            if (client.username !== null) {
                // client is authenticated
                client.connection.sendUTF(message);
            }
        });
    }

    // disconnect a client
    var disconnectClient = function(clientId, forcefully=true) {

        var clientIndex = clients.findIndex(client => client.clientId === clientId);
        if (clientIndex >= 0) {
            // found
            var username = clients[clientIndex].username;

            // remove the client from the list
            clients.splice(clientIndex);

            if (username !== null) {
                // if user was already authenticated
                broadcastUsernames();
                var message = createMessageObject('Server', 'User ' + username + ' has left.')
                history.push(message);
                broadcastMessage(message);
            }
        }
    }

    // check if a client is already authenticated
    var isAuthenticated = function(clientId) {
        
        var clientIndex = clients.findIndex(client => client.clientId === clientId);
        return clientIndex >= 0 && clients[clientIndex].username !== null;
    }

    var broadcastMessage = function(messageObject) {

        var message = JSON.stringify(messageObject);
        clients.forEach(client => {
            if (client.username !== null) {
                // client is authenticated
                client.connection.sendUTF(message);
            }
        });
    }

    var broadcastUserJoined = function(username, joinedDate) {
        // broadcast a list of usernames
        broadcastUsernames();
        // build up the message and broadcast the mesage
        var message = createMessageObject('Server', 'User ' + username + ' has joined.');
        history.push(message);
        broadcastMessage(message);
    }

    // called to handle user authentication request
    var handleAuthentication = function(clientId, username) {
        if (isAuthenticated(clientId)) {
            // check that client is not yet authenticated
            console.log('Client is already authenticated');
            disconnectClient(clientId);
            return;
        }

        // check that username is unique
        var usernameUnique = true;
        if (clients.findIndex(client => client.username === username) >= 0) {
            usernameUnique = false;
        }

        // send back authentication response
        var clientIndex = clients.findIndex(client => client.clientId === clientId);

        if (clientIndex >= 0) {
            // send back success/failure upon authentication request
            var obj = {
                type: 'authentication',
                data: usernameUnique && username !== 'Server'
            };
            clients[clientIndex].connection.sendUTF(JSON.stringify(obj));

            if (usernameUnique) {
                // if the username is unique, store the username for given client
                clients[clientIndex].username = username;

                // send the complete history to the newly registered client
                history.forEach(message => {
                    clients[clientIndex].connection.sendUTF(JSON.stringify(message));
                })

                // broadcast a list of usernames to all connected & authenticated clients
                var now = new Date();
                broadcastUserJoined(username, now);
            }
        }
    }

    // called when a user posts a new message
    var handleMessage = function(clientId, message) {
        if (!isAuthenticated(clientId)) {
            // check that client is not already authenticated
            console.log('Client is not yet authenticated');
            disconnectClient(clientId);
            return;
        }

        // send back authentication response
        var clientIndex = clients.findIndex(client =>  client.clientId === clientId);
        if (clientIndex >= 0) {
            var messageObject = createMessageObject(clients[clientIndex].username, message);
            history.push(messageObject);
            console.log('Broadcasting message ' + JSON.stringify(messageObject));
            broadcastMessage(messageObject);
        }
    }

    var handleUnknownType = function(clientId) {
        console.log('Received unknown message type from client ...');
        disconnectClient(clientId);
    }

    // function to be called, when a new client connection has been accepted
    var onConnectionAccepted = function(connection) {
        // store connection & meta data
        // remember the index
        var clientId = clientIdCounter++;
        clients.push({clientId: clientId, connection: connection, username: null});

        return clientId;
    }

    var onMessageReceived = function(clientId, message) {
        // called whenever the client sends a new message
        if (message.type !== 'utf8') {
            console.log('Received message is not UTF-8 ');
            disconnectClient(clientId);
            return;
        }

        // parse plain text message
        var messageObject = JSON.parse(message.utf8Data);
        if (!messageObject.hasOwnProperty('type') || !messageObject.hasOwnProperty('data')) {
            // expect message to contain type and data properties
            console.log('Received message does not contain type or data ' + connection.remoteAddress);
            disconnectClient(clientId);
            return;
        }

        console.log('Received client message: ' + JSON.stringify(message))

        // check the message type
        if (messageObject.type === 'authentication') {
            // user sends authentication request
            handleAuthentication(clientId, messageObject.data);
        } else if (messageObject.type === 'message') {
            // user sends message
            handleMessage(clientId, messageObject.data);
        } else {
            handleUnknownType(clientId);
        }
    }

    var onClientDisconnected = function(clientId) {
        disconnectClient(clientId, false);
    }

    return {
        onConnectionAccepted : onConnectionAccepted,
        onMessageReceived : onMessageReceived,
        onClientDisconnected : onClientDisconnected
    }
})();

module.exports = Chat;
