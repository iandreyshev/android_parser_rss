package ru.iandreyshev.parserrss.models.async;

import android.os.AsyncTask;
import android.support.annotation.CallSuper;
import android.support.v4.os.IResultReceiver;

import ru.iandreyshev.parserrss.app.IEvent;

abstract class Task<T, U, V> extends AsyncTask<T, U, V> {
    private ITaskListener<V> mListener;
    private IEvent mResultEvent;

    protected void setTaskListener(final ITaskListener<V> listener) {
        mListener = listener;
    }

    @CallSuper
    @Override
    protected void onPreExecute() {
        if (mListener != null) {
            mListener.onPreExecute();
        }
    }

    @CallSuper
    @Override
    protected void onPostExecute(V result) {
        if (mListener != null) {
            mListener.onPostExecute(result);
        }
        if (mResultEvent != null) {
            mResultEvent.doEvent();
        }
    }

    protected void setResultEvent(final IEvent resultEvent) {
        mResultEvent = resultEvent;
    }
}
