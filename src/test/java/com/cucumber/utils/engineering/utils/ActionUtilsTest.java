package com.cucumber.utils.engineering.utils;

import com.cucumber.utils.helper.ActionUtils;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ActionUtilsTest {

    @Test(expected = RuntimeException.class)
    public void testActionRepeatWithRunnable() {
        ActionUtils.retryIfThrowsUp(() -> {
            System.out.println("Do smth");
            throw new IllegalStateException("lorem ipsum");
        }, IllegalStateException.class, 5, 200);
    }

    @Test(expected = RuntimeException.class)
    public void testActionRepeatWithSupplier() {
        ActionUtils.retryIfThrowsUp(() -> {
            System.out.println("Do smth");
            return 9 / 0;
        }, ArithmeticException.class, 5, 200);
    }

    @Test(expected = ArithmeticException.class)
    public void testActionRepeatWithSupplier_negative() {
        ActionUtils.retryIfThrowsUp(() -> {
            System.out.println("Do smth");
            return 9 / 0;
        }, IllegalStateException.class, 5, 200);
    }
}
