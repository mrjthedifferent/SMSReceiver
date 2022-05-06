package mrj.info.bd.nidsms.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.widget.Toast
import mrj.info.bd.nidsms.models.Message
import mrj.info.bd.nidsms.services.Database.sendTransactionsToServer
import mrj.info.bd.nidsms.utils.Constants

class SMSReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val sms = intent.extras?.get("pdus") as Array<*>
        for (i in sms.indices) {
            val smsMessage = SmsMessage.createFromPdu(sms[i] as ByteArray)
            val sender = smsMessage.displayOriginatingAddress
            val message = smsMessage.displayMessageBody
            println("SMS from $sender: $message")
            //building message object
            val messageModel = Message(
                message[6].toString(),
                message[14].toString(),
                message[4].toString().replace(",", ""),
                message[16] + " " + message[17],
                message,
            )
            //sending message to server
            val params = Constants.inputToJsonObject(messageModel)
            sendTransactionsToServer(context, params) { response ->
                val success = response?.getString("message")
                Toast.makeText(context, success, Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Sample Message:
     * You have received Tk 3,560.00 from 01700000000. Fee Tk 0.00. Balance Tk 00,000.20. TrxID 9DG2AIJUXX at 16/04/2022 11:54
     **/

}