package auggie.misapps.contacts.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import auggie.misapps.contacts.WebServices.NetworkHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ViewModel (context: Application) : AndroidViewModel(context) {

    private var listaProyectos= MutableLiveData<ArrayList<models.Contacts>>()
   // private val Disposable= ArrayList<Disposable>()
   private val Disposable: CompositeDisposable? = CompositeDisposable()

    fun getData() {

        var obs = NetworkHelper.createGitHubAPI().getContats()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe{
                Disposable!!.add(it)
            }
            .doOnError {
                var t = 0
            }
            .doOnNext { data ->

                        listaProyectos.value= data

            }
            .subscribe()
    }


     fun getObservable(): MutableLiveData<ArrayList<models.Contacts>> {
        return listaProyectos
    }


    override fun onCleared() {
        Disposable!!.clear()
    }

}