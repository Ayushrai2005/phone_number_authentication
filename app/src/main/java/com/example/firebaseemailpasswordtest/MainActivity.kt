package com.example.firebaseemailpasswordtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.auth
import java.util.concurrent.TimeUnit
import kotlin.math.sign

class MainActivity : AppCompatActivity() {
    private lateinit var edtPhoneNumber: EditText
    private lateinit var edtVerifyOtp: EditText
    private lateinit var btnSendOtp: Button
    private lateinit var btnVerifyOtp: Button

    var verificationId = ""

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtPhoneNumber = findViewById(R.id.edt_phone_number)
        edtVerifyOtp = findViewById(R.id.edt_otp)
        btnSendOtp = findViewById(R.id.btn_getOtp)
        btnVerifyOtp = findViewById(R.id.btn_verify_otp)

        mAuth = Firebase.auth


        btnSendOtp.setOnClickListener {
            val number = "+91${edtPhoneNumber.text}".toString()
            //send verification code to user's number

            sendVerificationCode(number)

        }

        btnVerifyOtp.setOnClickListener {
            val otp = edtVerifyOtp.text.toString()
            verifyCode(otp)
        }


    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(verificationCallBack)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)


    }

    val verificationCallBack: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {

            }

            override fun onCodeSent(s: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(s, p1)
                verificationId = s
            }

        }


    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)

        signInWithCred(credential)


    }

    private fun signInWithCred(phoneAuthCredential: PhoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)

            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Phone Number Verified Successfully", Toast.LENGTH_SHORT)
                        .show()

                    startActivity(
                        Intent(this, HomeActivity::class.java)
                    )
                }else{
                    Toast.makeText(this, "Incorrect OTP", Toast.LENGTH_SHORT)


                }

            }

    }

}








