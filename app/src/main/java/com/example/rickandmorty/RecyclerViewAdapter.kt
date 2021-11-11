package com.example.rickandmorty


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RecyclerViewAdapter(private val data: ArrayList<Results>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image = itemView.findViewById<ImageView>(R.id.imgItem)
        val tvItemName = itemView.findViewById<TextView>(R.id.tvItemName)
        val tvItemStatus = itemView.findViewById<TextView>(R.id.tvItemStatus)
        val tvItemSpecies = itemView.findViewById<TextView>(R.id.tvItemSpecies)
        val tvItemGender = itemView.findViewById<TextView>(R.id.tvItemGender)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Picasso.get().load(this.data[position].image).into(holder.image)
        holder.tvItemName.text = this.data[position].name
        holder.tvItemStatus.text = "Status: ${this.data[position].status}"
        holder.tvItemSpecies.text = "Species: ${this.data[position].species}"
        holder.tvItemGender.text = "Gender: ${this.data[position].gender}"
    }

    override fun getItemCount() = this.data.size

    fun addCharacter(results: List<Results>) {
        this.data.apply {
            clear()
            addAll(results)
        }
    }


}
