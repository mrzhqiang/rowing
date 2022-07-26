package com.github.mrzhqiang.rowing.core.exception;

import org.junit.Assert;
import org.junit.Test;

public class MathTest {

    @Test
    public void floorMod() {
        int n = Math.floorMod(-1000037421, 10000);
        Assert.assertEquals(2579, n);
        int a = Math.floorMod(1000037421, 10000);
        Assert.assertEquals(7421, a);
    }
}
