package com.pointwest.bootcamp.eventhubri.core.exception.profile;

import com.pointwest.bootcamp.eventhubri.core.exception.EventhubException;
import com.pointwest.bootcamp.eventhubri.core.exception.enums.EventhubErrorCode;

public class ProfileImageUploadException extends EventhubException {
    public ProfileImageUploadException(String message, Throwable cause) {
        super(EventhubErrorCode.PROFILE_IMAGE_UPLOAD_FAILED, message, cause);
    }
}
