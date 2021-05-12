package auggie.misapps.contacts.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import auggie.misapps.contacts.database.DatabaseHelper
import auggie.misapps.contacts.R
import kotlinx.android.synthetic.main.elementview.view.*
import models


class ContactslistAdapter(
    val context: Context,
    val contacts: ArrayList<models.Contacts>,
    val  isFavorite:Boolean
   // val dataBaseHelper: DatabaseHelper
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((models.Contacts) -> Unit)? = null

    override fun getItemCount(): Int {
            return contacts.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.elementview, parent, false)
        )

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as ViewHolder).bind(contacts[position], context)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contact: models.Contacts?, context: Context) {
            itemView.contactName.text = contact!!.name
            Glide.with(context).load(contact.largeImageURL).placeholder(R.mipmap.user_icon_small).into(itemView.contacttImage)
            itemView.company_name.text = contact.companyName

            if (isFavorite){
               itemView.favorite.setImageResource(R.mipmap.favoritetrue)
            }
            else itemView.favorite.setImageBitmap(null)





        }

        init {
            itemView.setOnClickListener {
                  onItemClick?.invoke(contacts[adapterPosition])
            }
        }
    }

}

