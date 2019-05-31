package filter.state

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.toFilterGroups
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterGroup
import shouldBeEmpty
import shouldBeFalse
import shouldBeTrue
import shouldEqual
import kotlin.test.Test


class TestFilterState {

    private val nameA = "nameA"
    private val nameB = "nameB"
    private val groupA = FilterGroupID.And(nameA)
    private val groupB = FilterGroupID.And(nameB)
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

            getFacetGroups() shouldEqual mapOf(groupA to setOf(facetA, facetB))
        }
    }

    @Test
    fun addToDifferentGroup() {
        FilterState().apply {
            add(groupA, facetA)
            add(groupB, facetA)

            getFacetGroups() shouldEqual mapOf(
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

            getFacetGroups() shouldEqual mapOf(groupA to setOf(facetA))
            getNumericGroups() shouldEqual mapOf(groupA to setOf(numeric))
        }
    }

    @Test
    fun addDifferentTypesToDifferentGroup() {
        FilterState().apply {
            add(groupA, facetA)
            add(groupB, numeric)

            getFacetGroups() shouldEqual mapOf(groupA to setOf(facetA))
            getNumericGroups() shouldEqual mapOf(groupB to setOf(numeric))
        }
    }

    @Test
    fun removeHits() {
        FilterState().apply {
            add(groupA, facetA)
            add(groupA, facetB)
            remove(groupA, facetA)

            getFacetGroups() shouldEqual mapOf(groupA to setOf(facetB))
        }
    }

    @Test
    fun removeEmptyMisses() {
        FilterState().apply {
            remove(groupA, facetA)

            getFacetGroups().shouldBeEmpty()
            getTagGroups().shouldBeEmpty()
            getNumericGroups().shouldBeEmpty()
        }
    }

    @Test
    fun removeMisses() {
        FilterState().apply {
            add(groupA, facetA)
            remove(groupA, facetB)

            getFacetGroups() shouldEqual mapOf(groupA to setOf(facetA))
        }
    }

    @Test
    fun clearGroup() {
        FilterState().apply {
            add(groupA, facetA)
            add(groupB, facetB)
            clear(groupB)

            getFacetGroups() shouldEqual mapOf(groupA to setOf(facetA))
        }
    }

    @Test
    fun clearAll() {
        FilterState().apply {
            add(groupA, facetA)
            add(groupB, facetB)
            add(groupA, numeric)
            add(groupA, tag)
            clear()

            getFacetGroups().shouldBeEmpty()
            getTagGroups().shouldBeEmpty()
            getNumericGroups().shouldBeEmpty()
        }
    }

    @Test
    fun clearOne() {
        FilterState().apply {
            add(groupA, facetA)
            add(groupB, facetB)
            add(groupA, numeric)
            add(groupA, tag)
            clear(groupB)

            getFacetGroups() shouldEqual mapOf(groupA to setOf(facetA))
            getTagGroups() shouldEqual mapOf(groupA to setOf(tag))
            getNumericGroups() shouldEqual mapOf(groupA to setOf(numeric))
        }
    }

    @Test
    fun clearExceptOne() {
        FilterState().apply {
            add(groupA, facetA)
            add(groupB, facetB)
            add(groupA, numeric)
            add(groupA, tag)
            clearExcept(listOf(groupB))

            getFacetGroups() shouldEqual mapOf(groupB to setOf(facetB))
            getTagGroups().shouldBeEmpty()
            getNumericGroups().shouldBeEmpty()
        }
    }

    @Test
    fun transform() {
        val filterGroups = FilterState().apply {
            add(groupA, facetA)
            add(groupB, facetB)
        }.toFilterGroups()

        filterGroups shouldEqual listOf(
            FilterGroup.And.Facet(facetA, name = groupA.name),
            FilterGroup.And.Facet(facetB, name = groupB.name)
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
            getFacetGroups() shouldEqual mapOf(groupA to setOf(facetA))
            toggle(groupA, facetA)
            getFacetGroups().shouldBeEmpty()
        }
    }

    @Test
    fun getFilters() {
        FilterState().apply {
            add(groupA, facetA)
            add(groupB, facetB)
            add(groupA, numeric)
            add(groupA, tag)

            getFilters() shouldEqual setOf(facetA, facetB, numeric, tag)
        }
    }
}