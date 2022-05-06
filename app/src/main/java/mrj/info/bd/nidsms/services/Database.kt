package mrj.info.bd.nidsms.services

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import mrj.info.bd.nidsms.utils.Constants.DB_ADDRESS
import org.json.JSONObject

object Database {

    fun sendTransactionsToServer(context: Context, params: JSONObject, callback: (JSONObject?) -> Unit) {
        val url = DB_ADDRESS + "add_transaction"

        val request = JsonObjectRequest(Request.Method.POST, url, params, { response ->
            callback(response)
        }, { error ->
            callback(null)
            Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_LONG).show()
        })
        Volley.newRequestQueue(context).add(request)
    }
}