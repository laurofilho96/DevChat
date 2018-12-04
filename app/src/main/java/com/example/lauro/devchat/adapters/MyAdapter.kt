package com.example.lauro.devchat.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lauro.devchat.R
import com.example.lauro.devchat.models.ChatModel
import kotlinx.android.synthetic.main.new_message_model.view.*

class MyAdapter constructor (val context: Context,
                             private val chats:ArrayList<ChatModel>,
                             val clickListener:(ChatModel)->Unit):
        RecyclerView.Adapter<MyAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){}


    // O que esse Boneco faz? Quando o cabra estiver acessando o recycler, ele visualiza o chatmodel
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.new_message_model,
                        parent, false)

        val vh = ViewHolder(v)
        return vh

    }
    //Esse boneco daqui mostra as informações do chat e quando a gente clica nele, informa o index do chat. TROCAR ESSE CLICK LISTENER PARA ABRIR CHAT!!
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        chats.let {

            var chat = chats[position]

            holder.itemView.tv_nome_mensagem.text = chat.title

            //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(holder.itemView.ivChatBackground);

            holder.itemView.setOnClickListener{clickListener(chats[position])}
        }
    }

    //Conta o número de itens no recyclerView (Meio óbvio, não?)
    override fun getItemCount(): Int {

        if (chats != null) {

            return chats.size
        }else{

            return 0
        }
    }
}