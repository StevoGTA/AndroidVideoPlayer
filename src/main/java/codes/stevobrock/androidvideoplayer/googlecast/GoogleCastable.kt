package codes.stevobrock.androidvideoplayer.googlecast

import android.net.Uri
import codes.stevobrock.androidvideoplayer.model.MediaPlayable

//----------------------------------------------------------------------------------------------------------------------
interface GoogleCastable : MediaPlayable {

	// Properties
	val	subtitle :String
	val	posterUri : Uri?

	// Instance methods
	//------------------------------------------------------------------------------------------------------------------
	fun googleCastItem(licenseUri :Uri? = null, licenseHeaders :HashMap<String, String>? = null,
			autoplay :Boolean = true, startTime :Long = 0) : GoogleCastItem {
		// Return GoogleCastItem
		return GoogleCastItem(this.title, this.subtitle, this.uri, this.posterUri, licenseUri, licenseHeaders, autoplay,
				startTime)
	}
}
