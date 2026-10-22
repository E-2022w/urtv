package sa.urtv.screensaver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Some TVs (TCL) leave the app cold after a power cycle and the first dream fails.
 * Any boot broadcast that does arrive brings the process up, so the first dream finds it warm.
 * It also retires the old periodic update job on installs that still have it.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context c, Intent i) {
        UpdateJob.cancelOld(c);
    }
}
