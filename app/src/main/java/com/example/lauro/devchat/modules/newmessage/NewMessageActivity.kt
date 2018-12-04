  package com.example.lauro.devchat.modules.newmessage

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Adapter
import android.widget.Toast
import com.example.lauro.devchat.R
import com.example.lauro.devchat.adapters.MyAdapter
import com.example.lauro.devchat.models.ChatModel
import com.example.lauro.devchat.modules.register.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.new_message_model.view.*

  //RecyclerView por aqui
  class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        //Mudando o título da activity
        supportActionBar?.title = "Selecionar Contato"

        //Recycler View
        rv_nova_mensagem.setHasFixedSize(true)
        rv_nova_mensagem.layoutManager = LinearLayoutManager(this)

        val chats:ArrayList<ChatModel> = ArrayList<ChatModel>()

        for(i in 1..10) {

            chats.add(ChatModel("Usuario ${i}"))
            val ref = FirebaseDatabase.getInstance().getReference("/users")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(p0: DataSnapshot){

                    p0.children.forEach{
                        Log.d("Nova Mensagem", it.toString())
                        val user = it.getValue(User::class.java)
                        if(user != null){
                            chats.add(UserItem(user))
                        }
                    }
                }
            })
        }

        val mAdapter:RecyclerView.Adapter<*> = MyAdapter(this@NewMessageActivity,chats){

            Toast.makeText(this@NewMessageActivity,
                    it.toString(),
                    Toast.LENGTH_LONG).show()
        }
        rv_nova_mensagem.adapter = mAdapter

        buscarUsuarios()

    }

      private fun buscarUsuarios(){
          val ref = FirebaseDatabase.getInstance().getReference("/users")
          ref.addListenerForSingleValueEvent(object :ValueEventListener{

          override fun onDataChange(p0: DataSnapshot){

              p0.children.forEach{
                  Log.d("Nova Mensagem", it.toString())
                  val user = it.getValue(User::class.java)
                  if(user != null){
                      chats.add(UserItem(user))
                  }
              }

          }

          override fun onCancelled(p0: DatabaseError){}
          })
      }
}

  class UserItem(val user:User): RecyclerView.Adapter<MyAdapter.ViewHolder>(){

       fun bind(viewHolder: RecyclerView.ViewHolder, position: Int) {
          viewHolder.itemView.tv_nome_mensagem.text = user.username

          Picasso.get().load(user.perfilUrl).into(viewHolder.itemView.iv_imagem_perfil)
      }

      fun getLayout(): Int {
          return R.layout.new_message_model
      }
  }
