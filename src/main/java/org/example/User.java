package org.example;

public class User {
    private long id;
    private String [] parameters;
    private int status;
    private int index;
    public User(long id){
        this.id =id;
        this.parameters= new String[Constants.MAX_PARAMETER];
        this.status=Constants.GET_WIDTH_LEVEL;
        this.index=0;
    }
    public void addParameter(String toAdd){
        this.parameters[index]=toAdd;
        index++;
    }
    public boolean isEqualChatId(long toCheck){//מביאה לו id// והוא בודק אם id תואם לו
        return this.id == toCheck;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String[] getParameters() {
        return parameters;
    }
}
