# ILoveZappos
Interview Challenge from Zappos!

## Key Components
- Transaction History
- Order Book
- Notification Setting

** All APIs are gathered using Retrofit

### Transaction History
This is implemented with mpAndroidChart along with 'Transaction' API from BitStamp

https://www.bitstamp.net/api/v2/transactions/btcusd/

Main challenges were working with the mpAndroidChart library. It was hard to fully understand each methods and implementation to manipulate the looks and representation of data.


### Order Book
This is implemented with a RecyclerView along with 'Order Book' API from BitStamp

https://www.bitstamp.net/api/v2/order_book/btcusd/

I have experience working with RecyclerView so the process was pretty smooth. I really wanted to make it update real-time, but did not have any time for something cool like that unfortunately.


### Notification Setting
This is implemented with Android WorkManager along with 'Hourly Ticker' API from BitStamp

https://www.bitstamp.net/api/v2/ticker_hour/btcusd/

Main challenges were working with WorkManager class. I did not seem to fully grasp the workings of this class but was able to implement it in the end.

Unfortunately, I was not able to check if the notification was working or not because I ran out of time which is not a satisfying result.

## Summary
Strength: Working on the project, I tried my best to design the layouts and views that is appealiing and user-friendly. I was able to apply my ideas and implementation easily into the code and graphical interface.

Weakness: I was not able to work with new tools such as mpAndroidChart, WorkManager, ViewModel and etc in a efficient manner. What I mean is, I had difficulty quickly picking-up these new tools and in the end ran out of time.
Where I ended up with sort of messy and unorganized looking code and application which I did not aim for. Although I am just a beginner, still Android is such a huge world and there is a lot of tools to be learned, and I think that is why
it is very important to be able to qucikly pick-up new skillsets and create beautiful projects.

## Snapshots
<img src="https://github.com/hojoung97/ILoveZappos/blob/master/images/demo.gif" width="250">
