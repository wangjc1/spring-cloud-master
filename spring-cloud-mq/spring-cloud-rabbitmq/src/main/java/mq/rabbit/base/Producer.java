package mq.rabbit.base;

import org.apache.commons.lang.SerializationUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;
   
   
/** 
 *  
 * 功能概要：消息生产者 
 *  
 * @author linbingwen 
 * @since  2016年1月11日 
 */  
public class Producer extends ConnHandler {
       
    public Producer(String endPointName) throws IOException, TimeoutException {
        super(endPointName);  
    }  
   
    public void sendMessage(Serializable object) throws IOException {  
        channel.basicPublish("",endPointName, null, SerializationUtils.serialize(object));  
    }    
}  