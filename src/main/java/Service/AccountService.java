package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService 
{
    public AccountDAO accountDAO;

    //no args constructor
    public AccountService()
    {
        accountDAO = new AccountDAO();
    }

    //Constructor for preexisiting dao object
    public AccountService(AccountDAO accountDAO)
    {
        this.accountDAO = accountDAO;
    }
    
    //Create an Account
    public Account createAccount(Account account)
    {
        //Make sure username is not blank
        if (account.getUsername().isBlank())
        {
            return null;
        } 
        //Ensure password is 4 characters or longer
        else if (account.getPassword().isBlank() || account.getPassword().length() < 4)
        {
            return null;
        }
        //Create new account
        else 
        {
            return accountDAO.createAccount(account);
        }
    }

    //Login to Account
    public Account loginAccount(Account account)
    {
        return accountDAO.getAccount(account.getUsername(), account.getPassword());
    }
}
