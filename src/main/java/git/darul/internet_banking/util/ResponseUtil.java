package git.darul.internet_banking.util;

import git.darul.internet_banking.dto.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static <T> ResponseEntity<CommonResponse<T>> buildResponse(HttpStatus status, String message, T data) {
        CommonResponse<T> responseEntity = new CommonResponse<>(status.value(), message, data);
        return ResponseEntity.status(status).body(responseEntity);
    }
}
