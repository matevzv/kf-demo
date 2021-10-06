[![CI](https://github.com/matevzv/kf-demo/actions/workflows/main.yml/badge.svg)](https://github.com/matevzv/kf-demo/actions/workflows/main.yml)

# kf-demo

## Install dependencies
    $ apt-get install openjdk-11-jdk
    $ apt-get install maven
    $ apt-get install nodejs
    $ npm install -g @angular/cli
    $ npm install -g mocha

## Using the build script
### 1. Install HTTPS certificates for localhost
    $ ./runhello install password

### 2. Build the system
    $ ./runhello build

### 3. Run nodejs or java backend on port 443
    $ ./runhello nodejs
    $ ./runhello java password

## Test the system
    $ cd backend/nodejs
    $ npm test
