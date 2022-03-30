package uz.targetsoftwaredevelopment.wsen.presentation.ui.screens

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import dagger.hilt.android.AndroidEntryPoint
import uz.targetsoftwaredevelopment.wsen.R
import uz.targetsoftwaredevelopment.wsen.databinding.ScreenWatchVideoBinding
import uz.targetsoftwaredevelopment.wsen.utils.scope

@AndroidEntryPoint
class WatchVideoScreen : Fragment(R.layout.screen_watch_video) {
    private val binding by viewBinding(ScreenWatchVideoBinding::bind)
    private lateinit var player: ExoPlayer
    private val args: WatchVideoScreenArgs by navArgs()
    var isLike = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.scope {
        super.onViewCreated(view, savedInstanceState)

        loadData()

        likeVideoImg.setOnClickListener {
            isLike = if(isLike){
                likeVideoImg.setImageResource(R.drawable.ic_heart_unlike)
                // TODO: view model change like
                false
            }else{
                likeVideoImg.setImageResource(R.drawable.ic_heart)
                true
                // TODO: change like

            }
        }


    }

    private fun loadData() {
        binding.apply {

            player = ExoPlayer.Builder(requireContext()).build()
            val onlineUri: Uri = Uri.parse(args.videoData.video)
            val mediaItem: MediaItem = MediaItem.fromUri(onlineUri)
            watchVideoView.player = player
            player.setMediaItem(mediaItem)
            player.prepare()

            if(args.videoData.is_liked_by_currentUser){
                likeVideoImg.setImageResource(R.drawable.ic_heart)
            }else{
                likeVideoImg.setImageResource(R.drawable.ic_heart_unlike)
            }

            if(args.videoData.status=="1"){
                statusColor.setBackgroundResource(R.drawable.shape_status_waiting)
                statusTextTv.text = getString(R.string.waiting)
            }else{
                statusColor.setBackgroundResource(R.drawable.shape_status_finished)
                statusTextTv.text = getString(R.string.finished)
            }

            Glide.with(accountImgView.context)
                .load(args.videoData.owner?.photo)
                .placeholder(R.drawable.default_profile_img2)
                .error(R.drawable.default_profile_img2)
                .into(accountImgView)

            accountNameTv1.text = args.videoData.owner?.username

            likesCountTv.text = args.videoData.like.toString()

            val year = args.videoData.created_at?.substring(0,3)
            dateYearTv.text = year

            val monthData = args.videoData.created_at?.substring(5,9)
            dataMonthTv.text = monthData

            watchDescriptionTv.text = args.videoData.desc

        }
    }

    override fun onStop() {
        super.onStop()
        player.stop()
    }
}
