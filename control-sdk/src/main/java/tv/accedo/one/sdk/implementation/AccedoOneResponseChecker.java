package tv.accedo.one.sdk.implementation;

import tv.accedo.one.sdk.model.AccedoOneException;
import tv.accedo.one.sdk.model.AccedoOneException.StatusCode;
import tv.accedo.one.sdk.implementation.utils.Response;
import tv.accedo.one.sdk.implementation.utils.Request.ResponseChecker;

/**
 * @author Pásztor Tibor Viktor <tibor.pasztor@accedo.tv>
 */
class AccedoOneResponseChecker implements ResponseChecker<AccedoOneException>{
    @Override
    public void throwIfNecessary(Response response) throws AccedoOneException {
        switch (response.getCode()){
            case -1: throw new AccedoOneException(StatusCode.NO_RESPONSE, response.getCaughtException());
            case 200: break;
            case 201: break;
            case 204: break;
            case 304: break;
            case 400: throw new AccedoOneException(StatusCode.INVALID_PARAMETERS, response.getText(), response.getCaughtException());
            case 404: throw new AccedoOneException(StatusCode.KEY_NOT_FOUND, response.getText(), response.getCaughtException());
            default: throw new AccedoOneException(AccedoOneException.StatusCode.INVALID_RESPONSE, response.getText(), response.getCaughtException());
        }
    }
}