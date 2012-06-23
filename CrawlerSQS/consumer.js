
var aws = require ('aws-lib');
var crypto = require('crypto');
var http = require('http');

var config = require('./config.json');

var awsKey = config.aws.key;
var awsPrivateKey = config.aws.privateKey;

var cloudSearchHost = config.cloudSearch.host;
var cloudSearchDocs = config.cloudSearch.path;

sqs = aws.createSQSClient(awsKey, awsPrivateKey, {
    path: config.sqs.path
});

var wordBlackListArr = 'the,you,and,que,what,are,have,your,this,now,not,but,por,dont,was,get,her,his,their,there,los,all,when,person'.split(',');
var wordBlackList = {};

for (var i = 0; i < wordBlackListArr.length; ++i) {
    wordBlackList[ wordBlackListArr[i] ] = true;
}

function submitBatchToCloudSearch(batch) {
    console.log(batch);
    var bodyContent = JSON.stringify(batch);
    var csReq = http.request({
        host: cloudSearchHost,
        path: cloudSearchDocs,
        method: 'POST',
        port: 80,
        headers: {
            'Content-Type': 'application/json',
            'Content-Length': Buffer.byteLength(bodyContent, 'utf8')
        },
    }, function(res) {
        res.on('data', function(chunk) {
            // console.log(chunk.toString('utf8'));
        });
    });
    
    csReq.write(bodyContent);
    csReq.end();
}

var currentBatch = [];
function handleReceivedMessage(message) {
    var handle = message.ReceiptHandle;
    
    var rawTweet = JSON.parse(message.Body);
    
    var words = [];
    var tags = [];
    var mentions = [];
    
    var urls = [];
    
    var tweet = rawTweet.text.replace(/(https?|ftp):\/\/(-\.)?([^\s\/?\.#-]+\.?)+(\/[^\s]*)?/ig, function(match) {
        urls.push(match);
        return '';
    }).replace(/[^\w\s#@]+/g, '').replace(/\s+/g, ' ');
    
    var uncat = tweet.split(' ');
    
    for (var i = 0; i < uncat.length; ++i) {
        if (uncat[i].length < 3)
            continue;
        
        if (uncat[i][0] === '#') {
            tags.push(uncat[i].toLowerCase());
        } else if (uncat[i][0] === '@') {
            mentions.push(uncat[i].toLowerCase());
        } else if (!wordBlackList[ uncat[i] ]) {
            words.push(uncat[i].toLowerCase());
        }
    }
    
    if (words.length >= 1 || tags.length >= 1) {
        var md5sum = crypto.createHash('md5');
        md5sum.update(tweet);
    
        var cloudsearchDoc = {
            type: 'add',
            id: md5sum.digest('hex'),
            version: 1,
            lang: 'en',
            fields: {
                tags: tags,
                words: words,
                tweet: tweet,
                // mentions: mentions,
                // urls: urls
            }
        };
        
        currentBatch.push(cloudsearchDoc);
        if (currentBatch.length >= 10) {
            console.log('Pushing documents to CloudSearch');
            submitBatchToCloudSearch(currentBatch);
            currentBatch = [];
        }
    } else {
        // console.log(rawTweet, tags, words);
    }
    
    sqs.call ( "DeleteMessage", { ReceiptHandle: handle }, function (result) {
    });
}

function requestNewMessage() {
    // handleReceivedMessage({
    //     MessageId: 'foo',
    //     Body: JSON.stringify({
    //         text: 'http://foo.com/bar?q=dofino&sdfjn=e3+seoi awesome #found'
    //     })
    // });
    sqs.call ( "ReceiveMessage", {}, function (result) {
        if ('undefined' !== typeof result.ReceiveMessageResult.Message)
            handleReceivedMessage(result.ReceiveMessageResult.Message);
        
        requestNewMessage();
    });
}

requestNewMessage();
