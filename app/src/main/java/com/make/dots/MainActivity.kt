package com.make.dots

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewpager.widget.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.make.dots.databinding.ActivityMainBinding
import com.make.dots.databinding.ImageItemBinding

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
        binding.imageViewPager.adapter?.registerDataSetObserver(
            binding.dotsIndicator.dataSetObserver)
    }

    class ImagesAdapter(private val media: ArrayList<Int>) : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object` as View

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val binding = ImageItemBinding.inflate(
                LayoutInflater.from(container.context), container, false
            )
            val parentRoot = binding.root
            
            binding.imageView.setImageResource(media[position])
            container.addView(parentRoot)
            
            return parentRoot
        }

        override fun getCount(): Int = media.size

        override fun getItemPosition(`object`: Any): Int = super.getItemPosition(`object`)

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}
