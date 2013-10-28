package com.domsplace.Villages.Enums;

public class DeleteCause {
    public static final DeleteCause ADMIN_DELETE = new DeleteCause("Admin Deleted");
    public static final DeleteCause MAYOR_CLOSE = new DeleteCause("Mayor Closed");
    public static final DeleteCause UPKEEP_FAILED = new DeleteCause("Upkeep Failed");
    
    //Instance
    private String name;
    
    private DeleteCause(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}
