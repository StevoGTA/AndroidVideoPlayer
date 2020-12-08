package codes.stevobrock.androidvideoplayer.simpleexoplayer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.fragment.app.Fragment
import codes.stevobrock.androidvideoplayer.model.Video
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

//----------------------------------------------------------------------------------------------------------------------
class SimpleExoPlayerFragment : Fragment() {

	// Interfaces
	//------------------------------------------------------------------------------------------------------------------
	interface SimpleExoPlayerFragmentListener {
		fun onQueryVideo() :Video
		fun onQueryLayoutResourceID() :Int
		fun onQueryPlayerParentViewGroupResourceID() :Int
	}

	// Properties
	private 			var listener :SimpleExoPlayerFragmentListener? = null

	private	lateinit	var	video :Video
	private	lateinit	var	simpleExoPlayer :SimpleExoPlayer

	// Lifecycle methods
	//------------------------------------------------------------------------------------------------------------------
	override fun onCreate(savedInstanceState : Bundle?) {
		// Do super
		super.onCreate(savedInstanceState)

		// Setup properties
		this.video = this.listener!!.onQueryVideo()
	}

	//------------------------------------------------------------------------------------------------------------------
	override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
		// Setup view
		val view = inflater.inflate(this.listener!!.onQueryLayoutResourceID(), container, false)

		// Setup player
		val context = this.listener as Context

		val	dataSourceFactory =
					DefaultDataSourceFactory(context, Util.getUserAgent(context, "Media Player"))

		this.simpleExoPlayer = SimpleExoPlayer.Builder(context).build()
		if (this.video.uri.path!!.endsWith("mpd"))
			// Dash
			this.simpleExoPlayer.prepare(DashMediaSource.Factory(dataSourceFactory).createMediaSource(this.video.uri))
		else if (this.video.uri.path!!.endsWith("m3u8"))
			// HLS
			this.simpleExoPlayer.prepare(HlsMediaSource.Factory(dataSourceFactory).createMediaSource(this.video.uri))
		else if (this.video.uri.path!!.endsWith("mp4"))
			// MP4
			this.simpleExoPlayer.prepare(
					ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(this.video.uri))
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
		if (context is SimpleExoPlayerFragmentListener) {
			// Store
			listener = context
		} else {
			// Error
			throw RuntimeException("$context must implement SimpleExoPlayerFragmentListener")
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
}
