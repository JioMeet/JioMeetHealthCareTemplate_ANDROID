package com.example.demo

import android.app.UiModeManager
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.demo.view.ui.CoreLoginView
import com.example.demo.viewModel.AppViewModel
import com.jiomeet.core.constant.Constant
import com.jiomeet.core.utils.BaseUrl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.jio.telemedicine.sdkmanager.JioMeetSdkManager
import org.jio.telemedicine.templates.core.JioHealthCareLauncherActivity
import org.jio.telemedicine.templates.core.OnLeaveParticipant
import org.jio.telemedicine.templates.core.OnParticipantIconClicked
import org.jio.telemedicine.util.CallbackSharedEvent

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uiModeManager = getSystemService(UI_MODE_SERVICE) as UiModeManager
        if (uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        // Calling SDK from Activity as (Component activity)
        // Passing valid meeting ID and MeetingPin along with user name
        //Code to observe differnt events in meeting
        BaseUrl.initializedNetworkInformation(this@MainActivity, Constant.Environment.PROD)
        lifecycleScope.launch {
            CallbackSharedEvent.callbackFlow.events.collect {
                when(it){
                    is OnParticipantIconClicked -> {
                        Log.e("TAG", "onCreate: " + "callback" )
                        Toast.makeText(this@MainActivity,"ParticipantIcon clicked",Toast.LENGTH_LONG).show()
                    }
                    is OnLeaveParticipant ->{
                        Toast.makeText(this@MainActivity,"User Left Meeting",Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            }
        }
        setContent {
            CoreLoginView(onJoinMeetingClick = {
                val telemedicineLauncherIntent = Intent(this, JioHealthCareLauncherActivity::class.java)
                telemedicineLauncherIntent.putExtra(JioMeetSdkManager.MEETING_ID,viewModel.loginState.value.meetingID)
                telemedicineLauncherIntent.putExtra(JioMeetSdkManager.MEETING_PIN,viewModel.loginState.value.meetingPin)
                telemedicineLauncherIntent.putExtra(JioMeetSdkManager.GUEST_NAME, viewModel.loginState.value.meetingPin)
                startActivity(telemedicineLauncherIntent)
            }, viewModel)

        }

    }
}
