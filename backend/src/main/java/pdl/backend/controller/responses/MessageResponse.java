package pdl.backend.controller.responses;

public class MessageResponse {
    private String message;

    public MessageResponse(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    
}
