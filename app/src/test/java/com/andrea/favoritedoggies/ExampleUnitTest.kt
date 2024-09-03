package com.andrea.favoritedoggies

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
//
//Essential unit tests
//When following best practice, you should ensure you use unit tests in the following cases:
//
//Unit tests for ViewModels, or presenters.
//Unit tests for the data layer, especially repositories. Most of the data layer should be platform-independent. Doing so enables test doubles to replace database modules and remote data sources in tests. See the guide on using test doubles in Android
//Unit tests for other platform-independent layers such as the Domain layer, as with use cases and interactors.
//Unit tests for utility classes such as string manipulation and math.
//Testing Edge Cases
//Unit tests should focus on both normal and edge cases. Edge cases are uncommon scenarios that human testers and larger tests are unlikely to catch. Examples include the following:
//
//Math operations using negative numbers, zero, and boundary conditions.
//All the possible network connection errors.
//Corrupted data, such as malformed JSON.
//Simulating full storage when saving to a file.
//Object recreated in the middle of a process (such as an activity when the device is rotated).
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}