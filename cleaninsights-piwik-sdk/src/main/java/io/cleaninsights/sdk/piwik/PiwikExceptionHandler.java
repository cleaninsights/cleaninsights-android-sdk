/*
 * Android SDK for Piwik
 *
 * @link https://github.com/piwik/piwik-android-sdk
 * @license https://github.com/piwik/piwik-sdk-android/blob/master/LICENSE BSD-3 Clause
 */

package io.cleaninsights.sdk.piwik;

import timber.log.Timber;

/**
 * An exception handler that wraps the existing exception handler and dispatches event to a {@link Measurer}.
 * <p/>
 * Also see documentation for {@link MeasureHelper#uncaughtExceptions()}
 */
public class PiwikExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Measurer mTracker;
    private final MeasureMe mTrackMe;
    private final Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    public PiwikExceptionHandler(Measurer tracker, MeasureMe trackMe) {
        mTracker = tracker;
        mTrackMe = trackMe;
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public Measurer getTracker() {
        return mTracker;
    }

    /**
     * This will give you the previous exception handler that is now wrapped.
     */
    public Thread.UncaughtExceptionHandler getDefaultExceptionHandler() {
        return mDefaultExceptionHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            String excInfo = ex.getMessage();
            MeasureHelper.track().exception(ex).description(excInfo).fatal(true).with(getTracker());
            // Immediately dispatch as the app might be dying after rethrowing the exception
            getTracker().dispatch();
        } catch (Exception e) {
            Timber.tag(Measurer.LOGGER_TAG).e(e, "Couldn't measure uncaught exception");
        } finally {
            // re-throw critical exception further to the os (important)
            if (getDefaultExceptionHandler() != null && getDefaultExceptionHandler() != this) {
                getDefaultExceptionHandler().uncaughtException(thread, ex);
            }
        }
    }
}
