package kr.ac.kumoh.ce.s20190757.s23w04carddealer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kr.ac.kumoh.ce.s20190757.s23w04carddealer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var main: ActivityMainBinding
    private lateinit var model: CardDealerViewModel

    // 포커 족보를 저장하는 string 배열
    private var pokerRank = arrayOf<String>("High Card", "One Pair", "Two Pair", "Three of A Kind", "Straight",
                                            "Back Straight", "Mountain", "Flush", "Full House", "Four of A Kind",
                                            "Straight Flush", "Back Straight Flush", "Royal Straight Flush")

    // 카드 값을 저장하는 배열
    private var cardArr = IntArray(5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main = ActivityMainBinding.inflate(layoutInflater)
        setContentView(main.root)

        // model 변수에 CardDealerViewModel 클래스 할당
        model = ViewModelProvider(this)[CardDealerViewModel::class.java]
        model.cards.observe(this, Observer {
            val res = IntArray(5)
            for (i in res.indices) {
                cardArr[i] = model.cards.value!![i]     // model.shuffle()이 호출될 때,
                res[i] = resources.getIdentifier(
                    getCardName(model.cards.value!![i]),
                    "drawable",
                    packageName
                )
            }

            var cardID = arrayOf(main.card1, main.card2, main.card3, main.card4, main.card5)
            for (i in 0 until 5) {
                cardID[i].setImageResource(res[i])
            }
            main.rank.text = updatePokerRank(cardArr)
        })

        main.btnShuffle.setOnClickListener {
            model.shuffle()
        }
    }

    private fun getCardName(c: Int): String {
        // integer 값이 -1인 경우, 레드 조커를 출력
        if (c == -1) return "c_red_joker"

        var shape = when (c / 13) {
            0 -> "spades"
            1 -> "diamonds"
            2 -> "hearts"
            3 -> "clubs"
            else -> "errors"
        }

        val number = when (c % 13) {
            0 -> "ace"
            in 1..9 -> (c % 13 + 1).toString()
            10 -> {
                shape = shape.plus("2")
                "jack"
            }
            11 -> {
                shape = shape.plus("2")
                "queen"
            }
            12 -> {
                shape = shape.plus("2")
                "king"
            }
            else -> "errors"
        }
        return "c_${number}_of_${shape}"
    }

    private fun updatePokerRank(c: IntArray): String {
        if (c[0] == -1) return "Hand Ranking"

        var shape: Array<Int> = Array(4) { 0 }
        var number: Array<Int> = Array(13) { 0 }

        // 문양과 숫자의 개수 판단
        for (i in c.indices) {
            shape[cardArr[i] / 13]++
            number[cardArr[i] % 13]++
        }

        var answer: String = checkPokerRank(shape, number)
        return answer
    }

    private fun checkPokerRank(shape: Array<Int>, number: Array<Int>): String {
        var answer: String = ""
        var isFlush: Boolean = false
        var isStraight: Array<Boolean> = Array(3) { false } // 0 : Straight, 1 : Back, 2 : Mountain or Royal
        var isOnePair: Boolean = false
        var isThree: Boolean = false

        // Flush 여부 판단
        for (i in 0 .. 3) {
            if (shape[i] == 5) isFlush = true
        }

        // Staright 여부 판단
        for (i in 1 .. 8) {
            if (number[i] == 1 && number[i + 1] == 1&& number[i + 2] == 1 && number[i + 3] == 1 && number[i + 4] == 1)
                isStraight[0] = true
            else if (number[0] == 1 && number[1] == 1 && number[2] == 1 && number[3] == 1 && number[4] == 1 )
                isStraight[1] = true
            else if (number[0] == 1 && number[9] == 1 && number[10] == 1 && number[11] == 1 && number[12] == 1)
                isStraight[2] = true
        }

        if (isFlush && isStraight[2]) answer = pokerRank[12]             // Royal Straight Flush
        else if (isFlush && isStraight[1]) answer = pokerRank[11]        // Back Straight Flush
        else if (isFlush && isStraight[0]) answer = pokerRank[10]        // Straight Flush
        else if (isStraight[2]) answer = pokerRank[6]                    // Mountain
        else if (isStraight[1]) answer = pokerRank[5]                    // Back Straight
        else if (isStraight[0]) answer = pokerRank[4]                    // Straight
        else if (isFlush) answer = pokerRank[7]                          // Flush

        for (i in 0 .. 12) {
            if (number[i] == 4) answer = pokerRank[9]                    // Four of A Kind
            else if (number[i] == 3) {                                   // Three of A Kind
                answer = pokerRank[3]
                isThree = true
            }
            else if (number[i] == 2 && isOnePair) answer = pokerRank[2]  // Two Pair
            else if (number[i] == 2) {                                   // One Pair
                answer = pokerRank[1]
                isOnePair = true
            }
        }

        if (isOnePair && isThree) answer = pokerRank[8]                  // Full House
        else if (answer == "") answer = pokerRank[0]                     // High Card

        return answer
    }
}