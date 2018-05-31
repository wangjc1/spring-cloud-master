import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author: wangjc
 * 2018/5/31
 */
public class CountDownTest {
    private static final CountDownLatch cd = new CountDownLatch(1);

    /*
    //主线程处于无限期等待中
    "main@1" prio=5 tid=0x1 nid=NA waiting
    java.lang.Thread.State: WAITING
	  at sun.misc.Unsafe.park(Unsafe.java:-1)
	  at java.util.concurrent.locks.LockSupport.park(LockSupport.java:186)
	  at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:834)
	  at java.util.concurrent.locks.AbstractQueuedSynchronizer.doAcquireSharedInterruptibly(AbstractQueuedSynchronizer.java:994)
	  at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireSharedInterruptibly(AbstractQueuedSynchronizer.java:1303)
	  at java.util.concurrent.CountDownLatch.await(CountDownLatch.java:236)
	  at CountDownTest.testDataBind(CountDownTest.java:26)

	 //background-preinit 线程超时等待状态
	"background-preinit@776" prio=5 tid=0xc nid=NA sleeping
     java.lang.Thread.State: TIMED_WAITING
	  at java.lang.Thread.sleep(Thread.java:-1)
	  at java.lang.Thread.sleep(Thread.java:340)
	  at java.util.concurrent.TimeUnit.sleep(TimeUnit.java:360)
	  at CountDownTest$1.run(CountDownTest.java:19)
	  at java.lang.Thread.run(Thread.java:745)
     */
    @Test
    public void testDataBind() throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cd.countDown();
            }
        }, "background-preinit").start();
        cd.await();
    }
}
