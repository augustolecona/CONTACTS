package auggie.misapps.contacts.view

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import auggie.misapps.contacts.R
import auggie.misapps.contacts.database.DatabaseHelper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_contact_view.*

var contact: models.Contacts = models.Contacts()

class Contact : AppCompatActivity() {


    private lateinit var dataBaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_view)
        dataBaseHelper = DatabaseHelper(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        try {
            val gson = Gson()
            contact = gson.fromJson<models.Contacts>(
                intent.getStringExtra("contactjson"), models.Contacts::class.java
            )
        } catch (e: Exception) {
            Log.e("#ERROR", e.message!!)
        }

        Glide.with(this).load(contact.largeImageURL).placeholder(R.mipmap.user_large)
            .into(ContactImage)
        ContactName.text = contact.name
        if (!contact.phone.home.isEmpty()) phone.text = contact.phone.home
        else phonetext.width = 0
        if (!contact.phone.mobile.isEmpty()) mobilephone.text = contact.phone.mobile
        //else mobilephonetext.apply { setVisible(false) }
        if (!contact.address.toString().isEmpty()) address.text = contact.address.toString()
        if (!contact.birthdate.isEmpty()) birthday.text = contact.birthdate
        if (!contact.emailAddress.isEmpty()) email.text = contact.emailAddress
        if (!contact.companyName.isEmpty()) companyname.text = contact.companyName

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)


        if (dataBaseHelper.existsContactsFav(contact.id.toInt())) {

            menu!!.getItem(0).setIcon(R.mipmap.favoritetrue)


        }

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            android.R.id.home -> {
                finish()
                return true
            }


            R.id.fav -> {


                if (dataBaseHelper.existsContactsFav(contact.id.toInt())) {
                    if (dataBaseHelper.deleteContact(contact.id)) {
                        item.setIcon(R.mipmap.favoritefalse)
                    } else {
                        Toast.makeText(
                            this,
                            "Hubo un error al borrar el contacto",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {

                    if (dataBaseHelper.SaveContact(contact)) {

                        item.setIcon(R.mipmap.favoritetrue)
                    } else {
                        Toast.makeText(
                            this,
                            "Hubo un error al guaardar el contacto",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}