package com.h3110w0r1d.gitmoji

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import java.awt.FlowLayout
import java.awt.GridLayout
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class GitmojiConfig constructor(private val project: Project) : SearchableConfigurable {
    private val mainPanel = JPanel(GridLayout(20, 2))
    private val pci = PropertiesComponent.getInstance()

    private val renderCommitLog = JCheckBox()
    private val useUnicode = JCheckBox()
    private val displayEmoji = JCheckBox()
    private val insertInCursorPosition = JCheckBox()
    private val includeGitmojiDescription = JCheckBox()
    private val afterEmojiText = JLabel()
    private val afterEmojiOptions = arrayOf("<nothing>", "<space>", ":", "(", "_", "[", "-")
    private val afterEmoji = ComboBox(afterEmojiOptions)
    private val languageText = JLabel()
    private val languageOptions = arrayOf("English", "简体中文")
    private val language = ComboBox(languageOptions)

    private fun languageOpt2Cfg(languageOpt: String): String {
        return when (languageOpt) {
            "English" -> "en"
            "简体中文" -> "zh-CN"
            else -> "en"
        }
    }
    private fun languageCfg2Opt(languageOpt: String): String {
        return when (languageOpt) {
            "en" -> "English"
            "zh-CN" -> "简体中文"
            else -> "English"
        }
    }

    private fun afterEmojiOpt2Cfg(afterEmojiOpt: String): String {
        return when (afterEmojiOpt) {
            "<nothing>" -> ""
            "<space>" -> " "
            else -> afterEmojiOpt
        }
    }

    private fun afterEmojiCfg2Opt(afterEmojiCfg: String): String {
        return when (afterEmojiCfg) {
            "" -> "<nothing>"
            " " -> "<space>"
            else -> afterEmojiCfg
        }
    }

    private fun setLanguage() {
        val language = languageOptions[language.selectedIndex]
        if (language == "English") {
            renderCommitLog.text = "Render commit log (replace text to emoji, need restart)"
            useUnicode.text = "Use unicode emoji instead of text version (:code:)"
            displayEmoji.text = "Display emoji instead of icon in list (Bug in IntelliJ Windows or emoji in black and white)"
            insertInCursorPosition.text = "Insert the emoji in the cursor location"
            includeGitmojiDescription.text = "Include gitmoji description"
            afterEmojiText.text = "After emoji:"
            languageText.text = "Language"
        } else if (language == "简体中文") {
            renderCommitLog.text = "渲染Commit日志中的文本Emoji（替换文本为Unicode表情，重启生效）"
            useUnicode.text = "使用 Unicode 表情而不是文本版本（:code:）"
            displayEmoji.text = "在列表中显示表情而不是图标（IntelliJ Windows 中的错误或黑白表情）"
            insertInCursorPosition.text = "在光标位置插入Emoji"
            includeGitmojiDescription.text = "插入Gitmoji描述"
            afterEmojiText.text = "分隔符:"
            languageText.text = "语言:"
        }
    }
    init {
        mainPanel.add(renderCommitLog)
        mainPanel.add(useUnicode)
        mainPanel.add(displayEmoji)
        mainPanel.add(insertInCursorPosition)
        mainPanel.add(includeGitmojiDescription)

        val panel1 = JPanel(FlowLayout(FlowLayout.LEFT))
        panel1.add(afterEmojiText)
        panel1.add(afterEmoji)
        mainPanel.add(panel1)

        val panel2 = JPanel(FlowLayout(FlowLayout.LEFT))
        panel2.add(languageText)
        panel2.add(language)
        mainPanel.add(panel2)
    }

    override fun isModified(): Boolean {
        return renderCommitLog.isSelected != pci.getBoolean(CONFIG_RENDER_COMMIT_LOG, true) ||
                displayEmoji.isSelected != pci.getBoolean(CONFIG_DISPLAY_EMOJI, true) ||
                useUnicode.isSelected != pci.getBoolean(CONFIG_USE_UNICODE, false) ||
                insertInCursorPosition.isSelected != pci.getBoolean(CONFIG_INSERT_IN_CURSOR_POSITION, false) ||
                includeGitmojiDescription.isSelected != pci.getBoolean(CONFIG_INCLUDE_GITMOJI_DESCRIPTION, false) ||
                afterEmojiOptions[afterEmoji.selectedIndex] != afterEmojiCfg2Opt(pci.getValue(CONFIG_AFTER_EMOJI, " ")) ||
                languageOptions[language.selectedIndex] != languageCfg2Opt(pci.getValue(CONFIG_LANGUAGE, "English"))
    }
    override fun getDisplayName(): String = "Gitmoji"
    override fun getId(): String = "com.h3110w0r1d.gitmoji.config"

    override fun apply() {
        pci.setValue(CONFIG_RENDER_COMMIT_LOG, renderCommitLog.isSelected.toString())
        pci.setValue(CONFIG_DISPLAY_EMOJI, displayEmoji.isSelected.toString())
        pci.setValue(CONFIG_USE_UNICODE, useUnicode.isSelected.toString())
        pci.setValue(CONFIG_INSERT_IN_CURSOR_POSITION, insertInCursorPosition.isSelected.toString())
        pci.setValue(CONFIG_INCLUDE_GITMOJI_DESCRIPTION, includeGitmojiDescription.isSelected.toString())
        pci.setValue(CONFIG_AFTER_EMOJI, afterEmojiOpt2Cfg(afterEmojiOptions[afterEmoji.selectedIndex]))
        pci.setValue(CONFIG_LANGUAGE, languageOpt2Cfg(languageOptions[language.selectedIndex]))
        setLanguage()
    }

    override fun reset() {
        renderCommitLog.isSelected = pci.getBoolean(CONFIG_RENDER_COMMIT_LOG, true)
        displayEmoji.isSelected = pci.getBoolean(CONFIG_DISPLAY_EMOJI, true)
        useUnicode.isSelected = pci.getBoolean(CONFIG_USE_UNICODE, false)
        insertInCursorPosition.isSelected = pci.getBoolean(CONFIG_INSERT_IN_CURSOR_POSITION, false)
        includeGitmojiDescription.isSelected = pci.getBoolean(CONFIG_INCLUDE_GITMOJI_DESCRIPTION, false)
        afterEmoji.selectedIndex = if (
            afterEmojiOptions.indexOf(afterEmojiCfg2Opt(pci.getValue(CONFIG_AFTER_EMOJI, " "))) != -1
            ) afterEmojiOptions.indexOf(afterEmojiCfg2Opt(pci.getValue(CONFIG_AFTER_EMOJI, " "))) else 0
        language.selectedIndex = if (
            languageOptions.indexOf(languageCfg2Opt(pci.getValue(CONFIG_LANGUAGE, "en"))) != -1
            ) languageOptions.indexOf(languageCfg2Opt(pci.getValue(CONFIG_LANGUAGE, "en"))) else 0
        setLanguage()
    }

    override fun createComponent(): JComponent = mainPanel
}
