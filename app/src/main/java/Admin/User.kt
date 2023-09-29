package Admin

import com.google.firebase.firestore.DocumentId

data class User(
    var documentId: String? = null,
    var name: String? = null,
    var email: String? = null
)
