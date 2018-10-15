package ro.cucumber.core.poller;

import org.junit.Ignore;
import org.junit.Test;
import ro.cucumber.core.engineering.poller.MethodPoller;

import java.time.Duration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MethodPollerTest {

    @Test
    @Ignore
    public void testPollerForResult() {
        int expected = 3;
        int result = new MethodPoller<Integer>()
                .method(() -> generateRandomNumber(4))
                .duration(Duration.ofSeconds(10), 100)
                .until(n -> n.equals(expected)).poll();
        assertEquals(expected, result);
    }

    @Test
    @Ignore
    public void testPollerTimeout() {
        int expected = 5;
        int result = new MethodPoller<Integer>()
                .method(() -> generateRandomNumber(4))
                .duration(Duration.ofSeconds(2), 100)
                .until(n -> n.equals(expected)).poll();
        assertNotEquals(expected, result);
    }

    @Test
    @Ignore
    public void testPollerWithDurationTimeout() {
        int expected = 5;
        int result = new MethodPoller<Integer>()
                .method(() -> generateRandomNumber(4))
                .duration(2)
                .until(n -> n.equals(expected)).poll();
        assertNotEquals(expected, result);
    }

    private int generateRandomNumber(int maxLimit) {
        return (int) (Math.random() * maxLimit);
    }
}
