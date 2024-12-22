package Controller;

import static org.mockito.ArgumentMatchers.nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;

    public SocialMediaController()
    {
        this.accountService = new AccountService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postCreateAccountHandler);
        app.post("/login", this::postLoginAccountHandler);

        return app;
    }

    //Create an account in the database if requirement in service are met
    private void postCreateAccountHandler(Context ctx) throws JsonProcessingException
    {
        //Parse JSON input to an Account object, then add object to database
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account newAccount = accountService.createAccount(account);
        
        //Return account to user as JSON object or return a status code of 400
        if (newAccount == null)
        {
            ctx.status(400);
        }
        else
        {
            ctx.json(mapper.writeValueAsString(newAccount));
        }

    }

    private void postLoginAccountHandler(Context ctx) throws JsonProcessingException
    {
        //Parse JSON input to an Account object, then check if account is in database
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedInAccount = accountService.loginAccount(account);
         
        //Return account to user as JSON object or return a status code of 400
        if (loggedInAccount == null)
        {
            ctx.status(400);
        }
        else
        {
            ctx.json(mapper.writeValueAsString(loggedInAccount));
        }
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


}