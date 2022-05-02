@file:JvmName("ViewExt")

package com.lapisy.commonkit

import android.os.Build
import android.view.View
import android.view.ViewParent

fun View.isSafeToRequestDirectly(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && isInLayout) {
        // when isInLayout == true and isLayoutRequested == true,
        // means that this layout pass will layout current view which will
        // make currentView.isLayoutRequested == false, and this will let currentView
        // ignored in process handling requests called during last layout pass.
        isLayoutRequested.not()
    } else {
        var ancestorLayoutRequested = false
        var p: ViewParent? = parent
        while (p != null) {
            if (p.isLayoutRequested) {
                ancestorLayoutRequested = true
                break
            }
            p = p.parent
        }
        ancestorLayoutRequested.not()
    }
}

fun View.safeRequestLayout() {
    if (isSafeToRequestDirectly()) {
        requestLayout()
    } else {
        post { requestLayout() }
    }
}
