package com.example.anivault.data.`object`



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object SharedViewModel {
    private val _selectedSeason = MutableLiveData<Pair<Int, String>>()
    val selectedSeason: LiveData<Pair<Int, String>> = _selectedSeason

    fun setSelectedSeason(year: Int, season: String) {
        _selectedSeason.value = Pair(year, season)
    }
}