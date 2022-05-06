package mrj.info.bd.nidsms

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import mrj.info.bd.nidsms.models.Message
import mrj.info.bd.nidsms.services.Database.sendTransactionsToServer
import mrj.info.bd.nidsms.utils.Constants.inputToJsonObject
import mrj.info.bd.nidsms.utils.Constants.setProgressDialog

class MainActivity : AppCompatActivity() {

    private val sender: TextInputEditText by lazy { findViewById(R.id.sender) }
    private val transactionId: TextInputEditText by lazy { findViewById(R.id.transactionId) }
    private val taka: TextInputEditText by lazy { findViewById(R.id.taka) }
    private val sentTime: TextInputEditText by lazy { findViewById(R.id.sentTime) }
    private val message: TextInputEditText by lazy { findViewById(R.id.message) }
    private val submit: AppCompatButton by lazy { findViewById(R.id.submit) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_MMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS),
                1
            )
        } else {
            //show success message
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        }

        //submit
        submit.setOnClickListener {
            val dialog = setProgressDialog(this, "Loading..")
            dialog.show()

            val messageModel = Message(
                sender.text.toString(),
                transactionId.text.toString(),
                taka.text.toString(),
                sentTime.text.toString(),
                message.text.toString()
            )

            if (messageModel.sender.isEmpty() || messageModel.transactionId.isEmpty() || messageModel.taka.isEmpty() || messageModel.sentTime.isEmpty() || messageModel.message.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                val params = inputToJsonObject(messageModel)
                sendTransactionsToServer(this, params) { response ->
                    dialog.dismiss()
                    val success = response?.getString("message")
                    Toast.makeText(this, success, Toast.LENGTH_SHORT).show()
                    sender.setText("")
                    transactionId.setText("")
                    taka.setText("")
                    sentTime.setText("")
                    message.setText("")
                }
            }

        }
    }
}