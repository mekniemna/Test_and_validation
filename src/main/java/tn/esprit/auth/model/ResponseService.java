package tn.esprit.auth.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ResponseService<E> {
	private static final int SUCCESS_CODE = 200;
	private static final String SUCCESS_MESSAGE = "Success";
	private static final String FAILED_MESSAGE = "failed";
	private static final int ERROR_CODE = 400;

	private static Logger logger = LogManager.getLogger(ResponseService.class);

	public Response<E> getSuccessResponse(E body) {
		Response<E> response = new Response<>(SUCCESS_CODE, SUCCESS_MESSAGE, body);
		logger.info("getSuccessResponse: response = " + response);
		return response;
	}
	
	public Response<E> getFailedResponse(String message) {
		if(message == null)
			message = FAILED_MESSAGE;
		Response<E> response = new Response<>(ERROR_CODE, message, null);
		logger.info("getFailedResponse: response = " + response);
		return response;
	}
}
