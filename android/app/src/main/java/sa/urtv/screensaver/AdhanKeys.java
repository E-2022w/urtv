package sa.urtv.screensaver;

/**
 * What a key does while the adhan plays: OK on the adhan bar stops it, and with «إيقاف الأذان بأي زر» any key does, once; that press is
 * consumed whole (its repeats and its release too), so it never also opens the focused app or seeks the Quran. Home and Power belong to
 * Android and are never seen here; the volume keys stay ordinary. Stopping the adhan only ends the one that is playing: the next
 * prayer's alarm is not touched. No Android state in here (KeyEvent's numbers are written out, so it runs without the SDK): tests/AdhanKeysRun.java runs it. The screensaver
 * carries the same file (its package line apart).
 */
final class AdhanKeys {
    // android.view.KeyEvent's values
    private static final int ACTION_DOWN = 0, ACTION_UP = 1;
    private static final int KEYCODE_DPAD_CENTER = 23;
    private static final int KEYCODE_ENTER = 66;
    private static final int KEYCODE_NUMPAD_ENTER = 160;
    private static final int KEYCODE_BUTTON_A = 96;
    private static final int KEYCODE_VOLUME_UP = 24;
    private static final int KEYCODE_VOLUME_DOWN = 25;
    private static final int KEYCODE_VOLUME_MUTE = 164;
    private static final int KEYCODE_POWER = 26;
    private static final int KEYCODE_SLEEP = 223;
    private static final int KEYCODE_WAKEUP = 224;
    private static final int KEYCODE_HOME = 3;
    private static final int KEYCODE_APP_SWITCH = 187;
    private static final int KEYCODE_ASSIST = 219;
    private static final int KEYCODE_VOICE_ASSIST = 231;
    private static final int KEYCODE_SETTINGS = 176;
    private static final int KEYCODE_UNKNOWN = 0;

    /** the key goes on as it would have */
    static final int PASS = 0;
    /** stop the adhan, and the key is consumed */
    static final int STOP = 1;
    /** the key is consumed (the rest of a press that stopped the adhan) */
    static final int EAT = 2;

    /** the key whose press stopped the adhan, until its release */
    private int swallowed = KEYCODE_UNKNOWN;

    /**
     * @param barShowing the adhan bar is on screen (OK on it stops the adhan)
     * @param barFocused the bar is what the focus is on, or there is no focus to speak of (the screensaver)
     */
    int decide(int keyCode, int action, int repeat, boolean playing, boolean barShowing, boolean barFocused, boolean stopAny) {
        if (swallowed != KEYCODE_UNKNOWN && keyCode == swallowed) {
            if (action == ACTION_UP) swallowed = KEYCODE_UNKNOWN;
            return EAT;
        }
        if (!playing || action != ACTION_DOWN || repeat > 0 || reserved(keyCode)) return PASS;
        if (stopAny || (barShowing && barFocused && isOk(keyCode))) {
            swallowed = keyCode;
            return STOP;
        }
        return PASS;
    }

    static boolean isOk(int keyCode) {
        return keyCode == KEYCODE_DPAD_CENTER || keyCode == KEYCODE_ENTER || keyCode == KEYCODE_NUMPAD_ENTER
                || keyCode == KEYCODE_BUTTON_A;
    }

    /** Keys «any key» leaves alone: the volume (and mute), the power and home family, the assistant and the TV's own settings key. */
    static boolean reserved(int keyCode) {
        switch (keyCode) {
            case KEYCODE_VOLUME_UP:
            case KEYCODE_VOLUME_DOWN:
            case KEYCODE_VOLUME_MUTE:
            case KEYCODE_POWER:
            case KEYCODE_SLEEP:
            case KEYCODE_WAKEUP:
            case KEYCODE_HOME:
            case KEYCODE_APP_SWITCH:
            case KEYCODE_ASSIST:
            case KEYCODE_VOICE_ASSIST:
            case KEYCODE_SETTINGS:
            case KEYCODE_UNKNOWN:
                return true;
            default:
                return false;
        }
    }
}
