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
import io.javalin.Javalin;
import io.javalin.http.Context;

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

    //Login to account
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

    //Create a message
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

    //Get all messages
    private void getAllMessagesHandler(Context ctx)
    {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    //Get a specific message
    private void getMessageByIDHandler(Context ctx)
    {
        //Get message_id from path
        int message_id = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("message_id")));
        //Get messages
        Message message = messageService.getMessageByID(message_id);
        //If no message return 200 and a blank json object
        if (message == null)
        {
            ctx.status(200);
            ctx.json("");
        }
        //Return message
        else
        {
            ctx.json(message);
        }
    }

    //Delete a message
    private void deleteMessageHandler(Context ctx)
    {
        //Get message_id from path
        int message_id = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("message_id")));
        //Delete message
        Message message = messageService.removeMessageByID(message_id);
        //Return 200 and an empty object if no message
        if (message == null)
        {
            ctx.status(200);
            ctx.json("");
        }
        //Return deleted message
        else
        {
            ctx.json(message);
        }
    }

    //Update a message
    private void updateMessageHandler(Context ctx) throws JsonProcessingException
    {
        //Get message_text from http body
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        //Get message_id from path
        int message_id = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("message_id")));
        message.setMessage_id(message_id);
        //Update message
        Message updatedMessage = messageService.updateMessage(message);

        //Client error if no message
        if (updatedMessage == null)
        {
            ctx.status(400);
        }
        //Return updated message
        else
        {
            ctx.json(updatedMessage);
        }
    }

    //Get all messages by a single poster
    private void getMessagesByAccountIDHandler(Context ctx)
    {
        //Get account_id from path and then get messages
        int account_id = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("account_id")));
        List<Message> messages = messageService.getMessagesByPoster(account_id);
        ctx.json(messages);
    }
}