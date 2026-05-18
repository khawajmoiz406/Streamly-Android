package com.livestreaming.streamly.core.di

import android.app.Activity
import java.lang.ref.WeakReference

object ActivityHolder {
    private var activityRef: WeakReference<Activity?> = WeakReference(null)

    fun set(activity: Activity) {
        activityRef = WeakReference(activity)
    }

    fun clear() {
        activityRef = WeakReference(null)
    }

    fun get(): Activity? = activityRef.get()
}