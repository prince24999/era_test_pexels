package com.vp.era_test_pexels.control

import com.vp.era_test_pexels.model.Photo
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun encodeUrl(input: String): String {
    var temp = input.filter { !it.isWhitespace() }
    return URLEncoder.encode(temp, StandardCharsets.UTF_8.toString()) // Use UTF-8 encoding

}

fun getOrientation(orientation: String): String
{
    return when (orientation)
    {
        "landscape" -> "landscape"
        "portrait" -> "portrait"
        "square" -> "square"
        else -> "landscape"
    }
}

fun getSize(size: String): String
{
    return when (size)
    {
        "large" -> "large"
        "medium" -> "medium"
        "small" -> "small"
        else -> "medium"
    }
}

fun getColor(color: String): String
{
    return when (color)
    {
        "red" -> "large"
        "orange" -> "orange"
        "yellow" -> "yellow"
        "green" -> "green"
        "turquoise" -> "turquoise"
        "blue" -> "blue"
        "violet" -> "violet"
        "pink" -> "pink"
        "brown" -> "brown"
        "black" -> "black"
        "gray" -> "gray"
        "white" -> "white"
        else -> ""
    }
}

fun getPageNumber(pageNumber: String): Int
{
    return pageNumber.toIntOrNull() ?: 1 // if null return 1
}

fun getPhotosPerPage(perPageText: Float?): Int
{
    return perPageText?.toInt() ?: 15 // if null, return 15
}

fun calculateOrientationAndSizeOfPhoto(photoInput: Photo, orientation: String, size: String): String
{
    var result: String = ""

    if (orientation == "landscape")
    {
        result = photoInput.src.landscape
    }
    else if (orientation == "portrait")
    {
        result = photoInput.src.portrait
    }
    else
    {
        result = photoInput.src.original
    }

    return result
}







