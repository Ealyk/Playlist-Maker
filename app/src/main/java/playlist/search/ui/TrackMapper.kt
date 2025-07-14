package playlist.search.ui

import playlist.search.domain.model.Track
import playlist.search.ui.model.TrackUi

object TrackMapper {
    fun toUi(model: Track): TrackUi = TrackUi(
        trackName = model.trackName,
        artistName = model.artistName,
        trackTime = model.trackTime,
        artworkUrl100 = model.artworkUrl100,
        collectionName = model.collectionName,
        releaseDate = model.releaseDate,
        primaryGenreName = model.primaryGenreName,
        country = model.country,
        previewUrl = model.previewUrl
    )

    fun toDomain(ui: TrackUi): Track = Track(
        trackName = ui.trackName,
        artistName = ui.artistName,
        trackTime = ui.trackTime,
        artworkUrl100 = ui.artworkUrl100,
        collectionName = ui.collectionName,
        releaseDate = ui.releaseDate,
        primaryGenreName = ui.primaryGenreName,
        country = ui.country,
        previewUrl = ui.previewUrl
    )
}