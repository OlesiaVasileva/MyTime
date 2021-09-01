package com.olesix.mytime.di

import com.olesix.mytime.viewModel.TimeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel { TimeViewModel() }
}