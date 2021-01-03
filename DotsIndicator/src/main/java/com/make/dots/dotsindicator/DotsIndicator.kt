package com.make.dots.dotsindicator

import android.annotation.TargetApi
import android.content.Context
import android.database.DataSetObserver
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener

class DotsIndicator : LinearLayout {
    lateinit var mViewpager: ViewPager
    private var mIndicatorMargin = -1
    private var mIndicatorWidth = -1
    private var mIndicatorHeight = -1
    private var mIndicatorBackgroundResId = R.drawable.dot_selected
    private var mIndicatorUnselectedBackgroundResId = R.drawable.dot_unselected
    private var mLastPosition = -1

    private val mInternalPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            if (mViewpager.adapter == null || mViewpager.adapter?.count ?: 0 <= 0) return
            if (mLastPosition >= 0) getChildAt(mLastPosition)?.setBackgroundResource(mIndicatorUnselectedBackgroundResId)
            getChildAt(position)?.setBackgroundResource(mIndicatorBackgroundResId)
            mLastPosition = position
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

    val dataSetObserver: DataSetObserver = object : DataSetObserver() {
        override fun onChanged() {
            super.onChanged()
            val newCount = mViewpager.adapter?.count ?: 0
            val currentCount = childCount
            mLastPosition = when {
                newCount == currentCount -> return
                mLastPosition < newCount -> mViewpager.currentItem
                else -> -1
            }
            createIndicators()
        }
    }

    constructor(context: Context)
        : super(context) {
        
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet)
        : super(context, attrs) {
        
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
        : super(context, attrs, defStyleAttr) {
        
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
        : super(context, attrs, defStyleAttr, defStyleRes) {
        
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        handleTypedArray(context, attrs)
        checkIndicatorConfig()
    }

    private fun handleTypedArray(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DotsIndicator)
        with(typedArray) {
            mIndicatorWidth = getDimensionPixelSize(R.styleable.DotsIndicator_dot_width, -1)
            mIndicatorHeight = getDimensionPixelSize(R.styleable.DotsIndicator_dot_height, -1)
            mIndicatorMargin = getDimensionPixelSize(R.styleable.DotsIndicator_dot_margin, -1)
            mIndicatorBackgroundResId = getResourceId(R.styleable.DotsIndicator_dot_drawable,
                R.drawable.dot_selected)
            mIndicatorUnselectedBackgroundResId = getResourceId(
                R.styleable.DotsIndicator_dot_drawable_unselected, mIndicatorBackgroundResId)
    
            val orientation = getInt(R.styleable.DotsIndicator_dot_orientation, -1)
            setOrientation(if (orientation == VERTICAL) {
                VERTICAL
            } else {
                HORIZONTAL
            })
    
            val gravity = getInt(R.styleable.DotsIndicator_dot_gravity, -1)
            setGravity(if (gravity >= 0) {
                gravity
            } else {
                Gravity.CENTER
            })
    
            recycle()
        }
    }

    private fun checkIndicatorConfig() {
        mIndicatorWidth = if (mIndicatorWidth < 0) {
            convertDpToPx(DEFAULT_INDICATOR_WIDTH.toFloat())
        } else {
            mIndicatorWidth
        }
        
        mIndicatorHeight = if (mIndicatorHeight < 0) {
            convertDpToPx(DEFAULT_INDICATOR_WIDTH.toFloat())
        } else {
            mIndicatorHeight
        }
        
        mIndicatorMargin = if (mIndicatorMargin < 0) {
            convertDpToPx(DEFAULT_INDICATOR_WIDTH.toFloat())
        } else {
            mIndicatorMargin
        }
        
        mIndicatorBackgroundResId = if (mIndicatorBackgroundResId == 0) {
            R.drawable.dot_unselected
        } else {
            mIndicatorBackgroundResId
        }
        
        mIndicatorUnselectedBackgroundResId = if (mIndicatorUnselectedBackgroundResId == 0) {
            mIndicatorBackgroundResId
        } else {
            mIndicatorUnselectedBackgroundResId
        }
    }

    fun setViewPager(viewPager: ViewPager) {
        mViewpager = viewPager
        if (mViewpager.adapter != null) {
            mLastPosition = -1
            createIndicators()
            mViewpager.removeOnPageChangeListener(mInternalPageChangeListener)
            mViewpager.addOnPageChangeListener(mInternalPageChangeListener)
            mInternalPageChangeListener.onPageSelected(mViewpager.currentItem)
        }
    }

    private fun createIndicators() {
        removeAllViews()
        
        val count = mViewpager.adapter?.count ?: 0
        if (count <= 0) {
            return
        }
        
        val currentItem = mViewpager.currentItem
        for (i in 0 until count) {
            if (currentItem == i) {
                addIndicator(mIndicatorBackgroundResId)
            } else {
                addIndicator(mIndicatorUnselectedBackgroundResId)
            }
        }
    }

    private fun addIndicator(@DrawableRes backgroundDrawableId: Int) {
        val indicator = View(context)
        indicator.setBackgroundResource(backgroundDrawableId)
        addView(indicator, mIndicatorWidth, mIndicatorHeight)
        
        val layoutParams = indicator.layoutParams as LayoutParams
        indicator.layoutParams = layoutParams.apply {
            leftMargin = mIndicatorMargin
            rightMargin = mIndicatorMargin
            topMargin = mIndicatorMargin
            bottomMargin = mIndicatorMargin
        }
    }

    private fun convertDpToPx(dpValue: Float): Int
        = (dpValue * resources.displayMetrics.density + 0.5f).toInt()

    companion object {
        private const val DEFAULT_INDICATOR_WIDTH = 5
    }
}