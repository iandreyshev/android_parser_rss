package ru.iandreyshev.parserrss.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface IBaseView : MvpView {
    @StateStrategyType(SkipStrategy::class)
    fun showShortToast(message: String)

    @StateStrategyType(SkipStrategy::class)
    fun showLongToast(message: String)
}
