package com.algolia.instantsearch.telemetry

import com.algolia.instantsearch.Internal
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Internal
@Serializable
public enum class ComponentParam {
    // @formatter:off
    @ProtoNumber(0) Undefined,
    @ProtoNumber(1) ApiKey,
    @ProtoNumber(2) AppID,
    @ProtoNumber(3) Attribute,
    @ProtoNumber(4) Bounds,
    @ProtoNumber(5) ClearMode,
    @ProtoNumber(6) Client,
    @ProtoNumber(7) Facets,
    @ProtoNumber(8) Filter,
    @ProtoNumber(9) FilterGroupForAttribute,
    @ProtoNumber(10) FilterGroupIDs,
    @ProtoNumber(11) GroupName,
    @ProtoNumber(12) HierarchicalAttributes,
    @ProtoNumber(13) IndexName,
    @ProtoNumber(14) InfiniteScrolling,
    @ProtoNumber(15) Item,
    @ProtoNumber(16) Items,
    @ProtoNumber(17) Operator,
    @ProtoNumber(18) OrderedFacets,
    @ProtoNumber(19) Priority,
    @ProtoNumber(20) Range,
    @ProtoNumber(21) SearchTriggeringMode,
    @ProtoNumber(22) Searcher,
    @ProtoNumber(23) IsSelected,
    @ProtoNumber(24) SelectionMode,
    @ProtoNumber(25) SelectionModeForAttribute,
    @ProtoNumber(26) Selections,
    @ProtoNumber(27) Separator,
    @ProtoNumber(28) ShowItemsOnEmptyQuery,
    @ProtoNumber(29) IsDisjunctiveFacetingEnabled,
    @ProtoNumber(30) IsLoading,
    @ProtoNumber(31) Mode,
    @ProtoNumber(32) Number,
    @ProtoNumber(33) PersistentSelection,
    @ProtoNumber(34) SearchMode,
    @ProtoNumber(35) Strategy,
    @ProtoNumber(36) GroupIDs,
    @ProtoNumber(37) RequestOptions,
    @ProtoNumber(38) FacetsQuery,
    // @formatter:on
}
