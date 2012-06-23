function CrawlerStatusSample(twitterUser, twitterPassword) {
    this.username = twitterUser;
    this.password = twitterPassword;
}

CrawlerStatusSample.prototype.crawl = function(cb) {
    // crawl and send tweets to callback
    var https = require('https');
    
    https.request({
        host: 'stream.twitter.com',
        path: '/1/statuses/sample.json',
        method: 'GET',
        port: 443,
        headers: {
            'Authorization': 'Basic ' + (new Buffer(this.username + ':' + this.password, 'utf8')).toString('base64'),
        },
    }, function(res) {
        res.on('data', function(chunk) {
            var oneTweet;
            
            try {
                oneTweet = JSON.parse(chunk.toString('utf8'));
            } catch(e) {
                console.log(e);
                return;
            }
            
            if ('undefined' === typeof oneTweet.text)
                return;
            
            cb(null, {
                text: oneTweet.text
            });
        });
    }).on('error', function(e) {
        // return the error
        cb(e);
    }).end();
};

module.exports = {
    create: function(twitterUser, twitterPassword) {
        return new CrawlerStatusSample(twitterUser, twitterPassword);
    }
};
