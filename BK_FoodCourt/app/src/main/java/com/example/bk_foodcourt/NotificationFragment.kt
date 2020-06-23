package com.example.bk_foodcourt

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment


class NotificationFragment : Fragment() {
    public val CHANNEL1_ID: String = "Modify Request"
    public val CHANNEL2_ID: String = "Complete Notification"

    var channel: Spinner? = null
    //var order: Spinner? = null
    var description: EditText? = null


    // Spinner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_notification, container, false)

        channel = view.findViewById(R.id.spinnerChannel)
        //order = view.findViewById(R.id.spinnerOrder)
        description = view.findViewById(R.id.etDescription)

        setChannelContent()

        createNotificationChannels(description.toString().trim(), channel!!.selectedItem.toString().trim())
        return view
    }


    private fun setChannelContent() {
        val Adapter = ArrayAdapter.createFromResource(
            this.context!!,
            R.array.channel_choice,
            R.layout.support_simple_spinner_dropdown_item
        )
        Adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        channel!!.setAdapter(Adapter)
    }

    private fun createNotificationChannels(txtDescription: String, whichChannel: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel: NotificationChannel
            if (whichChannel == CHANNEL1_ID) {
                mChannel =  NotificationChannel(CHANNEL1_ID, "Modify Request !!!", IMPORTANCE_HIGH)
            }
            else {
                mChannel =  NotificationChannel(CHANNEL2_ID, "Order Completed !!!", IMPORTANCE_HIGH)
            }
            mChannel.description = txtDescription
            val notificationManager = context!!.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }


}