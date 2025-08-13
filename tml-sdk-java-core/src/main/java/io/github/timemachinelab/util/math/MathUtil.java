package io.github.timemachinelab.util.math;

import io.github.timemachinelab.util.Beta;

@Beta
public class MathUtil {

    /**
     * Find the mid-value between the l and r boundaries
     * @param l left
     * @param r right
     * @return mid-value
     */
    public static int mid(int l, int r){
        return l + ((r - l)>>1);
    }
}
