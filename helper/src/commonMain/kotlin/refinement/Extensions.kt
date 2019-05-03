package refinement

import filter.FilterGroupID


public fun RefinementOperator.toGroupID(groupName: String): FilterGroupID {
    return when (this) {
        RefinementOperator.And -> FilterGroupID.And(groupName)
        RefinementOperator.Or -> FilterGroupID.Or(groupName)
    }
}