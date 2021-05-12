package auggie.misapps.contacts.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import auggie.misapps.contacts.R
import auggie.misapps.contacts.Util
import auggie.misapps.contacts.database.DatabaseHelper
import auggie.misapps.contacts.view.adapters.ContactslistAdapter
import com.google.gson.Gson
import io.reactivex.plugins.RxJavaPlugins
import models


class MainActivity : AppCompatActivity() {

    private lateinit var dataBaseHelper: DatabaseHelper
    lateinit var recycleFavourites: RecyclerView
    lateinit var recycleOthers: RecyclerView
    var allcontactslist = ArrayList<models.Contacts>()
    var favContactslist = ArrayList<models.Contacts>()
    var otherContactslist = ArrayList<models.Contacts>()
    lateinit var adapterFav: ContactslistAdapter
    lateinit var adapterOthers: ContactslistAdapter
    lateinit var textView: TextView
    lateinit var progressbarlayout: View
    lateinit var modelView: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        castViews()

        dataBaseHelper = DatabaseHelper(this)

        adapterFav = ContactslistAdapter(
            this,
            favContactslist,
            true
        )
        recycleFavourites.adapter = adapterFav


        adapterFav.onItemClick = { contact ->
            val intent = Intent(this, Contact::class.java)
            val gson = Gson()
            val contactjson = gson.toJson(contact)
            intent.putExtra("contactjson", contactjson)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }


        adapterOthers = ContactslistAdapter(
            this,
            otherContactslist,
            false
        )
        recycleOthers.adapter = adapterOthers


        adapterOthers.onItemClick = { contact ->
            val intent = Intent(this, Contact::class.java)
            val gson = Gson()
            val contactjson = gson.toJson(contact)
            intent.putExtra("contactjson", contactjson)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }


        modelView = ViewModelProviders.of(this).get(ViewModel::class.java)
        modelView.getData()
        val nameObserver = Observer<ArrayList<models.Contacts>> { list ->
            allcontactslist.clear()
            allcontactslist.addAll(list)
            updatelists()
            progressbarlayout.visibility = View.INVISIBLE
        }
        modelView.getObservable().observe(this, nameObserver)

        progressbarlayout.visibility = View.VISIBLE

        handleRxErrors()


    }


    override fun onResume() {
        super.onResume()
        updatelists()
    }

    private fun handleRxErrors() {

        RxJavaPlugins.setErrorHandler { throwable ->
            throwable.printStackTrace()
            progressbarlayout.visibility = View.INVISIBLE
            Toast.makeText(
                this,
                Util.showError(throwable), Toast.LENGTH_LONG
            ).show()
            Log.e(this@MainActivity.javaClass.name, throwable.message)

        }
    }

    private fun updatelists() {
        for (element in allcontactslist) {
            element.isFavorite = dataBaseHelper.existsContactsFav(element.id.toInt())
        }
        favContactslist.clear()
        favContactslist.addAll(allcontactslist.filter { x -> x.isFavorite })
        adapterFav.notifyDataSetChanged()
        otherContactslist.clear()
        otherContactslist.addAll(allcontactslist.filter { x -> !x.isFavorite })
        adapterOthers.notifyDataSetChanged()
    }


    private fun castViews() {
        textView = findViewById(R.id.textView)
        recycleFavourites = findViewById<RecyclerView>(R.id.recyclerView)
        recycleOthers = findViewById<RecyclerView>(R.id.recyclerView2)
        recycleFavourites.setHasFixedSize(true)
        recycleOthers.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recycleFavourites.setLayoutManager(linearLayoutManager)
        val linearLayoutManager2 = LinearLayoutManager(this)
        linearLayoutManager2.orientation = LinearLayoutManager.VERTICAL
        recycleOthers.setLayoutManager(linearLayoutManager2)
        progressbarlayout = findViewById(R.id.llProgressBar)

    }


}