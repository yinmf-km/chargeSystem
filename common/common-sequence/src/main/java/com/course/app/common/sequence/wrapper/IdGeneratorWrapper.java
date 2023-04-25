package com.course.app.common.sequence.wrapper;

import com.course.app.common.sequence.config.IdGeneratorProperties;
import com.course.app.common.sequence.generator.BasicIdGenerator;
import com.course.app.common.sequence.generator.MyIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 分布式Id生成器的封装类。该对象可根据配置选择不同的生成器实现类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Component
public class IdGeneratorWrapper {

    @Autowired
    private IdGeneratorProperties properties;
    /**
     * Id生成器接口对象。
     */
    private MyIdGenerator idGenerator;

    /**
     * 今后如果支持更多Id生成器时，可以在该函数内实现不同生成器的动态选择。
     */
    @PostConstruct
    public void init() {
        idGenerator = new BasicIdGenerator(properties.getSnowflakeWorkNode());
    }

    /**
     * 由于底层实现为synchronized方法，因此计算过程串行化，且线程安全。
     *
     * @return 计算后的全局唯一Id。
     */
    public long nextLongId() {
        return idGenerator.nextLongId();
    }

    /**
     * 由于底层实现为synchronized方法，因此计算过程串行化，且线程安全。
     *
     * @return 计算后的全局唯一Id。
     */
    public String nextStringId() {
        return idGenerator.nextStringId();
    }
}
