
# EventBus test case
Main idea of this small app is to demonstrate issue with events delivery in [EventBus](http://greenrobot.org/eventbus/) library.

App appearance is pretty simple it just a RecyclerView and Button to start the test and demonstrate the results. Some additional info is prinnted into the log.

There are three type of events:
* StartEvent - starts real test
* AsyncEvent - event to start new separate thread (handler is defined as [ASYNC](http://greenrobot.org/eventbus/documentation/delivery-threads-threadmode/)) and call post
* [DataEvent](https://github.com/zepp/EventBusTestCase/blob/master/app/src/main/java/com/zeppa/eventbustestcase/events/DataEvent.java) - event that is posted from AsyncEvent handler

DataEvent is simple POJO that has following fields:
* revision - revision or sequence number of the data
* threadId - ID of the thread that posted the event
* timestamp - approximate time when object construction is finished in nanoseconds
DataEvent is constructed using factory method (newInstance) that generates new revision number and passes one to real private constructor. It is synchronized so there is garantee that new instance is older then privious one.
I assume that EventBus post method is called at the moment that is very close to the object constuction time.

Issue:
DataEvent handler is defined as [MAIN_ORDERED](http://greenrobot.org/eventbus/documentation/delivery-threads-threadmode/) so I expect that events are delivered in the same order as they were posted.

Actualy I observe this:
```
03-22 16:35:04.469 D/MainActivity: DataEvent{revision=1, threadId=329, timestamp=3413354623420}
03-22 16:35:04.470 D/MainActivity: DataEvent{revision=2, threadId=328, timestamp=3413354659780}
03-22 16:35:04.470 D/MainActivity: DataEvent{revision=3, threadId=330, timestamp=3413355153750}
03-22 16:35:04.470 D/MainActivity: DataEvent{revision=4, threadId=330, timestamp=3413356397090}
03-22 16:35:04.470 D/MainActivity: DataEvent{revision=5, threadId=330, timestamp=3413356485430}
03-22 16:35:04.470 D/MainActivity: DataEvent{revision=6, threadId=329, timestamp=3413356629960}
03-22 16:35:04.470 D/MainActivity: DataEvent{revision=7, threadId=328, timestamp=3413356675330}
03-22 16:35:04.470 D/MainActivity: DataEvent{revision=10, threadId=328, timestamp=3413361416440}
03-22 16:35:04.470 D/MainActivity: DataEvent{revision=11, threadId=329, timestamp=3413361566670}
03-22 16:35:04.470 D/MainActivity: DataEvent{revision=12, threadId=330, timestamp=3413361625260}
03-22 16:35:04.470 D/MainActivity: DataEvent{revision=9, threadId=332, timestamp=3413360934180}
03-22 16:35:04.470 D/MainActivity: DataEvent{revision=8, threadId=331, timestamp=3413360210050}
03-22 16:35:04.470 D/MainActivity: DataEvent{revision=13, threadId=333, timestamp=3413363575310}
03-22 16:35:04.470 D/MainActivity: DataEvent{revision=14, threadId=333, timestamp=3413363777830}
03-22 16:35:04.470 D/MainActivity: DataEvent{revision=15, threadId=333, timestamp=3413363870070}
03-22 16:35:04.470 D/MainActivity: DataEvent{revision=16, threadId=330, timestamp=3413363907330}
03-22 16:35:04.470 D/MainActivity: DataEvent{revision=17, threadId=329, timestamp=3413363943990}
03-22 16:35:04.470 D/MainActivity: DataEvent{revision=18, threadId=332, timestamp=3413364029920}
03-22 16:35:04.470 D/MainActivity: DataEvent{revision=19, threadId=328, timestamp=3413364126370}
03-22 16:35:04.470 D/MainActivity: DataEvent{revision=20, threadId=333, timestamp=3413364179860}
```

As you can see delivery order is incorrect.
