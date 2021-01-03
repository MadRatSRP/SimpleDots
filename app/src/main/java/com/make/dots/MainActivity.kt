package com.make.dots

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.make.dots.databinding.ActivityMainBinding
import com.make.dots.databinding.ListImageIdsBinding

class MainActivity : AppCompatActivity() {
    // ViewBinding
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ViewBinding initialization
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    
        val imagesList: ArrayList<Int> = ArrayList()
        imagesList.add(R.drawable.one)
        imagesList.add(R.drawable.two)
        imagesList.add(R.drawable.three)
        imagesList.add(R.drawable.four)
        imagesList.add(R.drawable.five)

        binding.imageViewPager.adapter = ImagesAdapter(imagesList)
        binding.dotsIndicator.setViewPager(binding.imageViewPager)
    }

    class ImagesAdapter(private val listOfImageIds: ArrayList<Int>)
        : RecyclerView.Adapter<ImagesAdapter.ImagesHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesHolder {
            val binding = ListImageIdsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ImagesHolder(binding)
        }
    
        override fun onBindViewHolder(holder: ImagesHolder, position: Int)
            = holder.bind(listOfImageIds[position])
    
        override fun getItemCount(): Int
            = listOfImageIds.size
    
        class ImagesHolder(private val binding: ListImageIdsBinding)
            : RecyclerView.ViewHolder(binding.root) {
            fun bind(imageId: Int) {
                binding.imageView.setImageResource(imageId)
            }
        }
    }
}
