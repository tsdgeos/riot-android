package im.vector.analytics

import android.content.Context
import org.piwik.sdk.*
import org.piwik.sdk.extra.CustomVariables
import org.piwik.sdk.extra.TrackHelper

class PiwikAnalytics(context: Context) : Analytics {
    private val tracker: Tracker

    init {
        val config = TrackerConfig("https://piwik.riot.im/", 1, "AndroidPiwikTracker")
        tracker = Piwik.getInstance(context).newTracker(config)
        tracker.dispatchInterval = 30 * 1000
    }

    override fun trackScreen(screen: String, title: String?) {
        TrackHelper.track().screen(screen).title(title).with(tracker)
    }

    override fun trackEvent(event: Event) {
        TrackHelper.track().event(event.category.value, event.category.value).name(event.title).value(event.value).with(tracker)
    }

    @Suppress("DEPRECATION")
    override fun visitVariable(index: Int, name: String, value: String) {
        val customVariables = CustomVariables(tracker.defaultTrackMe.get(QueryParams.VISIT_SCOPE_CUSTOM_VARIABLES))
        customVariables.put(index, name, value)
        tracker.defaultTrackMe.set(QueryParams.VISIT_SCOPE_CUSTOM_VARIABLES, customVariables.toString())
    }

    override fun forceDispatch() {
        tracker.dispatch()
    }
}
