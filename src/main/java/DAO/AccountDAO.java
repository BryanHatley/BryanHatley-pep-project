package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO 
{

    public Account getAccount(String username, String password)
    {
        Connection connection = ConnectionUtil.getConnection();

        try 
        {
            //Prep Statement
            String sql = "";
            PreparedStatement ps = connection.prepareStatement(sql);

            //Insert Statement Values
            ps.setString(1, username);
            ps.setString(2, password);
            
            //Query DB
            ResultSet rs = ps.executeQuery();

            //Assemble account object from result
            while (rs.next())
            {
                Account account = new Account(rs.getInt("account_id"),
                                                rs.getString("username"),
                                                rs.getString(password));
                return account;
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account createAccount(Account account) 
    {
        Connection connection = ConnectionUtil.getConnection();

        try 
        {
            //Set up statement
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);

            //Finish statement
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            //Insert new Account
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) 
            {
                int generated_account_id = (int) rs.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }
        catch (SQLException e) 
        {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}
