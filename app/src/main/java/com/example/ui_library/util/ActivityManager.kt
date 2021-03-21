package com.example.ui_library.util

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference

class ActivityManager private constructor() {

    private val activityRefs = ArrayList<WeakReference<Activity>>()
    private val frontbackCallback = ArrayList<FrontBackCallback>()
    private var activityStartCount = 0
    private var front = true


    val topActivity: Activity?
        get() {
            if (activityRefs.size <= 0) {
                return null
            } else {
                return activityRefs[activityRefs.size - 1].get()
            }
            return null;
        }

    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(InnerActivityLifecycleCallbacks())
    }

    fun addFontBackCallback(callback: FrontBackCallback) {
        frontbackCallback.add(callback)
    }

    fun removeFrontBackCallback(callback: FrontBackCallback) {
        frontbackCallback.remove(callback)
    }

    inner class InnerActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            activityRefs.add(WeakReference(activity))
        }

        override fun onActivityStarted(activity: Activity) {
            activityStartCount++
            //activityStartCount>0 说明应用处于可见状态，也就是前台
            //！front 之前是不是在后台
            if (!front && activityStartCount > 0) {
                front = true;
                onFrontBackChanged(front)
            }
        }

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
            activityStartCount--
            if (activityStartCount <= 0 && front) {
                front = false
                onFrontBackChanged(front)
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            for (activityRef in activityRefs) {
                if (activityRef != null && activityRef.get() == activity) {
                    activityRefs.remove(activityRef)
                    break
                }
            }
        }

    }

    private fun onFrontBackChanged(front: Boolean) {
        for (callback in frontbackCallback) {
            callback.onChanged(front)
        }
    }

    interface FrontBackCallback {

        fun onChanged(front: Boolean)

    }

    companion object {
        val instance: ActivityManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ActivityManager()
        }
    }

}