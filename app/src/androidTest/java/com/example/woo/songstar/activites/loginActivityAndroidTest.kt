package com.example.woo.songstar.activites

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.woo.songstar.R
import com.example.woo.songstar.activities.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class loginActivityAndroidTest {

    @Rule
    @JvmField
    val rule: ActivityTestRule<LoginActivity> = ActivityTestRule(LoginActivity::class.java)
    @Test
    fun userCanLogin() {
        Espresso.onView(ViewMatchers.withId(R.id.etUserName))
            .perform(ViewActions.typeText("admin_user"))
        Espresso.onView(ViewMatchers.withId(R.id.etPassword)).perform(ViewActions.typeText("qwery123$"))
        Espresso.onView(ViewMatchers.withId(R.id.btnLogin)).perform(ViewActions.click())

    }

}
