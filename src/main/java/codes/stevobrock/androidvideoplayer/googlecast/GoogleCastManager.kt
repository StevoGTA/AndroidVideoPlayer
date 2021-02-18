package codes.stevobrock.androidvideoplayer.googlecast

import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadRequestData
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.common.images.WebImage
import org.json.JSONObject

//----------------------------------------------------------------------------------------------------------------------
object GoogleCastManager {

	// Instance methods
	//------------------------------------------------------------------------------------------------------------------
	fun play(googleCastItem :GoogleCastItem, castContext :CastContext) {
		// Setup
		val castSession = castContext.sessionManager.currentCastSession ?: return
		val remoteMediaClient = castSession.remoteMediaClient ?: return

		val mediaMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE)
		mediaMetadata.putString(MediaMetadata.KEY_TITLE, googleCastItem.title)
		mediaMetadata.putString(MediaMetadata.KEY_SUBTITLE, googleCastItem.subtitle)
		if (googleCastItem.posterUri != null) mediaMetadata.addImage(WebImage(googleCastItem.posterUri))

		val streamType :Int
//		if (googleCastItem.uri.path!!.endsWith("mp4"))
//			// MP4
			streamType = MediaInfo.STREAM_TYPE_BUFFERED
//		else if (googleCastItem.uri.path!!.endsWith("m3u8"))
//			// HLS
//			streamType = MediaInfo.STREAM_TYPE_LIVE
//		else
//			// ???
//			streamType = MediaInfo.STREAM_TYPE_NONE

		val	customData = JSONObject()
		customData.put("licenseURL", googleCastItem.licenseUri)
		if (googleCastItem.licenseHeaders != null)
			customData.put("licenseHeaders", JSONObject(googleCastItem.licenseHeaders as Map<*, *>))

		val mediaInfo =
					MediaInfo.Builder(googleCastItem.uri.toString())
						.setStreamType(streamType)
						.setContentType("video/mp4")
						.setMetadata(mediaMetadata)
						.setCustomData(customData)
						.build()

		val mediaLoadRequestData =
					MediaLoadRequestData.Builder()
						.setMediaInfo(mediaInfo)
						.setAutoplay(googleCastItem.autoplay)
						.setCurrentTime(googleCastItem.startTime)
						.build()

		// Load the request
		remoteMediaClient.load(mediaLoadRequestData)
	}

	//------------------------------------------------------------------------------------------------------------------
	fun stop(castContext :CastContext) {
		// Setup
		val castSession = castContext.sessionManager.currentCastSession ?: return
		val remoteMediaClient = castSession.remoteMediaClient ?: return

		// Stop
		remoteMediaClient.stop()
	}
}
