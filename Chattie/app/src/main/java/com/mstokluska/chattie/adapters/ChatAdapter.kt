import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mstokluska.chattie.R
import com.mstokluska.chattie.adapters.ChatListener
import com.mstokluska.chattie.models.ChatModel
import kotlinx.android.synthetic.main.card_chat.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class ChatAdapter constructor(private var chats: List<ChatModel>, private val listener: ChatListener) : RecyclerView.Adapter<ChatAdapter.MainHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_chat, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {

        val chat = chats[holder.adapterPosition]

        holder.bind(chat, listener)
    }

    override fun getItemCount(): Int = chats.size

  class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(chat: ChatModel, listener: ChatListener) {

            itemView.chatWith.text = chat.receiver
            itemView.setOnClickListener { listener.onChatClick(chat) }
            itemView.image_delete.setOnClickListener { listener.onDeleteClick(chat) }

        }
    }
}