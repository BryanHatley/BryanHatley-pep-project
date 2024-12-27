package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService 
{
    public MessageDAO messageDAO;

    public MessageService()
    {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO)
    {
        messageDAO = new MessageDAO();
    }

    public List<Message> getAllMessages()
    {
        return messageDAO.getAllMessages();
    }
    
    public Message getMessageByID(int message_id)
    {
        return messageDAO.getMessageByID(message_id);
    }

    public List<Message> getMessagesByPoster(int posted_by)
    {
        return messageDAO.getMessagesByPoster(posted_by);
    }

    public Message createMessage(Message message)
    {
        if (message.getMessage_text().isBlank() || message.getMessage_text().length() > 255)
        {
            return null;
        }
        else if (message.getPosted_by() < -1)
        {
            return null;
        }
        return messageDAO.createMessage(message);
    }

    public Message removeMessageByID(int message_id)
    {
        return messageDAO.removeMessageByID(message_id);
    }
    
    public Message updateMessage(Message message)
    {
        if (message.getMessage_text().isBlank() || message.getMessage_text().length() > 255)
        {
            return null;
        }
        return messageDAO.updateMessage(message);
    }
}
