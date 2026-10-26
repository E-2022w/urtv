package sa.urtv.home;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * Puts the TV to sleep for «مؤقت النوم». An app that is not part of the system cannot press the power key: PowerManager.goToSleep
 * needs a permission only the system holds, and DevicePolicyManager.lockNow needs a device admin that the TV does not offer. The one
 * way left that needs no root is an accessibility service: Android lets it ask for the lock-screen action, which turns the screen
 * off the way the power key does (API 28 and later). It listens to nothing and reads nothing; it exists for this one call.
 *
 * The person turns it on once in the TV's accessibility settings, or the agent does it over ADB (optimize/dock.sh sleep-setup).
 *
 * Second, opt-in job (GitHub issue 37): the remote's settings key does nothing inside Netflix or YouTube on some Google TVs. When
 * `optimize/dock.sh settings-key on` was given, the service asks Android for key events (FLAG_REQUEST_FILTER_KEY_EVENTS, added
 * at run time only while it is on, so a TV that never turned it on is exactly as before) and, for that one key, opens the TV's
 * settings itself (SettingsKeyLogic). Every other key is passed on untouched.
 *
 * Until then SleepTimer stops the sound and brings the screensaver up instead, and says so.
 */
public class SleepService extends AccessibilityService {
    private static volatile SleepService running;

    private static final String PREFS = "settingskey";
    private boolean downWasOurs;

    @Override protected void onServiceConnected() { running = this; applyKeyFilter(); }
    @Override public boolean onUnbind(android.content.Intent i) { running = null; return super.onUnbind(i); }
    @Override public void onAccessibilityEvent(AccessibilityEvent e) { }
    @Override public void onInterrupt() { }

    /** Opt-in state, kept in our own preferences: off unless the agent or the person turned it on. */
    static boolean settingsKeyOn(Context c) {
        return c.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean("on", false);
    }

    /** Turn it on or off; takes effect now when the service is running (otherwise at its next start). */
    static void setSettingsKey(Context c, boolean on) {
        c.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putBoolean("on", on).apply();
        SleepService s = running;
        if (s != null) s.applyKeyFilter();
    }

    /** Ask for key events only while the option is on. canRequestFilterKeyEvents is declared in sleep_service.xml; the flag is not. */
    private void applyKeyFilter() {
        try {
            AccessibilityServiceInfo info = getServiceInfo();
            if (info == null) return;
            if (settingsKeyOn(this)) info.flags |= AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS;
            else info.flags &= ~AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS;
            setServiceInfo(info);
        } catch (RuntimeException ignored) { }   // never let this break the sleep feature
    }

    @Override protected boolean onKeyEvent(KeyEvent e) {
        int d = SettingsKeyLogic.decide(settingsKeyOn(this), e.getKeyCode(), e.getAction(), e.getRepeatCount(), downWasOurs);
        if (d == SettingsKeyLogic.PASS) return false;
        downWasOurs = e.getAction() == KeyEvent.ACTION_DOWN;
        if (d == SettingsKeyLogic.OPEN) openSettings();
        return true;
    }

    /** Start the first settings screen that this TV has (SettingsKeyLogic.candidates); the name of what was started, or null. */
    String openSettings() {
        PackageManager pm = getPackageManager();
        Set<String> have = new HashSet<>();
        try { pm.getPackageInfo(SettingsKeyLogic.TV_SETTINGS, 0); have.add(SettingsKeyLogic.TV_SETTINGS); } catch (PackageManager.NameNotFoundException ignored) { }
        for (String spec : SettingsKeyLogic.candidates(have)) {
            Intent i = spec.startsWith("component:") ? new Intent(Intent.ACTION_MAIN).setComponent(ComponentName.unflattenFromString(spec.substring(10)))
                                                      : new Intent(spec.substring(7));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (i.resolveActivity(pm) == null) continue;
            try { startActivity(i); return spec; } catch (RuntimeException ignored) { }
        }
        return null;
    }

    /** For «settings-key test»: open the settings now, as the key would. */
    static String testOpen() {
        SleepService s = running;
        if (s == null) return "the sleep service is off: run optimize/dock.sh sleep-setup first";
        String r = s.openSettings();
        return r == null ? "no settings screen on this TV could be started" : "opened " + r;
    }

    /** Is the service on? (the person or ADB enabled it, and Android has started it) */
    static boolean enabled() { return running != null; }

    /** Turn the screen off now; false when the service is off or this Android is older than 9. */
    static boolean sleepNow() {
        SleepService s = running;
        return s != null && Build.VERSION.SDK_INT >= 28 && s.performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN);
    }
}
