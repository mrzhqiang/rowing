package com.github.mrzhqiang.rowing.jre;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MathTest {

    @Test
    public void floorMod() {
        int n = Math.floorMod(-1000037421, 10000);
        Assertions.assertEquals(2579, n);
        int a = Math.floorMod(1000037421, 10000);
        Assertions.assertEquals(7421, a);
    }
}
