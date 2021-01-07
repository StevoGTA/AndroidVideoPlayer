package codes.stevobrock.androidvideoplayer.exoplayer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.fragment.app.Fragment
import codes.stevobrock.androidvideoplayer.model.MediaPlayable
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import java.lang.Exception

//----------------------------------------------------------------------------------------------------------------------
class ExoPlayerFragment : Fragment() {

	// Interfaces
	//------------------------------------------------------------------------------------------------------------------
	interface ExoPlayerFragmentListener {
		// Core methods
		fun onQueryMediaPlayable() :MediaPlayable

		// Playback methods
		fun onQueryMediaSource(context :Context, mediaPlayable :MediaPlayable) :MediaSource
				{ return defaultMediaSource(context, mediaPlayable) }

		// UI methods
		fun onQueryLayoutResourceID() :Int
		fun onQueryPlayerParentViewGroupResourceID() :Int
	}

	// Properties
	private 			var listener :ExoPlayerFragmentListener? = null

	private	lateinit	var	mediaPlayable :MediaPlayable
	private	lateinit	var	simpleExoPlayer :SimpleExoPlayer

	// Lifecycle methods
	//------------------------------------------------------------------------------------------------------------------
	override fun onCreate(savedInstanceState :Bundle?) {
		// Do super
		super.onCreate(savedInstanceState)

		// Setup properties
		this.mediaPlayable = this.listener!!.onQueryMediaPlayable()
	}

	//------------------------------------------------------------------------------------------------------------------
	override fun onCreateView(inflater :LayoutInflater, container :ViewGroup?, savedInstanceState :Bundle?) :View {
		// Setup view
		val view = inflater.inflate(this.listener!!.onQueryLayoutResourceID(), container, false)

		// Setup player
		val context = this.listener as Context

		this.simpleExoPlayer = SimpleExoPlayer.Builder(context).build()
		this.simpleExoPlayer.prepare(this.listener!!.onQueryMediaSource(context, this.mediaPlayable))
		this.simpleExoPlayer.playWhenReady = true

		val	playerView = PlayerView(context)
		playerView.player = this.simpleExoPlayer

		view!!.findViewById<ViewGroup>(this.listener!!.onQueryPlayerParentViewGroupResourceID())
				.addView(playerView, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))

		return view
	}

	//------------------------------------------------------------------------------------------------------------------
	override fun onAttach(context :Context) {
		// Do super
		super.onAttach(context)

		// Check context
		if (context is ExoPlayerFragmentListener) {
			// Store
			listener = context
		} else {
			// Error
			throw RuntimeException("$context must implement ExoPlayerFragmentListener")
		}
	}

	//------------------------------------------------------------------------------------------------------------------
	override fun onDetach() {
		// Do super
		super.onDetach()

		// Reset
		this.simpleExoPlayer.stop()
		listener = null
	}

	// Companion object
	//------------------------------------------------------------------------------------------------------------------
	companion object {

		// Exceptions
		class UnknownVideoTypeException : Exception()

		// Methods
		fun defaultMediaSource(context :Context, mediaPlayable :MediaPlayable) :MediaSource {
			// Setup
			val	dataSourceFactory =
						DefaultDataSourceFactory(context, Util.getUserAgent(context,
								"ExoPlayerFragment"))

			return if (mediaPlayable.uri.path!!.endsWith("mpd"))
				// Dash
				DashMediaSource.Factory(dataSourceFactory).createMediaSource(mediaPlayable.uri)
			else if (mediaPlayable.uri.path!!.endsWith("m3u8"))
				// HLS
				HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaPlayable.uri)
			else if (mediaPlayable.uri.path!!.endsWith("mp4"))
				// MP4
				ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaPlayable.uri)
			else
				// Huh?
				throw UnknownVideoTypeException()
		}
	}
}
