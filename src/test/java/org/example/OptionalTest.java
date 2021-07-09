package org.example;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Optional测试
 *
 * @author yanduohuang
 * @date 2021/7/8 19:14
 */
class OptionalTest {
    static class User {
        private String name;
        private String email;
        private String position;

        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Optional<String> getPosition() {
            return Optional.ofNullable(this.position);
        }

        public void setPosition(String position) {
            this.position = position;
        }
    }

    /**
     * 空optional
     */
    @Test
    void test1() {
        // 创建一个空的optional
        Optional<User> empty = Optional.empty();
        // 访问值抛出NoSuchElementException
        Assertions.assertThrows(NoSuchElementException.class, empty::get);
    }

    /**
     * of和ofNullable使用场景
     */
    @Test
    void test2() {
        // 使用of和ofNullable创建包含值的optional，null作为参数传给of会抛出NullPointerException
        Assertions.assertThrows(NullPointerException.class, ()->Optional.of(null));
        // 对象既可能是null也可能非null，应该使用ofNullable
        Assertions.assertDoesNotThrow(()->Optional.ofNullable(null));
    }

    /**
     * 不确定情况下不要调用get
     */
    @Test
    void test3() {
        // 成功创建包含null的optional
        Optional<String> optional = Optional.ofNullable(null);
        // 获取值时依然抛出NoSuchElementException
        Assertions.assertThrows(NoSuchElementException.class, optional::get);
    }

    /**
     * 确保值不为null再调用get
     */
    @Test
    void test4() {
        // 创建一个包含null或非null的optional
        Optional<String> optional = Optional.ofNullable("mary");
        // 获取值前需要验证是否有值
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals("mary", optional.get());
    }

    /**
     * ifPresent使用场景
     */
    @Test
    void test5() {
        Optional<String> optional = Optional.ofNullable("mary");
        // ifPresent是检测是否有值的方法，如果有值则运行lambda
        optional.ifPresent(value->Assertions.assertEquals("mary", value));
    }

    /**
     * orElse使用场景
     */
    @Test
    void test6() {
        // 没有值，取orElse中的默认值(可以传入函数)
        Object empty = Optional.ofNullable(null).orElse("mary");
        Assertions.assertEquals("mary", empty);
        // 有值，忽略默认值
        String lisa = Optional.ofNullable("lisa").orElse("mary");
        Assertions.assertEquals("lisa", lisa);
    }

    /**
     * orElse与orElse在值不为null情况下的区别（值为空时没有区别）
     */
    @Test
    void test7() {
        System.out.println("orElse run");
        // 执行task
        Optional.ofNullable("result").orElse(task());
        System.out.println("orElseGet run");
        // 不执行task
        Optional.ofNullable("result").orElseGet(this::task);
    }

    /**
     * orElseThrows使用场景
     */
    @Test
    void test8() {
        // 值为nulls时抛出自定义异常,而不总是NullPointException
        try {
            Optional.ofNullable(null).orElseThrow(IllegalArgumentException::new);
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    /**
     * map使用场景
     */
    @Test
    void test9() {
        User user = new User("mary", "mary@gmail.com");
        // map对值进行处理后包装成optional返回，保证链式调用的可能
        String email = Optional.ofNullable(user).map(User::getEmail).orElse("default@gmail.com");
        Assertions.assertEquals(user.getEmail(), email);
    }

    /**
     * flatMap使用场景
     */
    @Test
    void test10() {
        User user = new User("mary", "mary@gmail.com");
        user.setPosition("developer");
        // 解除被optional包装的值
        String position = Optional.ofNullable(user).flatMap(User::getPosition).orElse("default");
        Assertions.assertEquals(user.getPosition().get(),position);
    }

    /**
     * filter使用场景
     */
    @Test
    void test11() {
        User user = new User("mary", "mary@gmail.com");
        // 接受一个Predicate参数，若测试结果为false，则返回包含空的optional
        Optional<User> optional = Optional.ofNullable(user).filter(u -> u.getEmail() != null && u.getEmail().contains("@"));
        Assertions.assertTrue(optional.isPresent());
    }




    private String task() {
        System.out.println("task run");
        return "result";
    }
}
