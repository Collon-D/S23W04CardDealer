package kr.ac.kumoh.ce.s20190757.s23w04carddealer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.ac.kumoh.ce.s20190757.s23w04carddealer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var main: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main = ActivityMainBinding.inflate(layoutInflater)
        setContentView(main.root)
        main.card1.setImageResource(R.drawable.c_2_of_spades)

        val res = resources.getIdentifier(
            // TODO: 하드 코딩 제거할 것
            getCardName(37),
            "drawable",
            packageName
        )

        main.card1.setImageResource(res)
    }

    private fun getCardName(c: Int): String {
        val shape = when (c / 13) {
            0 -> "spades"
            1 -> "diamonds"
            2 -> "hearts"
            3 -> "clubs"
            else -> "errors"
        }

        val number = when (c % 13) {
            0 -> "ace"
            in 1..9 -> (c % 13 + 1).toString()
            10 -> "jack"
            11 -> "queen"
            12 -> "king"
            else -> "errors"
        }

        return if (number in arrayOf("jack", "queen", "king"))
            "c_${number}_of_${shape}2"
        else
            "c_${number}_of_${shape}"
    }
}