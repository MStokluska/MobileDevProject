import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mstokluska.chattie.R
import com.mstokluska.chattie.models.ChatModel
import kotlinx.android.synthetic.main.card_chat.view.*


class ChatAdapter constructor(private var chats: List<ChatModel>) : RecyclerView.Adapter<ChatAdapter.MainHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_chat, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val chats = chats[holder.adapterPosition]
        holder.bind(chats)
    }

    override fun getItemCount(): Int = chats.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(chat: ChatModel) {
            itemView.chatWith.text = chat.receiver
            itemView.lastMessage.text = chat.sender
        }
    }
}