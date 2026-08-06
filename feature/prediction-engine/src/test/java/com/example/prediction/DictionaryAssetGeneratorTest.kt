package com.example.prediction

import com.example.prediction.builtin.DawgBuilder
import com.example.prediction.builtin.DawgIndex
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

/**
 * Dictionary pipeline (Phase 1): ingests the open word-frequency data under
 * /tools/dictionary-builder/data (hermitdave/FrequencyWords, Apache-2.0) and writes
 * the compiled DAWG binaries into /app/src/main/assets/. The app never depends on
 * Python or raw data at runtime — only the compiled binaries ship. This test is the
 * build step that regenerates the assets, and it self-verifies: word count, real-word
 * spot checks and measured load time are asserted right here.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DictionaryAssetGeneratorTest {

    private val dataDir = File("../../tools/dictionary-builder/data")
    private val assetsDir = File("../../app/src/main/assets")

    private fun generate(languageFile: String, assetName: String, isBangla: Boolean, minWords: Int, spotChecks: List<String>) {
        val source = File(dataDir, languageFile)
        assertTrue("missing data file: ${source.absolutePath}", source.exists())
        val words = DawgBuilder.parseWordList(source.readLines(), isBangla)
        assertTrue("expected >= $minWords words in $languageFile, got ${words.size}", words.size >= minWords)

        val bytes = DawgBuilder.build(words)
        assetsDir.mkdirs()
        File(assetsDir, assetName).writeBytes(bytes)

        val index = DawgIndex()
        val start = System.nanoTime()
        index.load(bytes)
        val loadMs = (System.nanoTime() - start) / 1_000_000

        println("ASSET $assetName: ${index.wordCount} words, ${bytes.size} bytes, load ${loadMs}ms")
        assertTrue("asset must parse under 2s", loadMs < 2000)
        for (check in spotChecks) {
            assertTrue("spot check failed: '$check' must exist in $assetName", index.contains(check))
        }
    }

    @Test
    fun `generate english and bangla dawg assets`() {
        generate(
            languageFile = "en.txt",
            assetName = "dictionary_en.dawg",
            isBangla = false,
            minWords = 10_000,
            spotChecks = listOf("receive", "believe", "definitely", "environment", "good", "morning", "night", "luck", "job", "keyboard")
        )
        generate(
            languageFile = "bn.txt",
            assetName = "dictionary_bn.dawg",
            isBangla = true,
            minWords = 5_000,
            spotChecks = listOf("বাংলা", "ভালো", "আমি", "কী")
        )
    }
}
