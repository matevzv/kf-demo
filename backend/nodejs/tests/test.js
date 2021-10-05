var chai = require('chai');
var chaiHttp = require('chai-http');
process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0'
process.env.NODE_TLS_ACCEPT_UNTRUSTED_CERTIFICATES_THIS_IS_INSECURE = '1'

chai.use(chaiHttp);

describe('test home page operation', function() {

  it('should load login page', function(done) {
    chai.request('https://localhost')
      .get('/')
      .end(function(err, res) {
        chai.expect(err).to.be.null;
        chai.expect(res).to.have.status(200);
        chai.expect(res).to.be.html;
        done();
    });
  });

  it('should not get name', function(done) {
    chai.request('https://localhost')
      .get('/hello')
      .end(function(err, res) {
        chai.expect(err).to.be.null;
        chai.expect(res).to.have.status(200);
        chai.expect(res).to.be.json;
        chai.expect(res.text).to.contain("ni imena");
        done();
    });
  });

  it('should get name', function(done) {
    chai.request('https://localhost')
      .get('/hello/test')
      .end(function(err, res) {
        chai.expect(err).to.be.null;
        chai.expect(res).to.have.status(200);
        chai.expect(res).to.be.json;
        chai.expect(res.text).to.contain("Hello test");
        done();
    });
  });

});

exports.chai = chai;
