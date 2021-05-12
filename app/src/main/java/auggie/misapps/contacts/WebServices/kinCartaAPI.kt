package auggie.misapps.contacts.WebServices

import io.reactivex.Observable
import retrofit2.http.GET


interface kinCartaAPI {


    @GET("technical-challenge/v3/contacts.json")
    fun getContats(): Observable<ArrayList<models.Contacts>>
}



