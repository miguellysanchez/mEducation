var http = require('http');
var express = require('express');
var app = express();

var JSFtp = require("jsftp");
var ftp = new JSFtp({
  host: "localhost",
  port: 21, // defaults to 21
  user: "smart", // defaults to "anonymous"
  pass: "password" // defaults to "@anonymous"
});

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

app.get("/", function(req, res) {//displays all the files in sample folder of remote computer
  ftp.ls("./sample", function(err, res2) {
    var json_string = "{";
    res2.forEach(function(file) {
      json_string+='"file" : "' + file.name.toString()+'",';
    });
    json_string=json_string.substring(0,json_string.length-1);
    json_string+="}"
    res.send(JSON.parse(json_string));
  });  
});

app.get('/files/:filename?', function(req, res) {//downloads file from the sample folder of remote computer. needs the filename
  ftp.get('./sample/' + req.param("filename"), './sample/' + req.param("filename"), function(hadErr) {
    if (hadErr)
      console.error('There was an error retrieving the file.');
    else
      console.log('File copied successfully!');
  });
});

app.post("/files", function(req, res) {//uploads new file. needs buffer and the filename in destination(save as what)

});