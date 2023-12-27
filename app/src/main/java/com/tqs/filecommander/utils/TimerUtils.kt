package com.tqs.filecommander.utils

import android.os.CountDownTimer

object TimerUtils {

    var countDownTimer: CountDownTimer? = null
    var countDownTimerCancel = false
    fun startCountDownTimer(time: Long, onTick: (Long) -> Unit, onFish: () -> Unit) {
        stopCountDownTimer()
        countDownTimer =
            object : CountDownTimer(time, 33) {
                override fun onTick(millisUntilFinished: Long) {
                    if(countDownTimerCancel)
                        cancel()
                    onTick.invoke(millisUntilFinished)
                }

                override fun onFinish() {
                    if(countDownTimerCancel){
                        return
                    }
                    onFish.invoke()

                }
            }
        countDownTimer?.start()
        countDownTimerCancel = false
    }

    fun stopCountDownTimer(){
        countDownTimer?.cancel()
        countDownTimerCancel = true
    }
}