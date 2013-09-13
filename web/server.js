var http = require('http');
var express = require('express');
var app = express();

http.createServer(app).listen(8080);
console.log("server running");

//server stuff
app.configure(function() {
	app.use(express.bodyParser());
	app.use(app.router);
	app.use(express.static(__dirname +'/views'));
	app.use(function(err, req, res, next){
		console.error(err.stack);
		res.send(500, 'Something broke!');
	});
});

app.get("/", function(req, res) {
	res.sendfile('./views/quickstart.html');
});