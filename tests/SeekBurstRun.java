package sa.urtv.home.tilawah;

/** For tests/seek_burst_test.py: SeekBurst. Prints one line per check: name TAB ok. */
public final class SeekBurstRun {
    static void check(String name, boolean ok) { System.out.println(name + "\t" + (ok ? "ok" : "FAIL")); }

    public static void main(String[] args) {
        SeekBurst b = new SeekBurst();
        check("a single press is applied at once, as before", b.offer(10_000, 1000) == 10_000 && !b.hasPending());
        check("a later single press (window over) is applied at once too", b.offer(-10_000, 1000 + SeekBurst.WINDOW_MS) == -10_000);

        b = new SeekBurst();
        int applied = b.offer(10_000, 0);
        int now = 0;
        for (int i = 1; i <= 15; i++) { now = i * 10; applied += b.offer(10_000, now); }   // 15 more in 150 ms, inside the window
        check("a burst applies only the first press at once", applied == 10_000);
        check("the rest waits for the window", b.hasPending() && b.dueIn(now) <= SeekBurst.WINDOW_MS);
        int step = b.flush(SeekBurst.WINDOW_MS);
        check("the flush is one step: the sum of the pending presses, capped", step > 0 && step <= SeekBurst.MAX_STEP_MS);
        check("nothing is left after a flush", !b.hasPending() && b.flush(now + 60) == 0);

        b = new SeekBurst();
        b.offer(10_000, 0);
        for (int i = 0; i < 5000; i++) b.offer(10_000, 10);
        check("a flood is capped", b.flush(100) == SeekBurst.MAX_STEP_MS);

        b = new SeekBurst();
        b.offer(10_000, 0);
        b.offer(10_000, 50); b.offer(-10_000, 100);
        check("equal and opposite presses leave nothing to do", b.flush(150) == 0);

        b = new SeekBurst();
        b.offer(10_000, 0);
        b.offer(10_000, 10);
        b.reset();
        check("reset drops what waited and the next press is a first press", !b.hasPending() && b.offer(5_000, 20) == 5_000);

        check("zero delta does nothing", new SeekBurst().offer(0, 0) == 0);
        check("a clock that went backwards does not hold a press back", b.offer(1_000, 0) == 1_000);

        check("target clamps at the start", SeekBurst.target(3_000, -10_000, 100_000) == 0);
        check("target clamps at the duration", SeekBurst.target(95_000, 10_000, 100_000) == 100_000);
        check("target with no known duration only has the floor", SeekBurst.target(95_000, 10_000, 0) == 105_000);
        check("target does not overflow", SeekBurst.target(Integer.MAX_VALUE, Integer.MAX_VALUE, 0) > 0);
    }
}
