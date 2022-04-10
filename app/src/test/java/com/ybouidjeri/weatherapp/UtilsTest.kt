package com.ybouidjeri.weatherapp

import org.junit.Assert
import org.junit.Test

class UtilsTest {

    @Test
    fun getCitiesList() {
        val expected = 10
        val cities = Utils.getListOfCities()
        val actual = cities.size
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun testWindDirection() {
        var expected = "N"
        var actual = Utils.getWindDirection(0.0)
        Assert.assertEquals(expected, actual)

        expected = "E"
        actual = Utils.getWindDirection(90.0)
        Assert.assertEquals(expected, actual)

        expected = "N"
        actual = Utils.getWindDirection(360.0)
        Assert.assertEquals(expected, actual)

        expected = "NW"
        actual = Utils.getWindDirection(320.0)
        Assert.assertEquals(expected, actual)
    }
}