package kr.ac.kumoh.ce.s20190757.s23w04carddealer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kr.ac.kumoh.ce.s20190757.s23w04carddealer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var main: ActivityMainBinding
    private lateinit var model: CardDealerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main = ActivityMainBinding.inflate(layoutInflater)
        setContentView(main.root)
        //main.card1.setImageResource(R.drawable.c_2_of_spades)

        // model 변수에 CardDealerViewModel 클래스 할당
        model = ViewModelProvider(this)[CardDealerViewModel::class.java]
        model.cards.observe(this, Observer {
            val res = IntArray(5)
            for (i in res.indices) {
                res[i] = resources.getIdentifier(
                    getCardName(model.cards.value!![0]),
                    "drawable",
                    packageName
                )
            }
            main.card1.setImageResource(res[0])
        })

        main.btnShuffle.setOnClickListener {
            model.shuffle()
        }
    }

    private fun getCardName(c: Int): String {
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
}