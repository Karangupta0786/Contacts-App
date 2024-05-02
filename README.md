# Calling feature and MVVM dependency
// calling feature:<br/>

        val intent = Intent(Intent.ACTION_CALL)
        intent.setData(Uri.parse("tel:$number"))
