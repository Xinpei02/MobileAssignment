package Admin

import User.ResetPassword
import com.google.firebase.firestore.DocumentId

data class User(
    var documentId: String? = null,
    var name: String? = null,
    var email: String? = null,
    var contact: String? = null,
    var role: String? = null
)
