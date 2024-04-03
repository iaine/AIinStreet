package uk.ac.warwick.cim.aiinstreet

data class AudioLocations(val lat: Double,
                    val long: Double,
                    val audioUrl: String,
                    val audioText: String) {
    operator fun iterator(): MutableIterator<AudioLocations> {
        return TODO("Provide the return value")
    }


}