package com.algolia.instantsearch.telemetry

public expect interface Component {

    /**
     * `.com.algolia.instantsearch.telemetry.TelemetryComponentType type = 600;`
     * @return The enum numeric value on the wire for type.
     */
    fun getTypeValue(): Int

    /**
     * `.com.algolia.instantsearch.telemetry.TelemetryComponentType type = 600;`
     * @return The type.
     */
    fun getType(): ComponentType?

    /**
     * `repeated .com.algolia.instantsearch.telemetry.TelemetryComponentParam parameters = 601;`
     * @return A list containing the parameters.
     */
    fun getParametersList(): List<ComponentParam?>?

    /**
     * `repeated .com.algolia.instantsearch.telemetry.TelemetryComponentParam parameters = 601;`
     * @return The count of parameters.
     */
    fun getParametersCount(): Int

    /**
     * `repeated .com.algolia.instantsearch.telemetry.TelemetryComponentParam parameters = 601;`
     * @param index The index of the element to return.
     * @return The parameters at the given index.
     */
    fun getParameters(index: Int): ComponentParam?

    /**
     * `repeated .com.algolia.instantsearch.telemetry.TelemetryComponentParam parameters = 601;`
     * @return A list containing the enum numeric values on the wire for parameters.
     */
    fun getParametersValueList(): List<Int?>?

    /**
     * `repeated .com.algolia.instantsearch.telemetry.TelemetryComponentParam parameters = 601;`
     * @param index The index of the value to return.
     * @return The enum numeric value on the wire of parameters at the given index.
     */
    fun getParametersValue(index: Int): Int

    /**
     * `bool isConnector = 602;`
     * @return The isConnector.
     */
    fun getIsConnector(): Boolean
}
