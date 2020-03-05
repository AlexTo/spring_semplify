package ai.semplify.tasker.components;

import ai.semplify.tasker.entities.redis.Task;
import ai.semplify.tasker.listeners.TaskMessageListener;
import lombok.var;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
@EnableRedisRepositories
public class Redis {

    private RedisConnectionFactory redisConnectionFactory;
    private TaskMessageListener taskMessageListener;

    public Redis(RedisConnectionFactory redisConnectionFactory,
                 TaskMessageListener taskMessageListener) {
        this.redisConnectionFactory = redisConnectionFactory;
        this.taskMessageListener = taskMessageListener;
    }

    @Bean(name = "pendingTasksTopic")
    public ChannelTopic pendingTasksChannel() {
        return new ChannelTopic("tasker_pending_tasks");
    }

    public MessageListenerAdapter taskMessageListenerAdapter() {
        var adapter = new MessageListenerAdapter(taskMessageListener);
        adapter.setSerializer(new Jackson2JsonRedisSerializer<>(Task.class));
        adapter.afterPropertiesSet();
        return adapter;
    }


    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        var container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(taskMessageListenerAdapter(), pendingTasksChannel());
        return container;
    }

    @Bean
    public RedisTemplate<String, Task> taskRedisTemplate() {
        var template = new RedisTemplate<String, Task>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setValueSerializer(new Jackson2JsonRedisSerializer<Task>(Task.class));
        return template;
    }

}
