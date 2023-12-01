package com.example.demo

import android.app.UiModeManager
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.jio.sdksampleapp.R
import com.jiomeet.core.constant.Constant
import com.jiomeet.core.main.models.JMJoinMeetingData
import com.jiomeet.core.utils.BaseUrl
import kotlinx.coroutines.flow.MutableStateFlow
import org.jio.telemedicine.sdkmanager.JioMeetListener
import org.jio.telemedicine.sdkmanager.JioMeetSdkManager
import org.jio.telemedicine.templates.core.LaunchJioHealthCare
import org.jio.telemedicine.templates.core.pipParams


class JoinRoomActivity : ComponentActivity() {

    private val isPipEnabled = MutableStateFlow(false)
    private val pipSupported: Boolean by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) packageManager.hasSystemFeature(
            PackageManager.FEATURE_PICTURE_IN_PICTURE
        )
        else false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseUrl.initializedNetworkInformation(this@JoinRoomActivity, Constant.Environment.PROD)

        val jioMeetListener = object : JioMeetListener {
            override fun onLeaveMeeting() {
                finish()
                Toast.makeText(this@JoinRoomActivity, "Left meeting ", Toast.LENGTH_LONG).show()
            }

            override fun onParticipantIconClicked() {
                Toast.makeText(this@JoinRoomActivity, "partcipant Icon clicked ", Toast.LENGTH_LONG)
                    .show()
            }
        }
        val jmJoinMeetingData = JMJoinMeetingData(
            meetingId = intent.getStringExtra(JioMeetSdkManager.MEETING_ID) ?: "",
            meetingPin = intent.getStringExtra(JioMeetSdkManager.MEETING_PIN) ?: "",
            displayName = intent.getStringExtra(JioMeetSdkManager.GUEST_NAME) ?: "",
            version = "",
            deviceId = ""
        )
        setContent {
            LaunchJioHealthCare(
                jioMeetListener = jioMeetListener,
                jmJoinMeetingData = jmJoinMeetingData,
                isPipEnabled = isPipEnabled
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        isPipEnabled.value = isInPictureInPictureMode
    }


    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (!pipSupported) return
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        pipParams()?.let { enterPictureInPictureMode(it) }
    }
}
