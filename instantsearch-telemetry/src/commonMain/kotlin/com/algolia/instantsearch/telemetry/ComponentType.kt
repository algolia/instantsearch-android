package com.algolia.instantsearch.telemetry

import com.algolia.instantsearch.Internal
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Internal
@Serializable
public enum class ComponentType {
    // @formatter:off
    @ProtoNumber(0) Unknown,
    @ProtoNumber(1) HitsSearcher,
    @ProtoNumber(2) FacetSearcher,
    @ProtoNumber(3) MultiSearcher,
    @ProtoNumber(4) AnswersSearcher,
    @ProtoNumber(5) FilterState,
    @ProtoNumber(6) DynamicFacets,        // Dynamic Facets
    @ProtoNumber(7) HierarchicalFacets,   // Hierarchical Menu
    @ProtoNumber(8) FacetList,            // Refinement List
    @ProtoNumber(9) FilterClear,          // Clear Filters
    @ProtoNumber(10) FacetFilterList,     // Filter List (Facet)
    @ProtoNumber(11) NumericFilterList,   // Filter List (Numeric)
    @ProtoNumber(12) TagFilterList,       // Filter List (Tag)
    @ProtoNumber(13) FilterToggle,        // Filter Toggle
    @ProtoNumber(14) NumberFilter,        // Filter Numeric Comparison
    @ProtoNumber(15) NumberRangeFilter,   // Filter Numeric Range
    @ProtoNumber(16) CurrentFilters,      // Current Filters
    @ProtoNumber(17) Hits,                // Hits
    @ProtoNumber(18) Loading,             // Loading
    @ProtoNumber(19) Stats,               // Stats
    @ProtoNumber(20) SearchBox,           // SearchBox
    @ProtoNumber(21) QueryRuleCustomData, // QueryRuleCustomData
    @ProtoNumber(22) RelevantSort,        // RelevantSort
    @ProtoNumber(23) SortBy,              // SortBy
    @ProtoNumber(24) RelatedItems,        // Related items
    @ProtoNumber(25) FilterMap,           // Filter Map
    @ProtoNumber(26) MultiHits,           // Multi Hits
    // @formatter:on
}
