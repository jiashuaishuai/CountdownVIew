package com.example.shuaijia.countdownview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by JiaShuai on 2018/7/6.
 */

public class CountDownTextView extends TextView {

    public CountDownFinishCall call;


    public void setCall(CountDownFinishCall call) {
        this.call = call;
    }

    public CountDownTextView(Context context) {
        super(context, null);
    }

    public CountDownTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public CountDownTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    /**
     * Millis since epoch when alarm should stop.
     */
    private long mMillisInFuture;

    /**
     * The interval in millis that the user receives callbacks
     */
    private final long mCountdownInterval = 1000;

    private long mStopTimeInFuture;

    private long currentTime;

    /**
     * boolean representing if the timer was cancelled
     */
    private boolean mCancelled = false;


    /**
     * false run
     */
    public boolean ismCancelled() {
        return mCancelled;
    }

    public long getmMillisInFuture() {
        return mMillisInFuture;
    }

    /**
     * Cancel the countdown.
     */
    public synchronized final void cancel() {
        mCancelled = true;
        mHandler.removeMessages(MSG);
    }

    /**
     * Start the countdown.
     */
    public synchronized final void start(long millisInFuture) {
        mMillisInFuture = millisInFuture;
        mCancelled = false;
        if (mMillisInFuture <= 0) {
            onFinish();
            return;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
    }


    /**
     * Callback fired on regular interval.
     *
     * @param millisUntilFinished The amount of time until finished.
     */
    public void onTick(long millisUntilFinished) {
        currentTime = millisUntilFinished;
        setText(formatTime2(millisUntilFinished));

    }

    public long getCurrentTime() {
        return currentTime;
    }

    /**
     * Callback fired when the time is up.
     */
    public void onFinish() {
        cancel();
        if (call != null) {
            call.call();
        }
    }


    private static final int MSG = 1;


    // handles counting down
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (CountDownTextView.this) {
                if (mCancelled) {
                    return;
                }

                final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();

                if (millisLeft <= 0) {
                    onFinish();
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    onTick(millisLeft);

                    // take into account user's onTick taking time to execute
                    long lastTickDuration = SystemClock.elapsedRealtime() - lastTickStart;
                    long delay;

                    if (millisLeft < mCountdownInterval) {
                        // just delay until done
                        delay = millisLeft - lastTickDuration;

                        // special case: user's onTick took more than interval to
                        // complete, trigger onFinish without delay
                        if (delay < 0) delay = 0;
                    } else {
                        delay = mCountdownInterval - lastTickDuration;

                        // special case: user's onTick took more than interval to
                        // complete, skip to next interval
                        while (delay < 0) delay += mCountdownInterval;
                    }

                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    };

    public interface CountDownFinishCall {
        void call();
    }

    public static String formatTime2(long lefttime) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss", Locale.CHINA);
        String sDateTime = sdf.format(lefttime);
        return sDateTime;
    }

}
