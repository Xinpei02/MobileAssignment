package Admin

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileassignment.R
import com.google.firebase.firestore.FirebaseFirestore
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder

class MyAdapter(private val userList : ArrayList<User>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    interface OnUpdateListener { // Step 1: Define an interface
        fun onUpdateSuccess()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_item,
            parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        val currentitem = userList[position]

        holder.name.text = currentitem.name
        holder.email.text = currentitem.email

        holder.btnEdit.setOnClickListener {
            val dialogPlus = DialogPlus.newDialog(holder.itemView.context)
                .setContentHolder(ViewHolder(R.layout.update_popup))
                .setExpanded(true, 1200)
                .create()

            //dialogPlus.show()

            val view = dialogPlus.holderView

            val editName = view.findViewById<EditText>(R.id.txtEditName)
            val editEmail = view.findViewById<EditText>(R.id.txtEditEmail)

            val btnUpdate = view.findViewById<Button>(R.id.btnUpdate)

            editName.setText(currentitem.name)
            editEmail.setText(currentitem.email)

            dialogPlus.show()


            btnUpdate.setOnClickListener {
                val db = FirebaseFirestore.getInstance()
                val userDetail: Map<String, Any> = mapOf(
                    "name" to editName.text.toString(),
                    "email" to editEmail.text.toString()
                )

                val userEmail = currentitem.email // Assuming "email" is the unique field
                val usersCollection = db.collection("users")

                usersCollection
                    .whereEqualTo("email", userEmail)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val documentReference = usersCollection.document(document.id)
                            documentReference
                                .update(userDetail)
                                .addOnSuccessListener {
                                    Toast.makeText(holder.name.context, "Data Updated Successfully.", Toast.LENGTH_SHORT).show()
                                    dialogPlus.dismiss()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(holder.name.context, "Error while updating: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(holder.name.context, "Error while querying: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        holder.btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(holder.name.context)
            builder.setTitle("Are you sure?")
            builder.setMessage("Delete data can't be undone.")

            builder.setPositiveButton("Delete") { dialog, which ->
                val db = FirebaseFirestore.getInstance()
                val userEmail = currentitem.email // Assuming "email" is the unique field

                db.collection("users")
                    .whereEqualTo("email", userEmail)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            db.collection("users")
                                .document(document.id)
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        holder.name.context,
                                        "Data Deleted Successfully.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        holder.name.context,
                                        "Error while deleting: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            holder.name.context,
                            "Error while querying: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                Toast.makeText(holder.name.context, "Cancelled", Toast.LENGTH_SHORT).show()
            }

            builder.show()
        }
    }
    override fun getItemCount(): Int {
        return userList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.tvName)
        val email : TextView = itemView.findViewById(R.id.tvEmail)

        val btnEdit : Button = itemView.findViewById(R.id.editBtn)
        val btnDelete : Button = itemView.findViewById(R.id.deleteBtn)
    }

}
