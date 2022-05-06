package mrj.info.bd.nidsms.models

class Message(
    var sender: String,
    var transactionId: String,
    var taka: String,
    var sentTime: String,
    var message: String
) {

    override fun toString(): String {
        return "Message(sender='$sender', transactionId='$transactionId', taka='$taka', sentTime='$sentTime', message='$message')"
    }
}