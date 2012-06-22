# Tweet crawler

## Usage

Run the crawler with:

    $ node tweet_crawler.js twitter.user twitter.pass processor.host processor.port

For example:

    $ node tweet_crawler.js bieberfan94 justin<3 10.2.33.0 3000

If you run it in the background, you can get status information using:

    $ node tweet_crawler.js bieberfan94 justin<3 10.2.33.0 3000 &
    $ export CRAWLER_PID=$!
    $ kill -SIGUSR1 $CRAWLER_PID
    Total bytes send to processor: 1244, kB/s: ~2.9457

Be sure to start the processor first. The crawler will terminate once the processor doesn't react.
So make sure to either monitor the crawler or keep the processor up.
