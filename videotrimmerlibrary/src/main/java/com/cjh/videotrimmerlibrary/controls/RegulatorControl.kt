package com.cjh.videotrimmerlibrary.controls

import android.annotation.SuppressLint
import android.util.Log
import android.widget.RelativeLayout
import android.widget.TextView
import com.cjh.videotrimmerlibrary.MediaHandleManager
import com.cjh.videotrimmerlibrary.VideoTrimmerView
import com.cjh.videotrimmerlibrary.callback.EndTouchActionListener
import com.cjh.videotrimmerlibrary.callback.EndScrollActionListener
import com.cjh.videotrimmerlibrary.callback.IConfig
import com.cjh.videotrimmerlibrary.callback.UpdatePosListener
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by cjh on 2017/8/31.
 */
class RegulatorControl private constructor(leftPos: TextView, rightPos: TextView) : EndScrollActionListener, EndTouchActionListener, UpdatePosListener {


    val leftPosTv = leftPos

    val rightPosTv = rightPos

    override fun updatePos() {
        updatePosTextViewsMargin()
    }

    private fun updatePosTextViewsMargin() {
        updateLeftMargin()
        updateRightMargin()
        updatePosTextViewsContent()
    }

    private fun updateLeftMargin() {
        val layoutParams = leftPosTv.layoutParams as RelativeLayout.LayoutParams
        layoutParams.setMargins(TrimmerSeekBarControl.getInstance().getLeftPosX().toInt(), layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin)
        leftPosTv.layoutParams = layoutParams
    }

    private fun updateRightMargin() {
        val layoutParams = rightPosTv.layoutParams as RelativeLayout.LayoutParams
        val rightMaring = TrimmerSeekBarControl.getInstance().mTrimmerSeekBar.imeasureWidth - TrimmerSeekBarControl.getInstance().getRightPosX().toInt()
        layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, rightMaring, layoutParams.bottomMargin)
        rightPosTv.layoutParams = layoutParams

    }

    override fun updateRegionIndex() {
        updateVideoViewThumb()
    }

    override fun updateByScroll() {
        updateVideoViewThumb()
        updatePosTextViewsContent()
    }

    private fun updateVideoViewThumb() {
        VideoViewControl.getInstance().updatePos(getThumbPos(TrimmerSeekBarControl.getInstance().leftIndex))
    }

    private fun updatePosTextViewsContent() {
        Log.e("64 ::: ", TrimmerSeekBarControl.getInstance().rightIndex.toString())
        Log.e("65 ::: ", getThumbPos(TrimmerSeekBarControl.getInstance().rightIndex).toString())
        setPosTextViews(getThumbPos(TrimmerSeekBarControl.getInstance().leftIndex), getThumbPos(TrimmerSeekBarControl.getInstance().rightIndex))
    }

    private fun getThumbPos(regionIndex: Int): Long {
        var realIndex = RecyclerViewControl.getInstance().firstItemPosition + regionIndex
        val totalThumbSize = RecyclerViewControl.getInstance().mThumbAdapter.mDatas.size
        if (realIndex >= totalThumbSize)
            realIndex = totalThumbSize - 1
        val thumbVo = RecyclerViewControl.getInstance().mThumbAdapter.mDatas[realIndex]
        return thumbVo.positionL / 1000
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var mInstance: RegulatorControl? = null

        fun getInstance(videoTrimmerView: VideoTrimmerView): RegulatorControl {
            if (mInstance == null) {
                synchronized(VideoViewControl::class) {
                    if (mInstance == null) {
                        mInstance = RegulatorControl(videoTrimmerView.getLeftPosTextView(), videoTrimmerView.getRightPosTextView())
                        VideoViewControl.getInstance(videoTrimmerView.getVideoView())
                        RecyclerViewControl.getInstance(videoTrimmerView.getRecyclerView(), mInstance!!)
                        TrimmerSeekBarControl.getInstance(videoTrimmerView.getTrimmerSeekBar(), mInstance!!, mInstance!!)
                    }
                }
            }
            return mInstance!!
        }

        fun getInstance(): RegulatorControl {
            if (mInstance == null) {
                throw IllegalArgumentException("RegulatorControl getInstance ::: must call method getInstance(videoView: VideoView, recyclerView: RecyclerView) first !!!")
            }
            return mInstance!!
        }
    }

    fun setVideoPath(videoPath: String): RegulatorControl {
        MediaHandleManager.getInstance().setVideoPath(videoPath)
        return this
    }

    fun initialThumbItemWH(wh: Array<Int>): RegulatorControl {
        MediaHandleManager.getInstance().setThumbItemWH(wh)
        return this
    }

    fun handle() {
        setPosTextViews(0, MediaHandleManager.getInstance().getConfigVo().getVisiableEndPos())
        VideoViewControl.getInstance().initial()
        MediaHandleManager.getInstance().getFrameThumb(RecyclerViewControl.getInstance())
    }

    private fun setPosTextViews(leftPos: Long, rightPos: Long) {
        leftPosTv.text = SimpleDateFormat("m:ss").format(Date(leftPos))
        rightPosTv.text = SimpleDateFormat("m:ss").format(rightPos)
    }

    fun release() {
        mInstance = null
        MediaHandleManager.getInstance().release()
    }

    fun setIConfig(icg: IConfig) {
        MediaHandleManager.getInstance().setIConfig(icg)
        TrimmerSeekBarControl.getInstance().mTrimmerSeekBar.postInvalidate()
    }

    fun getTrimmerPos(): LongArray = longArrayOf(getThumbPos(TrimmerSeekBarControl.getInstance().leftIndex), getThumbPos(TrimmerSeekBarControl.getInstance().rightIndex))
}