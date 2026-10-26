package sa.urtv.home.tilawah;

/**
 * A held seek key sends dozens of SEEK_BY intents a second; each one must not become a MediaPlayer.seekTo (that is the stutter).
 * The first press in a quiet moment is applied at once, as it always was. Presses inside the next WINDOW_MS are added up and
 * applied as ONE step when the window ends. The sum is capped, so a flood cannot run far past what the screen can show.
 * No Android in it (tests/SeekBurstRun.java runs it); the service owns the clock and the timer.
 */
final class SeekBurst {
    static final long WINDOW_MS = 200;
    /** the most one coalesced step moves, in ms either way */
    static final int MAX_STEP_MS = 120_000;

    private long lastApply = Long.MIN_VALUE;
    private int pending;

    /** A press at `now` (ms, a monotonic clock): the delta to apply right now, or 0 when it was added to the pending step. */
    int offer(int delta, long now) {
        if (delta == 0) return 0;
        if (lastApply == Long.MIN_VALUE || now - lastApply >= WINDOW_MS || now < lastApply) {
            lastApply = now;
            pending = 0;
            return clamp(delta);
        }
        pending = clamp(pending + delta);
        return 0;
    }

    /** True when something waits for the window's end. */
    boolean hasPending() { return pending != 0; }

    /** ms from `now` until the pending step is due (never negative). */
    long dueIn(long now) { return Math.max(0, lastApply + WINDOW_MS - now); }

    /** The window ended: the pending step, to apply now (0: nothing). Starts a new window. */
    int flush(long now) {
        int d = pending;
        pending = 0;
        if (d != 0) lastApply = now;
        return d;
    }

    /** The source changed or playback stopped: nothing pending, the next press counts as the first. */
    void reset() { pending = 0; lastApply = Long.MIN_VALUE; }

    /** `position` moved by `delta`, kept inside 0..duration (duration <= 0: not known, only the floor applies). */
    static int target(int position, int delta, int duration) {
        long t = (long) position + delta;
        if (t < 0) t = 0;
        if (duration > 0 && t > duration) t = duration;
        if (t > Integer.MAX_VALUE) t = Integer.MAX_VALUE;
        return (int) t;
    }

    private static int clamp(int v) { return Math.max(-MAX_STEP_MS, Math.min(MAX_STEP_MS, v)); }
}
