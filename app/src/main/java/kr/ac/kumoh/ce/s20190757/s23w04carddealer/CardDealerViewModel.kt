package kr.ac.kumoh.ce.s20190757.s23w04carddealer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class CardDealerViewModel : ViewModel() {
    private var _cards = MutableLiveData<IntArray>(IntArray(5) { -1})
    val cards: LiveData<IntArray>
        get() = _cards

    fun shuffle() {
        var num = 0
        val newCards = IntArray(5) { -1 }

        for (i in newCards.indices) {
            do {
                num = Random.nextInt(52)
            } while (num in newCards)
            newCards[i] = num
        }
        newCards.sort()
        _cards.value = newCards // LiveData에선 옵저버가 관측을 해야 한다. 따라서 MutableLiveData의 value를 수정하면 관측된다.
    }
}