package com.example.woo.songstar

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.woo.songstar.activities.ArtistsActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArtistActivityAndroidTest {
    @Rule
    @JvmField
    val rule: ActivityTestRule<ArtistsActivity> = ActivityTestRule(ArtistsActivity::class.java)
    @Test
    fun userCanType() {
        Espresso.onView(ViewMatchers.withId(R.id.svSearchSongs))
            .perform(ViewActions.typeText("abc"))
    }
}