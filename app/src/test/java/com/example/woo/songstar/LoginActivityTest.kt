package com.example.woo.songstar

import com.example.woo.songstar.activities.LoginActivity
import org.junit.Assert.*
import org.junit.Test

class LoginActivityTest {

    @Test
    fun `empty user returns false`() {
        val result = LoginActivity().fieldsValidation("", "abc1234")
        assertFalse(result)
    }

    @Test
    fun `empty password returns false`() {
        val result = LoginActivity().fieldsValidation("john_doe", "")
        assertFalse(result)
    }

    @Test
    fun `password should have 4 or more characters returns false`() {
        val result = LoginActivity().fieldsValidation("john_doe", "12")
        assertFalse(result)
    }

}