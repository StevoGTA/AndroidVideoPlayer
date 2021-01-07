package codes.stevobrock.androidvideoplayer.model

import android.net.Uri

//----------------------------------------------------------------------------------------------------------------------
interface MediaPlayable {

	// Properties
	val	identifier :String
	val	title :String
	val	description :String?
	val	uri :Uri
}
