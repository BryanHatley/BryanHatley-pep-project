package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO 
{
    public List<Message> getAllMessages()
    {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try
        {
            String sql = "SELECT * FROM message";

            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

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

    public Message getMessageByID(int message_id)
    {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try
        {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery();

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

    public List<Message> getMessagesByPoster(int posted_by)
    {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try
        {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, posted_by);
            ResultSet rs = ps.executeQuery();

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

    public Message createMessage(Message message) 
    {
        Connection connection = ConnectionUtil.getConnection();

        try 
        {
            //Set up statement
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);

            //Finish statement
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            //Insert new Account
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

            //Insert new Account
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
    
    public Message removeMessageByID(int message_id)
    {
        Connection connection = ConnectionUtil.getConnection();

        try
        {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery();

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
}
