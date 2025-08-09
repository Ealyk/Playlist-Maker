package playlist.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ealyk.playlistmaker2.R
import com.ealyk.playlistmaker2.databinding.TrackCardBinding
import playlist.search.ui.model.TrackUi

class TrackViewHolder(private val binding: TrackCardBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(model: TrackUi) {
        binding.trackCardName.text = model.trackName
        binding.trackCardNameArtist.text = model.artistName
        binding.trackCardTime.text = model.trackTime

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.placeholder)
            .into(binding.trackCardImage)

    }

    companion object {
        fun from(parent: ViewGroup): TrackViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TrackCardBinding.inflate(inflater, parent,false)
            return TrackViewHolder(binding)
        }
    }

}