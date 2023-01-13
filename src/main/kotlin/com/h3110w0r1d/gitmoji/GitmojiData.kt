package com.h3110w0r1d.gitmoji

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

const val PLUGIN_ID = "com.h3110w0r1d.gitmoji"

const val CONFIG_RENDER_COMMIT_LOG: String = "$PLUGIN_ID.render-commit-log"
// 使用Unicode而不是文本版本（：code：）
const val CONFIG_USE_UNICODE: String = "$PLUGIN_ID.use-unicode"
// 显示表情而不是列表中的图标（IntelliJ Windows中的错误或表情以黑白显示）
const val CONFIG_DISPLAY_EMOJI: String = "$PLUGIN_ID.display-emoji"
// Emoji之后的字符
const val CONFIG_AFTER_EMOJI: String = "$PLUGIN_ID.text-after-emoji"
// 在光标位置插入
const val CONFIG_INSERT_IN_CURSOR_POSITION: String = "$PLUGIN_ID.insert-in-cursor-position"
// 包括gitmoji描述
const val CONFIG_INCLUDE_GITMOJI_DESCRIPTION: String = "$PLUGIN_ID.include-gitmoji-description"
// 语言
const val CONFIG_LANGUAGE: String = "$PLUGIN_ID.language"


data class GitmojiData(val code: String, val emoji: String, val description: String) {
    private lateinit var _icon: Icon

    fun getIcon(): Icon {
        if (!this::_icon.isInitialized) {
            _icon = try {
                IconLoader.findIcon(
                    "/icons/gitmoji/" + code.replace(":", "") + ".png",
                    GitmojiData::class.java,
                    false,
                    true
                )!!
            } catch (e: Exception) {
                IconLoader.getIcon("/icons/gitmoji/anguished.png", GitmojiData::class.java)
            }
        }
        return _icon
    }
}
