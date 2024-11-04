package com.example.opsc7312poe

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.test.core.app.ApplicationProvider
import com.example.opsc7312poe.utils.LocaleHelper
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.*

class LocaleHelperTest {

    private lateinit var localeHelper: LocaleHelper
    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)  // Initialize mocks
        localeHelper = LocaleHelper()
        context = ApplicationProvider.getApplicationContext()

        // Create a mock for SharedPreferences and its editor
        sharedPreferences = Mockito.mock(SharedPreferences::class.java)
        editor = Mockito.mock(SharedPreferences.Editor::class.java)

        // Mock SharedPreferences behavior
        `when`(context.getSharedPreferences("LanguagePrefs", Context.MODE_PRIVATE)).thenReturn(sharedPreferences)
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString(Mockito.anyString(), Mockito.anyString())).thenReturn(editor)

        // Mock Resources if needed
        val resources = Mockito.mock(Resources::class.java)
        `when`(context.resources).thenReturn(resources)
    }

    @Test
    fun `test setLocale persists language`() {
        // Act
        localeHelper.setLocale(context, "fr")

        // Assert - Verify that the language is saved in SharedPreferences
        Mockito.verify(editor).putString("Locale.Helper.Selected.Language", "fr")
        Mockito.verify(editor).apply()
    }

    @Test
    fun `test getPersistedLanguage retrieves language from SharedPreferences`() {
        // Arrange - Set up SharedPreferences to return "es" for the selected language
        `when`(sharedPreferences.getString("Locale.Helper.Selected.Language", Locale.getDefault().language)).thenReturn("es")

        // Act
        val language = localeHelper.getPersistedLanguage(context)

        // Assert
        assertEquals("es", language)
    }

    @Test
    fun `test setLocale updates resources for Android N and above`() {
        // Act
        val resultContext = localeHelper.setLocale(context, "de")

        // Assert - Check the context configuration for the new locale
        val config = resultContext.resources.configuration
        assertEquals("de", config.locales[0].language)
    }

    @Test
    fun `test setLocale updates resources for Android below N`() {
        // Act
        val resultContext = localeHelper.setLocale(context, "ja")

        // Assert - Check the context configuration for the new locale
        val config = resultContext.resources.configuration
        assertEquals("ja", config.locale.language)
    }
}
