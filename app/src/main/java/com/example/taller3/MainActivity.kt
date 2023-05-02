package com.example.taller3

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.taller3.databinding.ActivityMainBinding

private fun logIn(log:EditText, pass:EditText, cont: Context){

    val user = log.text.toString()
    val passw = pass.text.toString() //getting username and password
    //TODO
    //create the validation for user and password required (prob. firebase as the main DB)
    if(user != "" && passw != ""){
        Toast.makeText(cont, "¡Hola, $user!", Toast.LENGTH_LONG).show()
        goToMenuActivity(cont)
    }else{
        log.setText("")
        pass.setText("")
        Toast.makeText(cont,"Ingrese usuario y contraseña",Toast.LENGTH_LONG).show()
    }

}

private fun register(log:EditText,pass:EditText,cont:Context){
    log.setText("")
    pass.setText("")
    val intent = Intent(cont, Register::class.java)
    cont.startActivity(intent)
}

private fun goToMenuActivity(cont: Context) {
    val intent = Intent(cont, Menu::class.java)
    cont.startActivity(intent)
}

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    companion object{
        const val GALLERY_REQUEST = 0
        const val CAMERA_REQUEST = 1
        const val PICK_IMAGE = 8
        const val CONTACTS_REQUEST =0
        const val ACCESS_FINE_LOCATION = 1
        const val ACCESS_COARSE_LOCATION = 2
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun validateForm(): Boolean {
        var valid = true
        val email = binding.user.text.toString()
        if (TextUtils.isEmpty(email)) {
            binding.user.error = "Required."
            valid = false
        } else {
            binding.user.error = null
        }
        val password = binding.password.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.password.error = "Required."
            valid = false
        } else {
            binding.password.error = null
        }
        return valid
    }

    private fun isEmailValid(email: String): Boolean {
        if (!email.contains("@") ||
            !email.contains(".") ||
            email.length < 5){
            return false
        }
        return true
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val intent = Intent(this, Menu::class.java)
            intent.putExtra("user", currentUser.email)
            startActivity(intent)
        } else {
            binding.user.setText("")
            binding.password.setText("")
        }
    }

    private fun signInUser(email: String, password: String){
        println( "" + email + " " + password)
        Log.e(TAG, "" + email + " " + password)
        if(validateForm() && isEmailValid(email)){
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
// Sign in success, update UI
                        Log.d(TAG, "signInWithEmail:success:")
                        val user = auth.currentUser
                        updateUI(auth.currentUser)
                        goToMenuActivity(this)

                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide() //hiding the large purple bar
        auth = Firebase.auth
        var log = findViewById<EditText>(R.id.user)
        var pass = findViewById<EditText>(R.id.password)
        var username = binding.user.text.toString()
        var authpass = binding.password.text.toString()
        var logger = findViewById<Button>(R.id.Log_in)
        var reg = findViewById<Button>(R.id.registermain)
        logger.setOnClickListener {
            //logIn(log,pass,this)
            auth.signInWithEmailAndPassword(log.text.toString(), pass.text.toString()).addOnCompleteListener(this){task ->
                Log.d(TAG, "signInWithEmail:onComplete: " + task.isSuccessful)
                if(!task.isSuccessful) {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        this, "Authentication failed. ",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.user.setText("")
                    binding.password.setText("")
                }else{
                    signInUser(log.text.toString(), pass.text.toString())
                }
            }
        }
        reg.setOnClickListener{
            register(log,pass,this)
        }
    }
}