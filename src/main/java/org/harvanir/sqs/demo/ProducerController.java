package org.harvanir.sqs.demo;

import com.amazonaws.services.sqs.buffered.AmazonSQSBufferedAsyncClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Harvan Irsyadi
 */
@RestController
@RequestMapping("/producer")
public class ProducerController implements InitializingBean {

    private final AmazonSQSBufferedAsyncClient amazonSQS;

    private Map<String, String> queueMap = new HashMap<>();

    public ProducerController(AmazonSQSBufferedAsyncClient amazonSQS) {
        this.amazonSQS = amazonSQS;
    }

    @GetMapping("/send/topic/{topic}/message/{message}")
    public String send(@PathVariable String topic, @PathVariable String message) {
        String url = queueMap.get(topic);

        if (url != null) {
            return amazonSQS.sendMessage(url, message).getMessageId();
        }

        return "Queue not found.";
    }

    @Override
    public void afterPropertiesSet() {
        queueMap.put("SomeQueueName", "https://sqs.ap-southeast-1.amazonaws.com/123456789123/SomeQueueName");
    }
}
