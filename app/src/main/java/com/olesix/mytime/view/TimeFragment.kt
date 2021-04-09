package com.olesix.mytime.view

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
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
        binding.fragmentChronometer.setOnClickListener {
            viewModel.start()
        }

        binding.fragmentChronometer.setOnLongClickListener {
            viewModel.reset()
            true
        }

        binding.fragmentLayout.setOnClickListener(object : DoubleClickListener() {
            override fun onDoubleClick(v: View?) {
                viewModel.stop()
            }
        })
    }

    // Observer is waiting for viewModel to update our UI
    private fun fragmentChronometerStatesUpdateObserver() {
        var difference: Long = 0
        var time: Long

        viewModel.uiTimeLiveData.observe(viewLifecycleOwner, Observer { updatedState ->
            when (updatedState) {
                TimeStateModel.START -> {
                    time = difference + SystemClock.elapsedRealtime()
                    binding.fragmentChronometer.base = time
                    binding.fragmentChronometer.start();
                    difference = 0
                }
                TimeStateModel.STOP -> {
                    binding.fragmentChronometer.stop()
                    difference = binding.fragmentChronometer.base - SystemClock.elapsedRealtime()
                }
                TimeStateModel.RESET -> {
                    binding.fragmentChronometer.stop()
                    binding.fragmentChronometer.base = SystemClock.elapsedRealtime();
                    difference = 0
                }
            }
        })
    }
}

abstract class DoubleClickListener : View.OnClickListener {
    var lastClickTime: Long = 0
    override fun onClick(v: View?) {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            onDoubleClick(v)
        }
        lastClickTime = clickTime
    }

    abstract fun onDoubleClick(v: View?)

    companion object {
        private const val DOUBLE_CLICK_TIME_DELTA: Long = 300
    }
}