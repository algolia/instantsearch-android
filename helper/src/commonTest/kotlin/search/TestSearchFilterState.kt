package search

import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterGroup
import com.algolia.search.model.filter.FilterGroupConverter
import shouldBeFalse
import shouldBeTrue
import shouldEqual
import kotlin.test.Test


class TestSearchFilterState {

    private val nameA = "nameA"
    private val nameB = "nameB"
    private val groupA = Group.And.Tag(nameA)
    private val groupB = Group.And.Tag(nameB)
    private val filterA = Filter.Tag("tagA")
    private val filterB = Filter.Tag("tagB")

    @Test
    fun addToSameGroup() {
        SearchFilterState().apply {
            add(groupA, filterA)
            add(groupA, filterB)

            get() shouldEqual mapOf(groupA to setOf(filterA, filterB))
        }
    }

    @Test
    fun addToDifferentGroup() {
        SearchFilterState().apply {
            add(groupA, filterA)
            add(groupB, filterA)

            get() shouldEqual mapOf(
                groupA to setOf(filterA),
                groupB to setOf(filterA)
            )
        }
    }

    @Test
    fun removeHits() {
        SearchFilterState().apply {
            add(groupA, filterA)
            add(groupA, filterB)
            remove(groupA, filterA).shouldBeTrue()

            get() shouldEqual mapOf(
                groupA to setOf(filterB)
            )
        }
    }

    @Test
    fun removeEmptyMisses() {
        SearchFilterState().apply {
            remove(groupA, filterA).shouldBeFalse()

            get() shouldEqual mapOf()
        }
    }

    @Test
    fun removeMisses() {
        SearchFilterState().apply {
            add(groupA, filterA)
            remove(groupA, filterB).shouldBeFalse()

            get() shouldEqual mapOf(groupA to setOf(filterA))
        }
    }

    @Test
    fun clearGroup() {
        SearchFilterState().apply {
            add(groupA, filterA)
            add(groupB, filterB)
            clear(groupB)

            get() shouldEqual mapOf(groupA to setOf(filterA))
        }
    }

    @Test
    fun clear() {
        SearchFilterState().apply {
            add(groupA, filterA)
            add(groupB, filterB)
            clear()

            get() shouldEqual mapOf()
        }
    }

    @Test
    fun replace() {
        SearchFilterState().apply {
            add(groupA, filterA)
            replace(groupA, setOf(filterB))

            get() shouldEqual mapOf(
                groupA to setOf(filterB)
            )
        }
    }

    @Test
    fun transform() {
        val filterGroups = SearchFilterState().apply {
            add(Group.Or.Tag(nameA), filterA)
            add(Group.And.Tag(nameA), filterA)
        }.toFilterGroups()

        filterGroups shouldEqual listOf(
            FilterGroup.Or(filterA),
            FilterGroup.And(filterA)
        )
    }
}