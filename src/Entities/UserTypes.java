package Entities;

public enum UserTypes {

    TEACHER (1),
    COVER_TEACHER (2),
    STUDENT (3);

    private final int typeID;
    UserTypes(int typeID) {
        this.typeID = typeID;
    }

    public int getID(){
        return typeID;
    }

    public static UserTypes getTypeByID(int typeID){
        return values()[typeID - 1];
    }

}
