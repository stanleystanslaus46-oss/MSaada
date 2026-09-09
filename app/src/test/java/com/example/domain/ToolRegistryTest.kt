package com.example.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ToolRegistryTest {
    @Test
    fun registry_has_unique_tool_ids_and_routes() {
        val ids = ToolRegistry.tools.map { it.id }
        val routes = ToolRegistry.tools.map { it.route }

        assertEquals(ids.size, ids.toSet().size)
        assertEquals(routes.size, routes.toSet().size)
        assertTrue(ToolRegistry.tools.isNotEmpty())
    }

    @Test
    fun every_tool_references_an_existing_category() {
        val categoryIds = ToolRegistry.categories.map { it.id }.toSet()
        assertTrue(ToolRegistry.tools.all { it.categoryId in categoryIds })
    }

    @Test
    fun tool_metadata_is_release_safe() {
        assertTrue(ToolRegistry.tools.all { it.route.isNotBlank() })
        assertTrue(ToolRegistry.tools.all { it.nameSw.isNotBlank() && it.nameEn.isNotBlank() })
        assertTrue(ToolRegistry.tools.all { it.descSw.isNotBlank() && it.descEn.isNotBlank() })
        assertFalse(ToolRegistry.tools.any { it.id.isBlank() })
    }
}
