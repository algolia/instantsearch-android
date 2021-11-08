package com.algolia.instantsearch.telemetry

import com.algolia.instantsearch.InternalInstantSearch
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@InternalInstantSearch
@Serializable
public enum class ComponentParam {

    @ProtoNumber(0)
    Undefined,

    @ProtoNumber(1)
    ApiKey,

    @ProtoNumber(2)
    AppID,

    @ProtoNumber(3)
    Attribute,

    @ProtoNumber(4)
    Bounds,

    @ProtoNumber(5)
    ClearMode,

    @ProtoNumber(6)
    Client,

    @ProtoNumber(7)
    Facets,

    @ProtoNumber(8)
    Filter,

    @ProtoNumber(9)
    FilterGroupForAttribute,

    @ProtoNumber(10)
    FilterGroupIDs,

    @ProtoNumber(11)
    GroupName,

    @ProtoNumber(12)
    HierarchicalAttributes,

    @ProtoNumber(13)
    IndexName,

    @ProtoNumber(14)
    InfiniteScrolling,

    @ProtoNumber(15)
    Item,

    @ProtoNumber(16)
    Items,

    @ProtoNumber(17)
    Operator,

    @ProtoNumber(18)
    OrderedFacets,

    @ProtoNumber(19)
    Priority,

    @ProtoNumber(20)
    Range,

    @ProtoNumber(21)
    SearchTriggeringMode,

    @ProtoNumber(22)
    Searcher,

    @ProtoNumber(23)
    Selected,

    @ProtoNumber(24)
    SelectionMode,

    @ProtoNumber(25)
    SelectionModeForAttribute,

    @ProtoNumber(26)
    Selections,

    @ProtoNumber(27)
    Separator,

    @ProtoNumber(28)
    ShowItemsOnEmptyQuery,
}
