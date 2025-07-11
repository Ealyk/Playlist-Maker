package playlist.search.ui.adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import playlist.search.domain.model.Track
import playlist.player.ui.activity.AudioplayerActivity

class TrackAdapter(
    private val onTrackClicked: (Track) -> Unit,
    private val context: Context
): RecyclerView.Adapter<TrackViewHolder>() {

    val trackList: MutableList<Track> = mutableListOf()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, AudioplayerActivity::class.java)
            intent.putExtra(EXTRA_TRACK, track)
            context.startActivity(intent)

            handler.postDelayed( { onTrackClicked(track) }, DELAY_HISTORY_UPDATE)

        }
    }

    fun updateList(newList: List<Track>) {
        trackList.clear()
        trackList.addAll(newList)
        notifyDataSetChanged()
    }

    companion object {
        private const val EXTRA_TRACK = "track"
        private const val DELAY_HISTORY_UPDATE = 500L
    }

}