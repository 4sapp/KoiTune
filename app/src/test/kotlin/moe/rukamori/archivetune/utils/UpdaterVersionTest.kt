package moe.rukamori.archivetune.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UpdaterVersionTest {
    @Test
    fun selectsNewestCanaryRegardlessOfListOrder() {
        val oldest = canary("N20261006", "2026-10-06T10:00:00Z")
        val newest = canary("N20261008", "2026-10-08T10:00:00Z")
        assertTrue(Updater.findLatestCanaryRelease(listOf(oldest, newest)) == newest)
        assertTrue(Updater.findLatestCanaryRelease(listOf(newest, oldest)) == newest)
    }

    @Test
    fun selectsLatestPublicationForMatchingCanaryTags() {
        val first = canary("N20261008", "2026-10-08T10:00:00Z")
        val second = canary("N20261008", "2026-10-08T12:00:00Z")
        assertTrue(Updater.findLatestCanaryRelease(listOf(first, second)) == second)
        assertTrue(Updater.findLatestCanaryRelease(emptyList()) == null)
    }

    private fun canary(tag: String, publishedAt: String) = ReleaseInfo(
        tagName = tag,
        name = tag,
        body = null,
        publishedAt = publishedAt,
        htmlUrl = "https://github.com/aeee123/KoiTune/releases/tag/$tag",
    )

    @Test
    fun comparesLargeVersionComponentsWithoutOfferingDowngrades() {
        assertTrue(Updater.isUpdateAvailable("2147483649.0.0", "2147483648.0.0"))
        assertFalse(Updater.isUpdateAvailable("2147483648.0.0", "2147483649.0.0"))
        assertFalse(Updater.isUpdateAvailable("1.2147483648.0", "1.2147483649.0"))
        assertFalse(Updater.isUpdateAvailable("1.0.2147483648", "1.0.2147483649"))
        assertTrue(Updater.isSameVersion("v2147483648.0.0", "2147483648.0.0"))
    }

    @Test
    fun comparesNumericPrereleasesBeyondLongRange() {
        assertTrue(
            Updater.isUpdateAvailable(
                "1.0.0-9223372036854775808",
                "1.0.0-9223372036854775807",
            ),
        )
        assertFalse(
            Updater.isUpdateAvailable(
                "1.0.0-9223372036854775807",
                "1.0.0-9223372036854775808",
            ),
        )
        assertTrue(
            Updater.isSameVersion(
                "v1.0.0-9223372036854775808",
                "1.0.0-9223372036854775808",
            ),
        )
    }

    @Test
    fun retainsSemanticVersionPrecedence() {
        assertTrue(Updater.isUpdateAvailable("1.0.0", "1.0.0-999999999999999999999999999999"))
        assertTrue(Updater.isUpdateAvailable("1.0.0-beta", "1.0.0-999999999999999999999999999999"))
        assertTrue(Updater.isUpdateAvailable("1.0.0-beta.10", "1.0.0-beta.2"))
        assertFalse(Updater.isUpdateAvailable("1.0.0-beta.2", "1.0.0-beta.10"))
    }
}
