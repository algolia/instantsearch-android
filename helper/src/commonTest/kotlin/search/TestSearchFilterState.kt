package search

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterGroup
import filter.FilterState
import filter.toFilterGroups
import shouldBeEmpty
import shouldBeFalse
import shouldBeTrue
import shouldEqual
import kotlin.test.Test


class TestSearchFilterState {

    private val nameA = "nameA"
    private val nameB = "nameB"
    private val groupA = GroupID.And(nameA)
    private val groupB = GroupID.And(nameB)
    private val attributeA = Attribute(nameA)
    private val attributeB = Attribute(nameB)
    private val facetA = Filter.Facet(attributeA, 0)
    private val facetB = Filter.Facet(attributeB, 0)
    private val tag = Filter.Tag("0")
    private val numeric = Filter.Numeric(attributeA, 0..10)

    @Test
    fun addToSameGroup() {
        FilterState().apply {
            add(groupA, facetA)
            add(groupA, facetB)

            getFacets() shouldEqual mapOf(groupA to setOf(facetA, facetB))
        }
    }

    @Test
    fun addToDifferentGroup() {
        FilterState().apply {
            add(groupA, facetA)
            add(groupB, facetA)

            getFacets() shouldEqual mapOf(
                groupA to setOf(facetA),
                groupB to setOf(facetA)
            )
        }
    }

    @Test
    fun addDifferentTypesToSameGroup() {
        FilterState().apply {
            add(groupA, facetA)
            add(groupA, numeric)

            getFacets() shouldEqual mapOf(groupA to setOf(facetA))
            getNumerics() shouldEqual mapOf(groupA to setOf(numeric))
        }
    }

    @Test
    fun addDifferentTypesToDifferentGroup() {
        FilterState().apply {
            add(groupA, facetA)
            add(groupB, numeric)

            getFacets() shouldEqual mapOf(groupA to setOf(facetA))
            getNumerics() shouldEqual mapOf(groupB to setOf(numeric))
        }
    }

    @Test
    fun removeHits() {
        FilterState().apply {
            add(groupA, facetA)
            add(groupA, facetB)
            remove(groupA, facetA)

            getFacets() shouldEqual mapOf(groupA to setOf(facetB))
        }
    }

    @Test
    fun removeEmptyMisses() {
        FilterState().apply {
            remove(groupA, facetA)

            getFacets().shouldBeEmpty()
            getTags().shouldBeEmpty()
            getNumerics().shouldBeEmpty()
        }
    }

    @Test
    fun removeMisses() {
        FilterState().apply {
            add(groupA, facetA)
            remove(groupA, facetB)

            getFacets() shouldEqual mapOf(groupA to setOf(facetA))
        }
    }

    @Test
    fun clearGroup() {
        FilterState().apply {
            add(groupA, facetA)
            add(groupB, facetB)
            clear(groupB)

            getFacets() shouldEqual mapOf(groupA to setOf(facetA))
        }
    }

    @Test
    fun clear() {
        FilterState().apply {
            add(groupA, facetA)
            add(groupB, facetB)
            add(groupA, numeric)
            add(groupA, tag)
            clear()

            getFacets().shouldBeEmpty()
            getTags().shouldBeEmpty()
            getNumerics().shouldBeEmpty()
        }
    }

    @Test
    fun transform() {
        val filterGroups = FilterState().apply {
            add(groupA, facetA)
            add(groupB, facetB)
        }.toFilterGroups()

        filterGroups shouldEqual listOf(
            FilterGroup.And.Facet(facetA),
            FilterGroup.And.Facet(facetB)
        )
    }

    @Test
    fun contains() {
        FilterState().apply {
            add(groupA, facetA)
            contains(groupA, facetA).shouldBeTrue()
            contains(groupA, facetB).shouldBeFalse()
            contains(groupB, facetA).shouldBeFalse()
        }
    }

    @Test
    fun toggle() {
        FilterState().apply {
            toggle(groupA, facetA)
            getFacets() shouldEqual mapOf(groupA to setOf(facetA))
            toggle(groupA, facetA)
            getFacets().shouldBeEmpty()
        }
    }
}