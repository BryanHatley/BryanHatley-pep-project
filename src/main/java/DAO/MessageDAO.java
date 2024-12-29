package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO 
{
    //Get all messages from database
    public List<Message> getAllMessages()
    {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try
        {
            //Set up statement
            String sql = "SELECT * FROM message";
            PreparedStatement ps = connection.prepareStatement(sql);
            
            //Execute statement
            ResultSet rs = ps.executeQuery();
            
            //Return messages
            while(rs.next())
            {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                                  rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    //Get a message by its id
    public Message getMessageByID(int message_id)
    {
        Connection connection = ConnectionUtil.getConnection();

        try
        {
            //Set up statement
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            //Finish statement
            ps.setInt(1, message_id);

            //Execute statement
            ResultSet rs = ps.executeQuery();

            //Return the message
            while (rs.next())
            {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                                  rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                return message;
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }

    //Get all messages by a single poster
    public List<Message> getMessagesByPoster(int posted_by)
    {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try
        {
            //Set up statement
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            //Finish statement
            ps.setInt(1, posted_by);
            
            //Execute statement
            ResultSet rs = ps.executeQuery();

            //Return messages
            while (rs.next())
            {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                                  rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    //Create a new message
    public Message createMessage(Message message) 
    {
        Connection connection = ConnectionUtil.getConnection();

        try 
        {
            //Set up statement
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //Finish statement
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            //Insert new Message
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) 
            {
                int generated_message_id = (int) rs.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }
        catch (SQLException e) 
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    //Update a message
    public Message updateMessage(Message message) 
    {
        Connection connection = ConnectionUtil.getConnection();

        try 
        {
            //Set up statement
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            //Finish statement
            ps.setString(1, message.getMessage_text());
            ps.setInt(2, message.getMessage_id());

            //Update Message
            ps.executeUpdate();
            return getMessageByID(message.getMessage_id());
        }
        catch (SQLException e) 
        {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    //Delete a message by its ID
    public Message removeMessageByID(int message_id)
    {
        Connection connection = ConnectionUtil.getConnection();

        try
        {
            //Set up statement and get message if it exists
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            Message message = getMessageByID(message_id);

            //Finish statement and delete message
            ps.setInt(1, message_id);
            ps.executeUpdate();
            return message;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
