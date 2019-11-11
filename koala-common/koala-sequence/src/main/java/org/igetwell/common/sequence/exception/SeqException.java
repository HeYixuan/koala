package org.igetwell.common.sequence.exception;

/**
 * 序列号生成异常
 */
public class SeqException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SeqException(String message) {
		super(message);
	}

	public SeqException(Throwable cause) {
		super(cause);
	}

}
