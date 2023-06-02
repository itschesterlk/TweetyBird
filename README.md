# Welcome to TweetyBird for FTC!

## A little note...
Currently TweetyBird is still in heavy development and will be slow until next FTC season.
Expect alot of accuracy issues and bugs in the mean time.
Alot of features still need to me implemented before this can be reliably used in a competition.
At this point there are prone to be extreemly dramatic changes so often check back on this GitHub
repository for the lastest updates and features.

Contributions of ANY KIND are EXTREEMLY WELCOME.
If you prefer to contact me, send me an email at contact@chesterlk.com


## So what is this so called TweetyBird?
TweetyBird is a set of java files for your FTC sdk that controls your meccanum drive train for you.

All you need to do is type a simple commant such as 'tweetybird.moveTo(20,0,0)' and TweetyBird will
repeat these following steps on its own: Where it is, Where it needs to be, How to get there, Execute.
Even if your robot is shoved 5 feet of course, it will return to where it was and continue.

The best part, TweetyBird runs the ENTIRE time your opmode is active, meaning that even when the robot reaches
its destination, if its shoved or hit, it will correct itself to where it was.

But if its always running, how will my code run?
TweetyBird runs multiple threads meaning that it runs multiple lines of code at a time, as soon as you send instructions
to TweetyBird, they enter a queue and your code continues running while TweetyBird starts going though the queue,
if you want the code to wait until the robot reaches its destination, we created a method called isBusy which will
return weather the robot is still running through queue or not.

You might be thinking, "Well, if it does all of this, then surely configuring TweetyBird for my robot will take forever."
This is something we took into consideration every part of development, all you need to do is run though our config file and
change a couple measurements of your deadwheel encoders to reflect your bot. Optionally the config file also contains other
parameters that affect how TweetyBird will reach its destination.
