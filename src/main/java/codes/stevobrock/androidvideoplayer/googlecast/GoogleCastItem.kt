package codes.stevobrock.androidvideoplayer.googlecast

import android.content.Intent
import android.net.Uri

//----------------------------------------------------------------------------------------------------------------------
class GoogleCastItem {

	// Properties
	val	title :String?
	val	subtitle :String?
	val	uri :Uri
	val	posterUri :Uri?

	val	licenseUri :Uri?
	val	licenseHeaders :HashMap<String, String>?

	val	autoplay :Boolean
	val	startTime :Long

	// Lifecycle methods
	//------------------------------------------------------------------------------------------------------------------
	constructor(title :String?, subtitle :String?, uri :Uri, posterUri :Uri?, licenseUri :Uri?,
			licenseHeaders :HashMap<String, String>?, autoplay :Boolean, startTime :Long) {
		// Store
		this.title = title
		this.subtitle = subtitle
		this.uri = uri
		this.posterUri = posterUri

		this.licenseUri = licenseUri
		this.licenseHeaders = licenseHeaders

		this.autoplay = autoplay
		this.startTime = startTime
	}

	//------------------------------------------------------------------------------------------------------------------
	constructor(intent :Intent) {
		// Retrieve
		this.title = intent.getStringExtra("GOOGLE_CAST_ITEM_TITLE")
		this.subtitle = intent.getStringExtra("GOOGLE_CAST_ITEM_SUBTITLE")
		this.uri = intent.getParcelableExtra("GOOGLE_CAST_ITEM_URI")!!
		this.posterUri = intent.getParcelableExtra("GOOGLE_CAST_ITEM_POSTER_URI")

		this.licenseUri = intent.getParcelableExtra("GOOGLE_CAST_ITEM_LICENSE_URI")
		this.licenseHeaders =
				intent.getSerializableExtra("GOOGLE_CAST_ITEM_LICENSE_HEADERS") as HashMap<String, String>?

		this.autoplay = intent.getBooleanExtra("GOOGLE_CAST_ITEM_AUTOPLAY", true)
		this.startTime = intent.getLongExtra("GOOGLE_CAST_ITEM_START_TIME", 0)
	}

	// Instance methods
	//------------------------------------------------------------------------------------------------------------------
	fun put(intent :Intent) {
		// Put
		intent.putExtra("GOOGLE_CAST_ITEM_TITLE", this.title)
		intent.putExtra("GOOGLE_CAST_ITEM_SUBTITLE", this.subtitle)
		intent.putExtra("GOOGLE_CAST_ITEM_URI", this.uri)
		intent.putExtra("GOOGLE_CAST_ITEM_POSTER_URI", this.posterUri)

		intent.putExtra("GOOGLE_CAST_ITEM_LICENSE_URI", this.licenseUri)
		intent.putExtra("GOOGLE_CAST_ITEM_LICENSE_HEADERS", this.licenseHeaders)

		intent.putExtra("GOOGLE_CAST_ITEM_AUTOPLAY", this.autoplay)
		intent.putExtra("GOOGLE_CAST_ITEM_START_TIME", this.startTime)
	}
}
