package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService 
{
    public MessageDAO messageDAO;

    //NoArgs Constructor
    public MessageService()
    {
        messageDAO = new MessageDAO();
    }

    //Constructor that takes a messageDao object
    public MessageService(MessageDAO messageDAO)
    {
        messageDAO = new MessageDAO();
    }

    //Get all messages
    public List<Message> getAllMessages()
    {
        return messageDAO.getAllMessages();
    }
    
    //Get a message by its id
    public Message getMessageByID(int message_id)
    {
        return messageDAO.getMessageByID(message_id);
    }

    //Get all messages from specific poster
    public List<Message> getMessagesByPoster(int posted_by)
    {
        return messageDAO.getMessagesByPoster(posted_by);
    }

    //Create a new message
    public Message createMessage(Message message)
    {
        //Check if message is blank or longer than 255 characters
        if (message.getMessage_text().isBlank() || message.getMessage_text().length() > 255)
        {
            return null;
        }
        //Check if Poster ID is valid
        else if (message.getPosted_by() < -1)
        {
            return null;
        }
        //Return list or null if poster does not exist
        return messageDAO.createMessage(message);
    }

    //Remove a message by its ID
    public Message removeMessageByID(int message_id)
    {
        return messageDAO.removeMessageByID(message_id);
    }
    
    //Update a message
    public Message updateMessage(Message message)
    {
        if (message.getMessage_text().isBlank() || message.getMessage_text().length() > 255)
        {
            return null;
        }
        return messageDAO.updateMessage(message);
    }
}
