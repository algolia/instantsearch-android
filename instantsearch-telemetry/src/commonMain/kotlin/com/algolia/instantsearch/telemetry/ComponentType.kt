package com.algolia.instantsearch.telemetry

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
public enum class ComponentType {

    @ProtoNumber(0)
    Unknown,

    @ProtoNumber(1)
    HitsSearcher,

    @ProtoNumber(2)
    FacetSearcher,

    @ProtoNumber(3)
    MultiSearcher,

    @ProtoNumber(4)
    AnswersSearcher,

    @ProtoNumber(5)
    FilterState,

    @ProtoNumber(6)
    DynamicFacets,

    @ProtoNumber(7)
    HierarchicalFacets,

    @ProtoNumber(8)
    FacetList,

    @ProtoNumber(9)
    FilterClear,

    @ProtoNumber(10)
    FilterList,

    @ProtoNumber(11)
    FilterToggle,

    @ProtoNumber(12)
    NumberFilter,

    @ProtoNumber(13)
    NumberRangeFilter,

    @ProtoNumber(14)
    CurrentFilters,

    @ProtoNumber(15)
    Hits,

    @ProtoNumber(16)
    Loading,

    @ProtoNumber(17)
    Stats,

    @ProtoNumber(18)
    QueryInput,

    @ProtoNumber(19)
    QueryRuleCustomData,

    @ProtoNumber(20)
    RelevantSort,

    @ProtoNumber(21)
    SortBy,
}
