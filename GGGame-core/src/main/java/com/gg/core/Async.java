package com.gg.core;

import java.lang.annotation.*;

/**
 * @author guofeng.qin
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Async {
}
