package com.jarvis.assistant.vision

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import java.util.Locale

/**
 * Represents a parsed UI element from the Android Accessibility tree.
 */
data class UiElementInfo(
    val node: AccessibilityNodeInfo? = null,
    val id: String = "",
    val viewId: String = "",
    val className: String = "",
    val text: String = "",
    val contentDescription: String = "",
    val isEditable: Boolean = false,
    val isClickable: Boolean = false,
    val isCheckable: Boolean = false,
    val isChecked: Boolean = false,
    val isEnabled: Boolean = true,
    val bounds: Rect = Rect()
) {
    val primaryLabel: String
        get() = (if (text.isNotBlank()) text else contentDescription).trim()

    val hasTextOrDesc: Boolean
        get() = text.isNotBlank() || contentDescription.isNotBlank()

    fun isSwitchOrToggle(): Boolean {
        val lowerClass = className.lowercase(Locale.ROOT)
        val lowerId = viewId.lowercase(Locale.ROOT)
        val lowerLabel = primaryLabel.lowercase(Locale.ROOT)
        return isCheckable ||
            lowerClass.contains("switch") ||
            lowerClass.contains("toggle") ||
            lowerClass.contains("checkbox") ||
            lowerId.contains("switch") ||
            lowerId.contains("toggle") ||
            lowerLabel.contains("switch") ||
            lowerLabel.contains("toggle")
    }
}

typealias UiElement = UiElementInfo

/**
 * Dual-Anchor UI Grounding Engine.
 * Scans accessibility node hierarchies into structured semantic elements,
 * detects surface types, finds target anchors, and formats summaries for reasoning.
 */
class UiGroundingEngine {

    companion object {
        private const val MAX_ELEMENTS = 120
    }

    fun scanScreen(root: AccessibilityNodeInfo?): List<UiElementInfo> {
        if (root == null) return emptyList()
        val results = mutableListOf<UiElementInfo>()
        traverse(root, results, 0)
        return results
    }

    private fun traverse(node: AccessibilityNodeInfo, list: MutableList<UiElementInfo>, depth: Int) {
        if (list.size >= MAX_ELEMENTS || depth > 30) return

        val rect = Rect()
        node.getBoundsInScreen(rect)

        val text = node.text?.toString()?.trim().orEmpty()
        val desc = node.contentDescription?.toString()?.trim().orEmpty()
        val viewId = node.viewIdResourceName?.substringAfterLast(":id/").orEmpty()
        val className = node.className?.toString()?.substringAfterLast('.').orEmpty()
        val isClickable = node.isClickable
        val isEditable = node.isEditable
        val isCheckable = node.isCheckable
        val isChecked = node.isChecked
        val isEnabled = node.isEnabled

        val isVisible = node.isVisibleToUser && rect.width() > 0 && rect.height() > 0
        if (isVisible && (text.isNotBlank() || desc.isNotBlank() || isClickable || isEditable)) {
            val elementId = if (viewId.isNotBlank()) viewId else "${className}_${list.size}"
            list.add(
                UiElementInfo(
                    node = node,
                    id = elementId,
                    viewId = viewId,
                    className = className,
                    text = text,
                    contentDescription = desc,
                    isEditable = isEditable,
                    isClickable = isClickable,
                    isCheckable = isCheckable,
                    isChecked = isChecked,
                    isEnabled = isEnabled,
                    bounds = rect
                )
            )
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            traverse(child, list, depth + 1)
        }
    }

    fun formatElementsSummary(elements: List<UiElementInfo>, root: AccessibilityNodeInfo? = null): String {
        val pkg = root?.packageName?.toString() ?: ""
        val sb = StringBuilder()
        if (pkg.isNotBlank()) {
            sb.append("[Active App: ").append(pkg).append("]\n")
        }
        if (elements.isEmpty()) {
            sb.append("No interactive UI elements detected.")
            return sb.toString()
        }

        elements.take(35).forEachIndexed { idx, el ->
            val label = el.primaryLabel
            val type = when {
                el.isEditable -> "Input"
                el.isCheckable -> if (el.isChecked) "CheckedBox" else "UncheckedBox"
                el.isClickable -> "Button"
                else -> "Text"
            }
            sb.append("${idx + 1}. [$type] ")
            if (label.isNotBlank()) {
                sb.append("\"").append(label.take(60)).append("\" ")
            }
            if (el.viewId.isNotBlank()) {
                sb.append("id=${el.viewId} ")
            }
            sb.append("bounds=(${el.bounds.left},${el.bounds.top}-${el.bounds.right},${el.bounds.bottom})\n")
        }
        return sb.toString().trim()
    }

    fun isGoogleFlowSurface(elements: List<UiElementInfo>): Boolean {
        val allText = elements.joinToString(" ") { it.primaryLabel }.lowercase(Locale.ROOT)
        return allText.contains("google") &&
            (allText.contains("sign in") || allText.contains("verify") || allText.contains("account") || allText.contains("security"))
    }

    fun isGoogleSearchResults(elements: List<UiElementInfo>): Boolean {
        val anySearchResult = elements.any { el ->
            !el.isEditable && (el.viewId.contains("result", ignoreCase = true) || el.bounds.top > 250) &&
                el.primaryLabel.length > 15
        }
        return anySearchResult
    }

    fun extractScreenContext(root: AccessibilityNodeInfo?, elements: List<UiElementInfo>): Pair<String, String> {
        val pkg = root?.packageName?.toString().orEmpty()
        val titleCandidate = elements.firstOrNull { el ->
            !el.isEditable && el.bounds.top < 300 && el.primaryLabel.isNotBlank() && el.primaryLabel.length in 3..40
        }?.primaryLabel.orEmpty()
        return pkg to titleCandidate
    }

    fun findTargetElement(
        elements: List<UiElementInfo>,
        targetName: String,
        isToggleIntent: Boolean = false,
        elementId: Any? = null
    ): UiElementInfo? {
        if (elementId != null) {
            val idStr = elementId.toString()
            val byId = elements.firstOrNull {
                it.id.equals(idStr, ignoreCase = true) ||
                it.viewId.equals(idStr, ignoreCase = true) ||
                (elementId is Number && elements.indexOf(it) == elementId.toInt())
            }
            if (byId != null) return byId
        }

        val targetClean = targetName.trim().lowercase(Locale.ROOT)
        if (targetClean.isBlank()) return null

        // 1. Exact match
        val exact = elements.firstOrNull { it.primaryLabel.lowercase(Locale.ROOT) == targetClean }
        if (exact != null) return exact

        // 2. Starts with / Contains
        val contains = elements.firstOrNull {
            val label = it.primaryLabel.lowercase(Locale.ROOT)
            label.contains(targetClean) || targetClean.contains(label)
        }
        if (contains != null) return contains

        // 3. Token match
        val tokens = targetClean.split(Regex("[^\\p{L}\\p{N}]+")).filter { it.length > 2 }
        if (tokens.isNotEmpty()) {
            val tokenMatch = elements.firstOrNull { el ->
                val label = el.primaryLabel.lowercase(Locale.ROOT)
                tokens.any { label.contains(it) }
            }
            if (tokenMatch != null) return tokenMatch
        }

        // 4. Toggle specific
        if (isToggleIntent) {
            val toggle = elements.firstOrNull { it.isCheckable }
            if (toggle != null) return toggle
        }

        return null
    }
}
