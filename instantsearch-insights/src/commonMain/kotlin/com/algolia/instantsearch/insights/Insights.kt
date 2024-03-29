package com.algolia.instantsearch.insights

import com.algolia.search.configuration.Credentials
import com.algolia.search.model.insights.InsightsEvent
import com.algolia.search.model.insights.UserToken

public interface Insights : HitsAfterSearchTrackable, FilterTrackable, Credentials {

    /**
     * Change this variable to `true` or `false` to disable Insights, opting-out the current session from tracking.
     */
    public var enabled: Boolean

    /**
     * Change this variable to change the default debouncing interval. Values lower than 15 minutes will be ignored.
     */
    public var debouncingIntervalInMinutes: Long?

    /**
     * Set a user identifier that will override any event's.
     *
     * Depending if the user is logged-in or not, several strategies can be used from a sessionId to a technical identifier.
     * You should always send pseudonymous or anonymous userTokens.
     */
    public var userToken: UserToken?

    /**
     * Change this variable to change the default amount of event sent at once.
     */
    public var minBatchSize: Int

    /**
     * Change this variable to `true` or `false` to enable or disable logging.
     * Use a filter on tag `Algolia Insights` to see all logs generated by the Insights library.
     */
    public var loggingEnabled: Boolean

    /**
     * Tracks a View event constructed manually.
     *
     * @param event insights view event to be tracked
     */
    public fun viewed(event: InsightsEvent.View)

    /**
     * Tracks a Click event constructed manually.
     *
     * @param event insights click event to be tracked
     */
    public fun clicked(event: InsightsEvent.Click)

    /**
     * Tracks a Conversion event, constructed manually.
     *
     * @param event insights conversion event to be tracked
     */
    public fun converted(event: InsightsEvent.Conversion)

    /**
     * Method for tracking an event.
     * [documentation][https://www.algolia.com/doc/rest-api/insights/?language=android#push-events].
     *
     * @param event insights event to be tracked.
     */
    public fun track(event: InsightsEvent)

    /**
     * Insights configuration.
     */
    public class Configuration(

        /**
         * Maximum amount of time in milliseconds before a connect timeout
         */
        public val connectTimeoutInMilliseconds: Long = 5000,

        /**
         * Maximum amount of time in milliseconds before a read timeout.
         */
        public val readTimeoutInMilliseconds: Long = 5000,

        /**
         * Default User Token.
         */
        public val defaultUserToken: UserToken? = null,

        /**
         * Defines if the timestamps of the events should be automatically attributed if not provided while calling the
         * event capturing function. If set to `false`, the events will be sent without timestamp value and will be
         * automatically attributed on the server, which may affect the accuracy of the events.
         */
        public val generateTimestamps: Boolean = true,
    )

    public companion object
}
