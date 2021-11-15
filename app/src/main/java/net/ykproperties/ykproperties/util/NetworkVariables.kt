package net.ykproperties.ykproperties.util

import android.util.Log
import kotlin.properties.Delegates

object NetworkVariables {
    var isNetworkConnected: Boolean by Delegates.observable(false) { property, oldValue, newValue ->
        Log.d("Network-Connectivity", "$newValue")
    }
}