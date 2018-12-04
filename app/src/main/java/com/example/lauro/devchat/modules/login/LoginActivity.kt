package com.example.lauro.devchat.modules.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.lauro.devchat.R
import com.example.lauro.devchat.modules.messagelist.MessageListActivity
import com.example.lauro.devchat.modules.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Botão de logar, quando apertar...
        btn_login_login.setOnClickListener {

            //Igualar as variáveis ao que foi escrito e defini-las como String.
            val lEmail = et_login_email.text.toString()
            val lSenha = pt_login_password.text.toString()

            //Mostrar no console o que foi escrito
            Log.d("LoginActivity", "Login é " + lEmail)
            Log.d("LoginActivity", "Senha é $lSenha")

            //Realizar o Login com o Firebase.
            FirebaseAuth.getInstance().signInWithEmailAndPassword(lEmail, lSenha)

            val intent = Intent(this, MessageListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
                    //.addOnCompleteListener()
                    //.add

        }
        //Quando clicar no texto de conta
        btn_login_register.setOnClickListener{
            //Registrar no console.
            Log.d("RegisterActivity", "Vá para a tela de Cadastro.")
            //levar o usuário para a tela de login.
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

}
