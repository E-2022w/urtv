package sa.urtv.screensaver;

import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.Context;

/**
 * Older versions ran the page update as a periodic job. The check now runs when the screensaver
 * starts (Updater.checkIfDue), so this only cancels that job once; the class stays so a job the
 * system still holds from an old install finds its service and ends at once.
 */
public class UpdateJob extends JobService {
    private static final int ID = 1;

    static void cancelOld(Context c) {
        Prefs prefs = new Prefs(c);
        if (prefs.raw().getBoolean("oldJobCancelled", false)) return;
        JobScheduler js = (JobScheduler) c.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (js != null) js.cancel(ID);
        prefs.raw().edit().putBoolean("oldJobCancelled", true).apply();
    }

    @Override
    public boolean onStartJob(JobParameters p) {
        cancelOld(getApplicationContext());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters p) { return false; }
}
