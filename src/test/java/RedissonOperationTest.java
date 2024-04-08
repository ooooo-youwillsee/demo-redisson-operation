import cn.hutool.core.thread.ThreadUtil;
import org.apache.logging.log4j.message.AsynchronouslyFormattable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class RedissonOperationTest {

    private static RedissonClient redissonClient;

    @BeforeAll
    static void beforeAll() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        redissonClient = Redisson.create(config);
    }

    @AfterAll
    static void afterAll() {
        redissonClient.shutdown();
    }


    @Test
    void testDistributedLock() {
        RLock lock = redissonClient.getLock("lock");
        try {
            lock.lock();
            ThreadUtil.sleep(30, TimeUnit.SECONDS);
            System.out.println("xxx");
        } finally {
            lock.unlock();
        }
    }

    @Test
    void testSpinLock() {
        RLock lock = redissonClient.getSpinLock("lock");
        try {
            lock.lock();
            ThreadUtil.sleep(30, TimeUnit.SECONDS);
            System.out.println("xxx");
        } finally {
            lock.unlock();
        }
    }

    @Test
    void testMultiLock() {
        RLock lock1 = redissonClient.getLock("lock1");
        RLock lock2 = redissonClient.getLock("lock2");
        RLock lock = redissonClient.getMultiLock(lock1, lock2);
        try {
            lock.lock();
            ThreadUtil.sleep(30, TimeUnit.SECONDS);
            System.out.println("xxx");
        } finally {
            lock.unlock();
        }
    }

    @Test
    void testOrderedSet() {
        RSortedSet<String> set = redissonClient.getSortedSet("set");
        set.add("3");
        set.add("2");
        set.add("1");
        for (String s : set.readAll()) {
            System.out.println(s);
        }
    }
}
