const https = require('https');
const fs = require('fs');
const express = require('express');
const path = require('path');

const port = 443;
const app = express();

const options = {
    cert: fs.readFileSync('cert/localhost.pem'),
    key: fs.readFileSync('cert/localhost-key.pem'),
};

app.get('/hello/:msg', function (req, res) {
    req.params.msg = "Hello " + req.params.msg;
    res.setHeader("Content-Type", "application/json");
    res.send(req.params);
});

app.get('/hello', function (req, res) {
    res.setHeader("Content-Type", "application/json");
    res.send({msg: "ni imena"});
});

app.use('/', express.static(path.join('frontend/kf/dist/kf')));

https.createServer(options, app).listen(port, () => {
    console.log('Server listening on port ' + port);
});
