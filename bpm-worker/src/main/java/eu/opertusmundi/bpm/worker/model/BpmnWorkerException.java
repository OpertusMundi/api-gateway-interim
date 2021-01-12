package eu.opertusmundi.bpm.worker.model;

import eu.opertusmundi.common.model.MessageCode;
import eu.opertusmundi.common.model.ServiceException;
import lombok.Builder;
import lombok.Getter;

public class BpmnWorkerException extends ServiceException {

    private static final long serialVersionUID = 1L;

    @Getter
    private String errorDetails;

    @Getter
    private int retries;

    @Getter
    private long retryTimeout = 0;

    @Builder
    private BpmnWorkerException(
        MessageCode code, String message, String errorDetails, int retries, long retryTimeout
    ) {
        super(code, message);

        this.errorDetails = errorDetails;
        this.retries      = retries;
        this.retryTimeout = retryTimeout;
    }

    public BpmnWorkerException(MessageCode code) {
        super(code, "Operation failed");
    }

    public BpmnWorkerException(MessageCode code, String message) {
        super(code, message);
    }

    public BpmnWorkerException(MessageCode code, String message, Throwable cause) {
        super(code, message, cause);
    }

}
