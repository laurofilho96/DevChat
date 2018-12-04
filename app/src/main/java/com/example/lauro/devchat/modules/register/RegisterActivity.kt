package com.example.lauro.devchat.modules.register

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.lauro.devchat.R
import com.example.lauro.devchat.modules.login.LoginActivity
import com.example.lauro.devchat.modules.messagelist.MessageListActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

//Tela de registro, Cadastro
class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //Quando clicar no botão de registrar...
        btn_register_register.setOnClickListener {
            registrar()
        }
        //Quando clicar no texto de conta
        tv_register_account.setOnClickListener{
            //Registrar no console.
            Log.d("RegisterActivity", "Vá para a tela de login.")
            //levar o usuário para a tela de login.
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        //Quando apertar o botão de imagem...
        btn_register_image.setOnClickListener{
            //Registra no console
            Log.d("RegisterActivity", "Pegar uma foto da galeria.")

            //vai chamar a intent action pack, ela irá catar todas as pastas com imagens.
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var uriFoto: Uri? = null

    //Isso daqui vai ser chamado quando terminar a intent aí em cima...Ele vai guardar o dado obtido na intent
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 &&
                resultCode == Activity.RESULT_OK &&
                data != null) {
            //Checar qual imagem foi selecionada.
            Log.d("RegisterActivity", "Imagem selecionada.")


            uriFoto = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uriFoto)
            ci_register_image.setImageBitmap(bitmap)
            btn_register_image.alpha = 0f
            //val bitmapDrawable = BitmapDrawable(bitmap)
            //btn_register_image.setBackgroundDrawable(bitmapDrawable)
        }
    }

    //Método de registrar usuário.
    private fun registrar() {

        //Definir as variáveis como strings
        val email = et_register_email.text.toString()
        val rSenha = et_register_password.text.toString()

        if(email.isEmpty() || rSenha.isEmpty()){

            Toast.makeText(this, "Insira um email e senha, por favor.", Toast.LENGTH_SHORT).show()
            return
        }

        //Apresenta-las no console
        Log.d("RegisterActivity", "email é " + email)
        Log.d("RegisterActivity", "Senha é $rSenha")

        //Criar um usuário com email e senha no firebase.
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, rSenha)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    //Se der certo.
                    Log.d("RegisterActivity",
                            "Usuário criado com sucesso: ${it.result?.user?.uid}")

                    mandarImagemProFirebase()

                    Toast.makeText(this, "Usuário Criado com Sucesso!! :)", Toast.LENGTH_SHORT).show()
                }
                //Se der errado...
                .addOnFailureListener{
                    Log.d("RegisterActivity", "Não foi possível criar usuário: ${it.message}")
                    Toast.makeText(this, "Não foi possível criar usuário:${it.message}", Toast.LENGTH_SHORT).show()
                }
    }

    private fun mandarImagemProFirebase(){

        if (uriFoto == null) return
        //O nome do arquivo vai ser uma string aleatória :v
        val nomeDoArquivo = UUID.randomUUID().toString()
        //Acessando firebaseStorage, tamo dizendo que todas as imagens serão salvas nessa pasta, gravando o nome tbm.
        val ref = FirebaseStorage.getInstance().getReference("/images/$nomeDoArquivo")

        ref.putFile(uriFoto!!)
                .addOnSuccessListener {
                    Log.d("RegisterActivity", "Imagem salva, danada!!: ${it.metadata?.path}")
                    //Apenas ta verificando se deu certo e passando de volta o link que  foi redirecionado.
                    ref.downloadUrl.addOnSuccessListener{
                        it.toString()
                        Log.d("RegisterActivity", "Pasta: $it")

                        salvarUsuarioNoBanco(it.toString())
                    }
                }
                .addOnFailureListener {
                    Log.d("RegisterActivity", "Falha em mandar imagem :(")
                }
    }

    private fun salvarUsuarioNoBanco(perfilUrl: String){

        val uid = FirebaseAuth.getInstance().uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid,
                et_register_username.text.toString(),
                perfilUrl)

        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("RegisterActivity", "Salvando Usuário no banco!")
                    //Isso daqui faz com que o usuário vá para a tela de mensagens logo.
                    val intent = Intent(this, MessageListActivity::class.java)
                    //Isso previne ele de voltar para a página de registro quando apertar o botão de voltar
                    //Ele vai para a tela inicial.
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
    }
}

    class User(val uid:String, val username: String, val perfilUrl: String){
        constructor(): this("","","")
    }