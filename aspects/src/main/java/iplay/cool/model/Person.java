package iplay.cool.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dove
 * @date 2022/7/4 23:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Person extends Animal{
    private String name;
    private int age;
}
