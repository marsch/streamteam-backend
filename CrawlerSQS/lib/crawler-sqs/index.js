var CrawlerFactory = module.exports = {};

CrawlerFactory.createStatusSampleCrawler = function(twitterUser, twitterPassword) {
    return require('./crawler-status-sample').create(twitterUser, twitterPassword);
};
