package com.example.contactlist

/**A helper class for the Contact class to build the appropriate constructor depending on giving parameters*/
class ContactConstruct {
    private var output:Contact = Contact("hello")

    fun process(id:Int, args:Array<String?>):Contact{
        return output
    }

    fun process(id:Int, args:Array<String?>, imgpath:String):Contact{
        return output
    }

    fun process(id:Int, args:Array<String?>, imgdata:ByteArray):Contact{
        return output
    }

}