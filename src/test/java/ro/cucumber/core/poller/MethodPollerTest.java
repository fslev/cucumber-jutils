package ro.cucumber.core.poller;

import org.junit.Ignore;
import org.junit.Test;
import ro.cucumber.core.engineering.poller.MethodPoller;

import java.time.Duration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@Ignore
public class MethodPollerTest {

    @Test
    public void testPollerForResult() {
        int expected = 3;
        int result = new MethodPoller<Integer>()
                .method(() -> generateRandomNumber(4))
                .duration(Duration.ofSeconds(10), 100L)
                .until(n -> n.equals(expected)).poll();
        assertEquals(expected, result);
    }

    @Test
    public void testPollerTimeout() {
        int expected = 5;
        int result = new MethodPoller<Integer>()
                .method(() -> generateRandomNumber(4))
                .duration(Duration.ofSeconds(2), 100L)
                .until(n -> n.equals(expected)).poll();
        assertNotEquals(expected, result);
    }

    @Test
    public void testPollerDurationTimeout() {
        int expected = 5;
        int result = new MethodPoller<Integer>()
                .method(() -> generateRandomNumber(4))
                .duration(2)
                .until(n -> n.equals(expected)).poll();
        assertNotEquals(expected, result);
    }

    @Test
    public void testPollerInterval() {
        int expected = 5;
        int result = new MethodPoller<Integer>()
                .method(() -> generateRandomNumber(4))
                .duration(500L)
                .until(n -> n.equals(expected)).poll();
        assertNotEquals(expected, result);
    }

    private int generateRandomNumber(int maxLimit) {
        return (int) (Math.random() * maxLimit);
    }
}
