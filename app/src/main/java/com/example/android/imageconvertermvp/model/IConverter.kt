package com.example.android.imageconvertermvp.model

import io.reactivex.rxjava3.core.Completable

interface IConverter {
    fun convertImage(path: String): Completable
}