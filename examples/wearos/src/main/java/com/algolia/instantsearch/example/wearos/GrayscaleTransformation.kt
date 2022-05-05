package com.algolia.instantsearch.example.wearos

import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import coil.size.Size
import coil.transform.Transformation

/**
 * A [Transformation] that converts an image to shades of gray.
 */
internal class GrayscaleTransformation(greyScale: Float) : Transformation {

    private val grayColorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(greyScale) })

    override val cacheKey: String = GrayscaleTransformation::class.java.name

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply { colorFilter = grayColorFilter }
        return createBitmap(input.width, input.height, input.config ?: Bitmap.Config.ARGB_8888).applyCanvas {
            drawBitmap(input, 0f, 0f, paint)
        }
    }

    override fun equals(other: Any?) = other is GrayscaleTransformation

    override fun hashCode() = javaClass.hashCode()

    override fun toString() = "GrayscaleTransformation()"
}
