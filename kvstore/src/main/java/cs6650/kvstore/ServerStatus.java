package cs6650.kvstore;

public class ServerStatus {
    enum Status {
        Crashed,
        // InGet,
        // // 2pc status
        // Init,
        // Ready,
        // Abort,
        // Commit,

        // tcp manager status 
        Busy,
        Idle,
    }

    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
