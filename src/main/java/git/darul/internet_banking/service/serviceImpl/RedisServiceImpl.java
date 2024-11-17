package git.darul.internet_banking.service.serviceImpl;

import git.darul.internet_banking.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(String key, String value, Duration duration) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(key, value, duration);
    }

    @Override
    public String get(String key) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public Boolean isExist(String key) {
        return redisTemplate.hasKey(key);
    }
}
