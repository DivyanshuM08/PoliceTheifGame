package com.example.policetheifgame.game.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

/**
 * Manages police siren audio using real-time PCM synthesis via AudioTrack.
 * Zero external audio assets required; smooth seamless wail oscillation.
 */
class SirenSoundManager(
    initialMuted: Boolean = false
) {
    private var audioTrack: AudioTrack? = null
    private var audioJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    var isMuted: Boolean = initialMuted
        private set

    @Volatile
    private var isPlaying: Boolean = false

    private val sampleRate = 22050
    private val bufferSize: Int

    init {
        val minBuff = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        bufferSize = maxOf(minBuff, sampleRate / 4) // ~250ms chunks
        initAudioTrack()
    }

    private fun initAudioTrack() {
        try {
            audioTrack = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AudioTrack(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build(),
                    AudioFormat.Builder()
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .build(),
                    bufferSize,
                    AudioTrack.MODE_STREAM,
                    AudioManager.AUDIO_SESSION_ID_GENERATE
                )
            } else {
                @Suppress("DEPRECATION")
                AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize,
                    AudioTrack.MODE_STREAM
                )
            }
            updateVolume()
        } catch (e: Exception) {
            audioTrack = null
        }
    }

    fun play() {
        if (isPlaying) return
        isPlaying = true

        val track = audioTrack ?: return
        try {
            if (track.playState != AudioTrack.PLAYSTATE_PLAYING) {
                track.play()
            }
        } catch (e: Exception) {
            return
        }

        audioJob?.cancel()
        audioJob = scope.launch {
            var phase = 0.0
            val minFreq = 650.0  // Police siren low tone (Hz)
            val maxFreq = 1150.0 // Police siren high tone (Hz)
            val sweepPeriodSeconds = 1.2
            var timeInSweep = 0.0
            val dt = 1.0 / sampleRate

            val buffer = ShortArray(1024)

            while (isActive && isPlaying) {
                for (i in buffer.indices) {
                    timeInSweep += dt
                    if (timeInSweep >= sweepPeriodSeconds) {
                        timeInSweep -= sweepPeriodSeconds
                    }

                    // Sine sweep oscillation (wail)
                    val sweepProgress = sin(2.0 * PI * (timeInSweep / sweepPeriodSeconds))
                    val currentFreq = minFreq + (maxFreq - minFreq) * (0.5 * (sweepProgress + 1.0))

                    phase += 2.0 * PI * currentFreq * dt
                    if (phase >= 2.0 * PI) {
                        phase -= 2.0 * PI
                    }

                    val sample = (sin(phase) * 16000.0).toInt().toShort()
                    buffer[i] = if (isMuted) 0 else sample
                }

                track.write(buffer, 0, buffer.size)
            }
        }
    }

    fun pause() {
        isPlaying = false
        audioJob?.cancel()
        try {
            audioTrack?.pause()
            audioTrack?.flush()
        } catch (_: Exception) {}
    }

    fun stop() {
        isPlaying = false
        audioJob?.cancel()
        try {
            audioTrack?.stop()
            audioTrack?.flush()
        } catch (_: Exception) {}
    }

    fun setMuted(muted: Boolean) {
        isMuted = muted
        updateVolume()
    }

    fun toggleMute(): Boolean {
        isMuted = !isMuted
        updateVolume()
        return isMuted
    }

    private fun updateVolume() {
        try {
            val volume = if (isMuted) 0f else 0.8f
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                audioTrack?.setVolume(volume)
            } else {
                @Suppress("DEPRECATION")
                audioTrack?.setStereoVolume(volume, volume)
            }
        } catch (_: Exception) {}
    }

    fun release() {
        stop()
        try {
            audioTrack?.release()
        } catch (_: Exception) {}
        audioTrack = null
    }
}
