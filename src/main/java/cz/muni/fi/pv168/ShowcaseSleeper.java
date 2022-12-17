package cz.muni.fi.pv168;

public class ShowcaseSleeper {

    public static void doShortSleep() {
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            // DO NOTHING
        }
    }

    public static void doLongSleep() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            // DO NOTHING
        }
    }
}
