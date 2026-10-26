package sa.urtv.home;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The remote's settings key (KEYCODE_SETTINGS) inside other apps: pure decisions, no Android (tests/SettingsKeyRun.java).
 * On some Google TVs the key does nothing while Netflix or YouTube is open (GitHub issue 37). SleepService, when the person (or the
 * agent: optimize/dock.sh settings-key on) turned this on, takes the key and opens the TV's settings itself. Off by default.
 */
public final class SettingsKeyLogic {
    private SettingsKeyLogic() { }

    public static final int KEYCODE_SETTINGS = 176;
    public static final int ACTION_DOWN = 0, ACTION_UP = 1;
    public static final int PASS = 0;         // not ours: the key goes on to the app and the system as before
    public static final int OPEN = 1;         // the first press: swallow it and open the settings
    public static final int SWALLOW = 2;      // its repeats and its release: swallow, do nothing (the system must not see half a key)

    public static final String TV_SETTINGS = "com.android.tv.settings";
    public static final String TV_SETTINGS_MAIN = "component:com.android.tv.settings/.MainSettings";
    public static final String ACTION_SETTINGS = "action:android.settings.SETTINGS";

    /** What to do with one key event. Only the settings key, and only when it is turned on, is ever taken. */
    public static int decide(boolean on, int keyCode, int action, int repeatCount, boolean downWasOurs) {
        if (!on || keyCode != KEYCODE_SETTINGS) return PASS;
        if (action == ACTION_DOWN) return repeatCount == 0 ? OPEN : SWALLOW;
        return downWasOurs ? SWALLOW : PASS;   // a release whose press we never saw belongs to someone else
    }

    /** What to try, in order: the stock TV settings app's own main screen when this TV has it, then the generic settings action
     *  (every Android has it; on a TV it is the same screen or the vendor's). The caller starts the first one that resolves. */
    public static List<String> candidates(Set<String> installedPackages) {
        List<String> r = new ArrayList<>();
        if (installedPackages != null && installedPackages.contains(TV_SETTINGS)) r.add(TV_SETTINGS_MAIN);
        r.add(ACTION_SETTINGS);
        return r;
    }
}
