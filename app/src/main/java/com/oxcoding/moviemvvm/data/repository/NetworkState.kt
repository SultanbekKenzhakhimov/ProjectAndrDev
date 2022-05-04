package com.oxcoding.moviemvvm.data.repository

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED

}

class SostoyanieSeti(val status: Status, val msg: String) {

    companion object {

        val LOADED: SostoyanieSeti
        val LOADING: SostoyanieSeti
        val ERROR: SostoyanieSeti
        val ENDOFLIST: SostoyanieSeti

        init {
            LOADED = SostoyanieSeti(Status.SUCCESS, "Успешно загружено")

            LOADING = SostoyanieSeti(Status.RUNNING, "Идет загрузка.....")

            ERROR = SostoyanieSeti(Status.FAILED, "Проблемы с сервером или с вашим интернетом")

            ENDOFLIST = SostoyanieSeti(Status.FAILED, "Все.....больше фильмов нет")
        }
    }
}