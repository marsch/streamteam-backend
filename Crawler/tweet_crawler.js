
if (process.argv.length < 5) {
    console.log('Usage: node tweet_crawler.js TWITTER_USER TWITTER_PASS TWEETPROCESSOR_HOST TWEETPROCESSOR_PORT');
    process.exit(1);
}

// Configuration as passed over the command line
var twitterUser = {
    name: process.argv[2],
    pass: process.argv[3]
};
var tweetprocessor = {
    host: process.argv[4],
    port: process.argv[5]
};

// our two dependencies, TSL for twitter, pure HTTP for internal communication
var https = require('https');
var http = require('http');

// Query the sample-stream of Twitter
// https://stream.twitter.com/1/statuses/sample.json
var req = https.request({
    host: 'stream.twitter.com',
    path: '/1/statuses/sample.json',
    method: 'GET',
    port: 443,
    headers: {
        'Authorization': 'Basic ' + (new Buffer(twitterUser.name + ':' + twitterUser.pass, 'utf8')).toString('base64'),
    },
}, function(res) {
    var startTime = (new Date()).getTime();
    var byteCount = 0;
    var oneTweet;
    res.on('data', function(chunk) {
        try {
            oneTweet = JSON.parse(chunk.toString('utf8'));
        } catch(e) {
            console.log(e);
            return;
        }
        
        if ('undefined' === typeof oneTweet.text)
            return;
        
        byteCount += Buffer.byteLength(oneTweet.text);
        
        // send to tweet processor
        var apiReq = http.request({
            host: tweetprocessor.host,
            port: tweetprocessor.port,
            path: '/tweet',
            headers: {
                'Content-Type': 'application/json'
            },
            method: 'POST'
        });
        
        // the document we pass to the processor just contains the tweet's text
        apiReq.write(JSON.stringify({
            'text': oneTweet.text
        }));
        // aaaaand send.
        apiReq.end();
    });
    
    process.on('SIGUSR1', function () {
        var elapsed = (new Date()).getTime() - startTime;
        console.log('Total bytes send to processor: ' + byteCount + ', kB/s: ~' + (byteCount / elapsed));
    });
});
req.end();
