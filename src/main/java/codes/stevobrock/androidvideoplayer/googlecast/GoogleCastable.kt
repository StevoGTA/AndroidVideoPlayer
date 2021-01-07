package codes.stevobrock.androidvideoplayer.googlecast

import android.net.Uri
import codes.stevobrock.androidvideoplayer.model.MediaPlayable

//----------------------------------------------------------------------------------------------------------------------
interface GoogleCastable : MediaPlayable {

	// Properties
	val	subtitle :String
	val	posterUri : Uri?
}
