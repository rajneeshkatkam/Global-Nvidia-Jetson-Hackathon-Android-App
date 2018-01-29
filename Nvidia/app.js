var express = require('express');
var fs = require('fs');
var util = require('util');
var app = express();
var http = require('http').createServer(app);
var logger = require("./utils/logger");
var io = require('socket.io')(http);
var tmpSocket = require("socket.js");
//tmpSocket.initializeSocket(io);
// Fetch the service account key JSON file contents
//var serviceAccount = require("./firebase.json");
// Initialize the app with a service account, granting admin privileges
/*admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://**********.firebaseio.com/"
});
*/
// As an admin, the app has access to read and write all data, regardless of Security Rules
//var db = admin.database();
//tmpSocket.initializeSocket(io);
//var FCM = require('fcm-node');
//var serverKey = ''; //put your server key here
//var fcm = new FCM(serverKey);

//var MongoClient = require('mongodb').MongoClient;
//var url = "mongodb://127.0.0.1:27017/mydb";
var utils;



app.get('/', function(req, res) {

    console.log('requestmade')

    res.sendfile('./index.html');
});


app.post('/plans',function(req,res){
	req.on('data', function(chunk) {

        console.log(chunk);

	});

	res.send('Successfully Sent');
});


http.listen(443,function(){
	logger.info("Signallng server is listening on port 443");
});
