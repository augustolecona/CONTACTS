

class models {


    public class Address {

        var street: String = ""
        var  city: String = ""
        var  state: String = ""
        var  country: String = ""
        var  zipCode: String = ""

    }


    data class Contacts (

        var  name: String = "",
        var  id: String = "",
        var  companyName: String = "",
        var isFavorite: Boolean = false,
        var  smallImageURL: String = "",
        var  largeImageURL: String = "",
        var  emailAddress: String = "",
        var  birthdate: String = "",
        var phone: Phone =  Phone(),
        var  address: Address = Address()

    )


    public class Phone {

        var  work: String = ""
        var  home: String = ""
        var  mobile: String = ""

    }
}