const certificate = require('selfsigned');
const keypair = require('keypair');
const fs = require('fs');
const mkdirp = require('mkdirp')

var CertificateCreator = {};

CertificateCreator.attrs = [{
    name: 'commonName',
    value: 'to_be_replace_with_appname'
}, {
    name: 'countryName',
    value: 'GB'
}, {
    shortName: 'ST',
    value: 'GB'
}, {
    name: 'localityName',
    value: 'London'
}, {
    name: 'organizationName',
    value: 'Symphony Development Certificate'
}, {
    shortName: 'OU',
    value: 'FOR TEST USE ONLY'
}]

CertificateCreator.create = (botUsername, path) => {

    /* Change the CName with the application Name */
    let attrs = CertificateCreator.attrs.slice(0);
    attrs[0].value = botUsername;

    certificate.generate(attrs, {
        keySize: 2048, // the size for the private key in bits (default: 1024)
        days: 365, // how long till expiry of the signed certificate (default: 365)
        algorithm: 'sha256', // sign the certificate with specified algorithm (default: 'sha1')
        extensions: [{
            name: 'basicConstraints',
            cA: true
        }, {
            name: 'keyUsage',
            keyCertSign: true,
            digitalSignature: true,
            nonRepudiation: true,
            keyEncipherment: true,
            dataEncipherment: true
        }], // certificate extensions array
        pkcs7: true, // include PKCS#7 as part of the output (default: false)
        clientCertificate: true, // generate client cert signed by the original key (default: false)
        clientCertificateCN: botUsername // client certificate's common name
    }, function (err, pems) {
        fs.writeFile(path + "/" + botUsername + ".pem", pems.clientcert + "\n" +
            pems.clientprivate + "\n" + pems.clientpublic + "\n" + pems.clientpkcs7 + "\n" +
            pems.cert + "\n" + pems.fingerprint + "\n" + pems.private + "\n" + pems.public, function (err) {
            if (err) {
                return console.log(err);
            }

        });
        fs.writeFile(path + "/" + "ca-cer-" + botUsername + ".cer", pems.cert, function (err) {
            if (err) {
                return console.log(err);
            }
        });
    });

};

CertificateCreator.createRSA = (botUsername, path) => {
    mkdirp.sync(path)

    let pair = keypair({"bits": 4096});

    fs.writeFile(path + "/" + "rsa-public-" + botUsername + ".pem", pair.public, function (err) {
        if (err) {
            return console.log(err);
        }
    });
    fs.writeFile(path + "/" + "rsa-private-" + botUsername + ".pem", pair.private, function (err) {
        if (err) {
            return console.log(err);
        }
    });
}

module.exports = CertificateCreator;
