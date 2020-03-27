import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mstokluska.chattie.R
import com.mstokluska.chattie.adapters.ChatListener
import com.mstokluska.chattie.adapters.MessagesListener
import com.mstokluska.chattie.models.ChatModel
import com.mstokluska.chattie.models.MessageModel
import kotlinx.android.synthetic.main.card_chat.view.*
import kotlinx.android.synthetic.main.card_message.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


class MessageAdapter constructor(private var messages: List<MessageModel>,
                                 private var listener: MessagesListener ) : RecyclerView.Adapter<MessageAdapter.MainHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_message, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {

        val message = messages[holder.adapterPosition]

        holder.bind(message, listener)
    }

    override fun getItemCount(): Int = messages.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(message: MessageModel, listener: MessagesListener) {

            itemView.message.text = message.content
            itemView.messageSender.text = message.mcreator
            itemView.setOnClickListener { listener.onMessageClick(message) }

        }
    }
}