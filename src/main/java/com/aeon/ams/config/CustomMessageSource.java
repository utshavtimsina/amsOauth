package com.aeon.ams.config;


import com.aeon.ams.utils.GlobalApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class CustomMessageSource {

    private final MessageSource messageSource;

    @Autowired
    public CustomMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String get(String code) {
        return messageSource.getMessage(code,null,null);
    }

    public String get(String code,Object... objects) {
        return messageSource.getMessage(code,objects,null);
    }

    public void getCustomGenericSuccessSave(GlobalApiResponse response){
        response.setResponse(this.get("success.save"),
                true,
                null);
    }

    public void getCustomGenericSuccessUpdate(GlobalApiResponse response){
        response.setResponse(this.get("success.update"),
                true,
                null);
    }

    public void getCustomGenericSuccessRetrive(GlobalApiResponse response){
        response.setResponse(this.get("success.retrieve"),
                true,
                null);
    }

    public void getCustomGenericSuccessDelete(GlobalApiResponse response){
        response.setResponse(this.get("success.delete"),
                true,
                null);
    }

//    protected String getGenericName() {
//        return ((Class<T>) ((ParameterizedType) getClass()
//                .getGenericSuperclass()).getActualTypeArguments()[0]).getTypeName();
//    }
//
//    public String getGenericMessage(String code) {
//        return messageSource.getMessage(this.getGenericName()+code,null,null);
//    }

}