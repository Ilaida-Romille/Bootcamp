package com.pointwest.bootcamp.eventhubri.core.exception.profile;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class InvalidProfileImageException extends EventhubException {
    public InvalidProfileImageException(String message) {
        super(EventhubErrorCode.INVALID_PROFILE_IMAGE, message);
    }
}
