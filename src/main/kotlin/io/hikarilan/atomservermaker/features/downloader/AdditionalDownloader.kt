package io.hikarilan.atomservermaker.features.downloader

abstract class AdditionalDownloader<T>(private val downloadAdditionalResource: (T) -> Array<Downloader.FilePackage>) {

    abstract val name: String
    abstract val description: String

    abstract fun available(): Boolean

    abstract val supportCheck: Boolean
    abstract fun check(files: Array<Downloader.FilePackage>): Array<Downloader.FilePackage>?

    abstract val util: T

    fun download(): Array<Downloader.FilePackage> {
        return downloadAdditionalResource.invoke(util)
    }
}