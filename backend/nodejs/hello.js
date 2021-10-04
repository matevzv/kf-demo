const https = require('https');
const fs = require('fs');
const express = require('express');
const path = require('path');

const app = express();

const options = {
    cert: fs.readFileSync('./cert.pem'),
    key: fs.readFileSync('./key.pem'),
};

app.get('/hello/:msg', function (req, res) {
    console.log(req.params.msg);
    req.params.msg = "Hello " + req.params.msg;
    res.send(req.params).json();
});

app.get('/hello', function (req, res) {
    res.send({msg: "ni imena"}).json();
});

app.use('/', express.static(path.join(__dirname, '../..', 'frontend/kf/dist/kf')));

const port = 3000;

https.createServer(options, app).listen(port, () => {
    console.log('Server listening on port ' + port);
});
