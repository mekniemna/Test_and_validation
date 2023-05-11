package tn.esprit.auth.model;

public class Response<E> {
	private int code;
    private String message;
    private E body;
    
	public Response(int code, String message, E body) {
		this.code = code;
		this.message = message;
		this.body = body;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public E getBody() {
		return body;
	}
	public void setBody(E body) {
		this.body = body;
	}

    
}
