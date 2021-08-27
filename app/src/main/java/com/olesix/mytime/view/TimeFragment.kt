package com.olesix.mytime.view

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.olesix.mytime.R
import com.olesix.mytime.databinding.FragmentTimeBinding
import com.olesix.mytime.model.TimeStateModel
import com.olesix.mytime.viewModel.TimeViewModel


/**
 * TimeFragment: - shows the UI, - listens to TimeViewModel for updates on UI
 * One click on the Chronometer - starts work, long click on the Chronometer
 * resets the time, double click on the area around the Chronometer - pauses.
 */
class TimeFragment: Fragment() {

    private var _binding: FragmentTimeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TimeViewModel by activityViewModels()
    private val LOG_TAG: String = "myApp"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        fragmentChronometerStatesUpdateObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupClickListeners() {
        val gestureDetector = GestureDetector(context, GestureListener())
        binding.fragmentLayout.setOnTouchListener {_, event -> gestureDetector.onTouchEvent(event) }
    }

    private fun startAnimation(it: View?, colorFrom: Int, colorTo: Int) {
        val duration = 500L
        ObjectAnimator.ofObject(
            it,
            "backgroundColor",
            ArgbEvaluator(),
            colorFrom,
            colorTo
        )
            .setDuration(duration)
            .start()
    }

    // Observer is waiting for viewModel to update our UI
    private fun fragmentChronometerStatesUpdateObserver() {

        viewModel.uiTimeLiveData.observe(viewLifecycleOwner, Observer { updatedState ->
            when (updatedState) {
                is TimeStateModel.Start -> {
                    binding.fragmentChronometer.base = updatedState.time
                    binding.fragmentChronometer.start();
                }
                is TimeStateModel.Stop -> {
                    binding.fragmentChronometer.stop()
                }
                is TimeStateModel.Reset -> {
                    binding.fragmentChronometer.stop()
                    binding.fragmentChronometer.base = updatedState.time;
                }
            }
        })
    }

    private inner class GestureListener : SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent?): Boolean {
            Log.d(LOG_TAG, "onDoubleTap")
            viewModel.stop()
            binding.fragmentLayout.animate().apply {
                context?.let { ContextCompat.getColor(it, R.color.colorAnimation) }?.let {
                    startAnimation(binding.fragmentLayout,
                        it, Color.BLACK)
                }
            }
            return true
        }
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
        override fun onLongPress(event: MotionEvent) {
            Log.d(LOG_TAG, "onLongPress")
            viewModel.reset()
        }
        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            Log.d(LOG_TAG, "onSingleTapConfirmed")
            val isRunning = viewModel.start()
            if (!isRunning) {
                binding.fragmentLayout.animate().apply {
                    startAnimation(binding.fragmentLayout, Color.WHITE, Color.BLACK)
                }
            }
            return super.onSingleTapConfirmed(e)
        }
    }
}


