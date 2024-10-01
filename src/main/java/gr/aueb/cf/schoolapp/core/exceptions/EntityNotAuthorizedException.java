package gr.aueb.cf.schoolapp.core.exceptions;

public class EntityNotAuthorizedException extends EntityGenericException {

    private static final String DEFAULT_CODE = "AlreadyExists";

    public EntityNotAuthorizedException(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}

