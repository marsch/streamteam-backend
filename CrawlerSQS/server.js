
var config = require('./config.json');

var crawler = require('./').createStatusSampleCrawler(config.twitter.username, config.twitter.password);
var aws = require ('aws-lib');

var awsKey = config.aws.key;
var awsPrivateKey = config.aws.privateKey;

sqs = aws.createSQSClient(awsKey, awsPrivateKey, {
    path: config.sqs.path
});

// sqs.call ( "ListQueues", {}, function (result) { 
//     console.log("ListQueues result: " + JSON.stringify(result)); 
// });

var pendingCount = 0;
var pending = {};

function sendTweetToQueue(tweet) {
    ++pendingCount;
    pending['SendMessageBatchRequestEntry.' + pendingCount + '.Id'] = 'P' + pendingCount;
    pending['SendMessageBatchRequestEntry.' + pendingCount + '.MessageBody'] = JSON.stringify(tweet);
    
    if (pendingCount >= 20) {
        sqs.call ( "SendMessageBatch", pending, function (result ) {
            console.log(result);
        });
        
        pendingCount = 0;
        pending = {};
    }
}

crawler.crawl(function(err, tweet) {
    if (err) {
        console.log(err);
    } else {
        sqs.call ( "SendMessage", { MessageBody: JSON.stringify(tweet) }, function (result ) {
        });
        // sendTweetToQueue(tweet);
    }
});
