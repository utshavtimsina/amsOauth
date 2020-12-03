package com.aeon.ams.config;

import com.aeon.ams.pojo.ApiError;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class CustomizedExceptionHandling extends ResponseEntityExceptionHandler {


    // 400
//    apiError

    private final CustomMessageSource customMessageSource;


    public CustomizedExceptionHandling(CustomMessageSource customMessageSource) {
        this.customMessageSource = customMessageSource;
    }

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<String>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            switch (error.getCode()){
                case "NotNull":
                    errors.add(customMessageSource.get("error.method.argument.notnull", error.getField()));
                    break;
                case "Pattern":
                    errors.add(customMessageSource.get("error.method.argument.pattern2", error.getField(),error.getDefaultMessage()));
                    break;
                default:
                    errors.add(error.getField() + ": " + error.getDefaultMessage());
            }
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ApiError apiError = new ApiError(false, httpStatus.value(), ex.getClass().getName(), errors);
        return handleExceptionInternal(ex, apiError, headers, httpStatus, request);
    }

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<String>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ApiError apiError = new ApiError(false, httpStatus.value(), ex.getClass().getName(), errors);
        return handleExceptionInternal(ex, apiError, headers, httpStatus, request);
    }

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final String error = customMessageSource.get("error.type.mismatch",ex.getValue(),ex.getPropertyName(),ex.getRequiredType());
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ApiError apiError = new ApiError(false, httpStatus.value(), ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final String error = customMessageSource.get("error.request.part.missing",ex.getRequestPartName());
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ApiError apiError = new ApiError(false, httpStatus.value(), ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final String error = customMessageSource.get("error.request.parameter.missing",ex.getParameterName());
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ApiError apiError = new ApiError(false, httpStatus.value(), ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }

    //

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    @ResponseBody
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final String error = customMessageSource.get("error.method.argument.mismatch",ex.getName(),ex.getRequiredType().getName());
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ApiError apiError = new ApiError(false, httpStatus.value(), ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }

    // 401

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> accessDenied(final AccessDeniedException ex, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        final ApiError apiError = new ApiError(false, httpStatus.value(), ex.getLocalizedMessage(), ex.getLocalizedMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }



    @ExceptionHandler({ ConstraintViolationException.class })
    @ResponseBody
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final List<String> errors = new ArrayList<String>();
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
//            errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage());
            errors.add(violation.getPropertyPath() + ": " + violation.getMessage());
        }
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ApiError apiError = new ApiError(false, httpStatus.value(), ex.getLocalizedMessage(), errors);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }

    @ExceptionHandler({ TransactionSystemException.class })
    @ResponseBody
    public ResponseEntity<Object> handleTransactionSystem(final TransactionSystemException e, final WebRequest request) {
        logger.info(e.getClass().getName());
        if (e.getRootCause() instanceof ConstraintViolationException){
            ConstraintViolationException ex = (ConstraintViolationException) e.getRootCause();
            final List<String> errors = new ArrayList<String>();
            for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
                if(violation.getMessage().contains("null"))
                    errors.add(customMessageSource.get("error.doesn't.exist",violation.getPropertyPath()));
                else
                    errors.add(violation.getPropertyPath() + " " + violation.getMessage());

            }
            HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
            final ApiError apiError = new ApiError(false, httpStatus.value(), ex.getLocalizedMessage(), errors);
            return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
        }
        return this.handleAll(e,request);
    }


    @ExceptionHandler({ DataIntegrityViolationException.class })
    @ResponseBody
    public ResponseEntity<Object> handleConstraintViolation(DataIntegrityViolationException ex, WebRequest request) {
        ex.printStackTrace();
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            logger.info(ex.getClass().getName());
            org.hibernate.exception.ConstraintViolationException violation  =  ((org.hibernate.exception.ConstraintViolationException) ex.getCause());
            //
            final List<String> errors = new ArrayList<String>();
//            errors.add("violates constraint "+violation.getConstraintName());
            System.out.println(violation.getCause().getLocalizedMessage());
            System.out.println(violation.getConstraintName());
            if(violation.getConstraintName().contains("unique_"))
                errors.add(customMessageSource.get("error.already.exist", violation.getConstraintName().split("unique_")[1]));
            else if(violation.getConstraintName().contains("_check"))
                errors.add(customMessageSource.get("error.check.constraint", violation.getConstraintName().split("_check")[0]));
            else if(violation.getCause().getLocalizedMessage().contains("not-null"))
                errors.add(customMessageSource.get("error.doesn't.exist", violation.getConstraintName()));
            else if(violation.getCause().getLocalizedMessage().contains("is not present in table"))
                errors.add(customMessageSource.get("error.doesn't.exist", violation.getConstraintName().replace("fk_","")));
            else if(violation.getCause().getLocalizedMessage().contains("is still referenced")){
                Matcher matcher = Pattern.compile("fk_(.*?)_[a-zA-Z]+_id").matcher(violation.getConstraintName());
                errors.add(customMessageSource.get("could.not.delete", matcher.find()?matcher.group(1):""));
            }
            else
                errors.add(customMessageSource.get("error.database.error"));
            HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
            final ApiError apiError = new ApiError(false, httpStatus.value(), violation.getCause().getClass().getName(), errors);
            return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
        }
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        final ApiError apiError = new ApiError(false, httpStatus.value(), ex.getClass().getName(), customMessageSource.get("error.database.error"));
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }

    // 404

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        final ApiError apiError = new ApiError(false, httpStatus.value(), ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }

    // 405

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));
        HttpStatus httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
        final ApiError apiError = new ApiError(false, httpStatus.value(), ex.getLocalizedMessage(), builder.toString());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }

    // 415

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));
        HttpStatus httpStatus = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        final ApiError apiError = new ApiError(false, httpStatus.value(), ex.getLocalizedMessage(), builder.substring(0, builder.length() - 2));
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }

    // 500


    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Bad Credentials")
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> userNotFound(UsernameNotFoundException exception) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        final ApiError apiError = new ApiError(false, httpStatus.value(), exception.getLocalizedMessage(), exception.getLocalizedMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
        logger.info(ex.getClass().getName());
        logger.error("error", ex);
        //
        ex.printStackTrace();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        final ApiError apiError = new ApiError(false, httpStatus.value(), ex.getLocalizedMessage(), ex.getLocalizedMessage());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), httpStatus);
    }

//    @Override
//    @ResponseBody
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
//        return  new ResponseEntity<>(new GlobalApiResponse(false,ex.getLocalizedMessage(),null,true),HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}
