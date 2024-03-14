package dev.cidick.sweetbobo.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.cidick.sweetbobo.R
import dev.cidick.sweetbobo.databinding.SpinnerItemBinding

class MainAdapter: RecyclerView.Adapter<MainAdapter.AdvHolder>() {
    private val slot = intArrayOf(1, 2, 3, 4, 5, 6, 7)
    private val combi = 7
    inner class AdvHolder(val spin: SpinnerItemBinding) : RecyclerView.ViewHolder(spin.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val spinBind = SpinnerItemBinding.inflate(layoutInflater, parent, false)
        return AdvHolder(spinBind)
    }

    override fun onBindViewHolder(holder: AdvHolder, position: Int) {
        val i = if (position < 7) position else position % combi
        with(holder.spin){
            when (slot[i]) {
                1 -> fruits.setImageResource(R.drawable.combination_1)
                2 -> fruits.setImageResource(R.drawable.combination_2)
                3 -> fruits.setImageResource(R.drawable.combination_3)
                4 -> fruits.setImageResource(R.drawable.combination_4)
                5 -> fruits.setImageResource(R.drawable.combination_5)
                6 -> fruits.setImageResource(R.drawable.combination_6)
                7 -> fruits.setImageResource(R.drawable.combination_7)
            }
        }
     
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

}