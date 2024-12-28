package Controller;

import static org.mockito.ArgumentMatchers.nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController()
    {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
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
        app.post("/messages", this::postCreateMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountIDHandler);

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
            ctx.status(401);
        }
        else
        {
            ctx.json(mapper.writeValueAsString(loggedInAccount));
        }
    }

    private void postCreateMessageHandler(Context ctx) throws JsonProcessingException
    {
        //Parse JSON input to a Message object, then add object to database
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message newMessage = messageService.createMessage(message);
        
        //Return Message to user as JSON object or return a status code of 400
        if (newMessage == null)
        {
            ctx.status(400);
        }
        else
        {
            ctx.json(mapper.writeValueAsString(newMessage));
        }
    }

    private void getAllMessagesHandler(Context ctx)
    {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void getMessageByIDHandler(Context ctx)
    {
        int message_id = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("message_id")));
        Message message = messageService.getMessageByID(message_id);
        if (message == null)
        {
            ctx.status(200);
            ctx.json("");
        }
        else
        {
            ctx.json(message);
        }
    }

    private void deleteMessageHandler(Context ctx)
    {
        int message_id = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("message_id")));
        Message message = messageService.removeMessageByID(message_id);
        if (message == null)
        {
            ctx.status(200);
            ctx.json("");
        }
        else
        {
            ctx.json(message);
        }
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("message_id")));
        message.setMessage_id(message_id);
        Message updatedMessage = messageService.updateMessage(message);

        if (updatedMessage == null)
        {
            ctx.status(400);
        }
        else
        {
            ctx.json(updatedMessage);
        }
    }

    private void getMessagesByAccountIDHandler(Context ctx)
    {
        int account_id = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("account_id")));
        List<Message> messages = messageService.getMessagesByPoster(account_id);
        ctx.json(messages);
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


}