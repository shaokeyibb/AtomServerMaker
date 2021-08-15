package io.hikarilan.atomservermaker.utils

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

object MessageDigestHelper {
    fun encodeToMD5(file: File): String {
        return BigInteger(1, MessageDigest.getInstance("MD5").digest(file.readBytes())).toString(16)
    }

    fun encodeToSHA256(file: File): String {
        return BigInteger(1, MessageDigest.getInstance("SHA256").digest(file.readBytes())).toString(16)
    }

    fun encodeToSHA1(file: File): String {
        return BigInteger(1, MessageDigest.getInstance("SHA1").digest(file.readBytes())).toString(16)
    }
}