var http = require('http');
var express = require('express');
var app = express();

http.createServer(app).listen(8080);
console.log("server running at 8080");

//server stuff
app.configure(function() {
	app.use(express.bodyParser());
	app.use(app.router);
	app.use(express.static(__dirname));
	app.use(function(err, req, res, next){
		console.error(err.stack);
		res.send(500, 'Something broke!');
	});
});

app.get("/a", function(req, res) {//gdocs
	res.sendfile('./views/sample_upload.html');
});

app.get("/b", function(req, res) {//gdocs
	res.sendfile('./views/all_files.html');
});

app.get("/", function(req, res) {
	
});