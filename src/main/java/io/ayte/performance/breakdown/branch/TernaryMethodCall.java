package io.ayte.performance.breakdown.branch;

/**
 * The question is: is the following method
 *
 * () {
 *     return volatileFlag ? constantA : constantB;
 * }
 *
 * be compiled just as () {
 *     return volatileFlag ? getConstantA() : getConstantB();
 * }
 *
 * long story short, assembly is identical except for addresses.
 */
public class TernaryMethodCall {
    private static final String LEFT = "left";
    private static final String RIGHT = "right";

    private long start;
    private boolean flag;
    // get off me, false sharing
    private long p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16;
    private String state;

    public static void main(String[] args) {
        TernaryMethodCall instance = new TernaryMethodCall();
        instance.start = System.currentTimeMillis();
        // 5 min
        while (instance.getRunningTime() < 300_000) {
            for (int i = 0; i < 1_000; i++) {
                for (int j = 0; j < 1_000; j++) {
                    instance.state = instance.directMethod();
                }
                for (int j = 0; j < 1_000; j++) {
                    instance.state = instance.nestedMethod();
                }
                instance.flag = !instance.flag;
            }
        }
    }

    private String nestedMethod() {
        return flag ? LEFT : RIGHT;
    }

    public static String getLeft() {
        return LEFT;
    }

    public static String getRight() {
        return RIGHT;
    }

    private String directMethod() {
        return flag ? LEFT : RIGHT;
    }

    private long getRunningTime() {
        return System.currentTimeMillis() - start;
    }
}
