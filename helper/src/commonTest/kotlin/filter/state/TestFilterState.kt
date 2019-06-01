package filter.state

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
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
    private val groupAndA = FilterGroupID(nameA, FilterOperator.And)
    private val groupAndB = FilterGroupID(nameB, FilterOperator.And)
    private val groupOrA = FilterGroupID(nameA, FilterOperator.Or)
    private val attributeA = Attribute(nameA)
    private val attributeB = Attribute(nameB)
    private val facetA = Filter.Facet(attributeA, 0)
    private val facetB = Filter.Facet(attributeB, 0)
    private val tag = Filter.Tag("0")
    private val numeric = Filter.Numeric(attributeA, 0..10)

    @Test
    fun constructor() {
        val map = mapOf(
            groupAndA to setOf(facetA, tag, numeric),
            groupOrA to setOf(facetB, tag, numeric)
        )

        FilterState(map).apply {
            getFacetFilters(groupAndA) shouldEqual setOf(facetA)
            getFacetFilters(groupOrA) shouldEqual setOf(facetB)
            getNumericFilters(groupAndA) shouldEqual setOf(numeric)
            getNumericFilters(groupOrA) shouldEqual setOf(numeric)
            getTagFilters(groupAndA) shouldEqual setOf(tag)
            getTagFilters(groupOrA) shouldEqual setOf(tag)
        }
    }

    @Test
    fun addToSameGroup() {
        FilterState().apply {
            add(groupAndA, facetA)
            add(groupAndA, facetB)

            getFacetGroups() shouldEqual mapOf(groupAndA to setOf(facetA, facetB))
        }
    }

    @Test
    fun addToDifferentGroup() {
        FilterState().apply {
            add(groupAndA, facetA)
            add(groupAndB, facetA)

            getFacetGroups() shouldEqual mapOf(
                groupAndA to setOf(facetA),
                groupAndB to setOf(facetA)
            )
        }
    }

    @Test
    fun addDifferentTypesToSameGroup() {
        FilterState().apply {
            add(groupAndA, facetA)
            add(groupAndA, numeric)

            getFacetGroups() shouldEqual mapOf(groupAndA to setOf(facetA))
            getNumericGroups() shouldEqual mapOf(groupAndA to setOf(numeric))
        }
    }

    @Test
    fun addDifferentTypesToDifferentGroup() {
        FilterState().apply {
            add(groupAndA, facetA)
            add(groupAndB, numeric)

            getFacetGroups() shouldEqual mapOf(groupAndA to setOf(facetA))
            getNumericGroups() shouldEqual mapOf(groupAndB to setOf(numeric))
        }
    }

    @Test
    fun removeHits() {
        FilterState().apply {
            add(groupAndA, facetA)
            add(groupAndA, facetB)
            remove(groupAndA, facetA)

            getFacetGroups() shouldEqual mapOf(groupAndA to setOf(facetB))
        }
    }

    @Test
    fun removeEmptyMisses() {
        FilterState().apply {
            remove(groupAndA, facetA)

            getFacetGroups().shouldBeEmpty()
            getTagGroups().shouldBeEmpty()
            getNumericGroups().shouldBeEmpty()
        }
    }

    @Test
    fun removeMisses() {
        FilterState().apply {
            add(groupAndA, facetA)
            remove(groupAndA, facetB)

            getFacetGroups() shouldEqual mapOf(groupAndA to setOf(facetA))
        }
    }

    @Test
    fun clearGroup() {
        FilterState().apply {
            add(groupAndA, facetA)
            add(groupAndB, facetB)
            clear(groupAndB)

            getFacetGroups() shouldEqual mapOf(groupAndA to setOf(facetA))
        }
    }

    @Test
    fun clearAll() {
        FilterState().apply {
            add(groupAndA, facetA)
            add(groupAndB, facetB)
            add(groupAndA, numeric)
            add(groupAndA, tag)
            clear()

            getFacetGroups().shouldBeEmpty()
            getTagGroups().shouldBeEmpty()
            getNumericGroups().shouldBeEmpty()
        }
    }

    @Test
    fun clearOne() {
        FilterState().apply {
            add(groupAndA, facetA)
            add(groupAndB, facetB)
            add(groupAndA, numeric)
            add(groupAndA, tag)
            clear(groupAndB)

            getFacetGroups() shouldEqual mapOf(groupAndA to setOf(facetA))
            getTagGroups() shouldEqual mapOf(groupAndA to setOf(tag))
            getNumericGroups() shouldEqual mapOf(groupAndA to setOf(numeric))
        }
    }

    @Test
    fun clearExceptOne() {
        FilterState().apply {
            add(groupAndA, facetA)
            add(groupAndB, facetB)
            add(groupAndA, numeric)
            add(groupAndA, tag)
            clearExcept(listOf(groupAndB))

            getFacetGroups() shouldEqual mapOf(groupAndB to setOf(facetB))
            getTagGroups().shouldBeEmpty()
            getNumericGroups().shouldBeEmpty()
        }
    }

    @Test
    fun transform() {
        val filterGroups = FilterState().apply {
            add(groupAndA, facetA)
            add(groupAndB, facetB)
        }.toFilterGroups()

        filterGroups shouldEqual listOf(
            FilterGroup.And.Facet(facetA, name = groupAndA.name),
            FilterGroup.And.Facet(facetB, name = groupAndB.name)
        )
    }

    @Test
    fun contains() {
        FilterState().apply {
            add(groupAndA, facetA)
            contains(groupAndA, facetA).shouldBeTrue()
            contains(groupAndA, facetB).shouldBeFalse()
            contains(groupAndB, facetA).shouldBeFalse()
        }
    }

    @Test
    fun toggle() {
        FilterState().apply {
            toggle(groupAndA, facetA)
            getFacetGroups() shouldEqual mapOf(groupAndA to setOf(facetA))
            toggle(groupAndA, facetA)
            getFacetGroups().shouldBeEmpty()
        }
    }

    @Test
    fun getFilters() {
        FilterState().apply {
            add(groupAndA, facetA)
            add(groupAndB, facetB)
            add(groupAndA, numeric)
            add(groupAndA, tag)

            getFilters() shouldEqual setOf(facetA, facetB, numeric, tag)
        }
    }
}